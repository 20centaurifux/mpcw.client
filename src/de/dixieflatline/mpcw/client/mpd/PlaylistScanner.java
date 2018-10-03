/***************************************************************************
    begin........: September 2018
    copyright....: Sebastian Fedrau
    email........: sebastian.fedrau@gmail.com
 ***************************************************************************/

/***************************************************************************
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License v3 as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    General Public License v3 for more details.
 ***************************************************************************/
package de.dixieflatline.mpcw.client.mpd;

import java.util.List;
import java.util.ArrayList;

import de.dixieflatline.mpcw.client.Song;

public class PlaylistScanner
{
	private boolean firstItemFound;
	private String artist;
	private String title;
	private final List<IPlaylistScannerListener> listeners;
	
	public PlaylistScanner()
	{
		listeners = new ArrayList<IPlaylistScannerListener>();
	}

	public void reset()
	{
		firstItemFound = false;
		artist = null;
		title = null;
	}

	public void feed(String line) throws InvalidFormatException
	{
		Pair<String, String> pair = ResponseParser.splitLineIntoPair(line);
		
		if(pair.getKey().equals("file"))
		{
			if(firstItemFound)
			{
				raiseOnPlaylistItemFound();
			}

			firstItemFound = true;
			artist = null;
			title = null;
		}
		else if(pair.getKey().equals("Artist"))
		{
			artist = pair.getValue();
		}
		else if(pair.getKey().equals("Title"))
		{
			title = pair.getValue();
		}
	}
	
	public void flush()
	{
		raiseOnPlaylistItemFound();
	}
	
	public void addListener(IPlaylistScannerListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IPlaylistScannerListener listener)
	{
		listeners.remove(listener);
	}

	private void raiseOnPlaylistItemFound()
	{
		if((artist != null && !artist.isEmpty())|| (title != null && !title.isEmpty()))
		{
			for(IPlaylistScannerListener listener : listeners)
			{
				Song item = new Song(artist, title);
				
				listener.onPlaylistItemFound(item);
			}
		}
	}
}