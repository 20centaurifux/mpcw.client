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

public class FindScanner
{
	private boolean firstItemFound;
	private String filename;
	private String artist;
	private String album;
	private int track;
	private String title;
	private final List<IFindScannerListener> listeners;
	
	public FindScanner()
	{
		listeners = new ArrayList<IFindScannerListener>();
	}

	public void reset()
	{
		firstItemFound = false;
		filename = null;
		artist = null;
		album = null;
		track = 0;
		title = null;
	}

	public void feed(String line) throws InvalidFormatException
	{
		Pair<String, String> pair = ResponseParser.splitLineIntoPair(line);
		
		if(pair.getKey().equals("file"))
		{
			if(firstItemFound)
			{
				raiseOnSongFound();
			}

			firstItemFound = true;
			filename = pair.getValue();
			artist = null;
			album = null;
			track = 0;
			title = null;
		}
		else if(pair.getKey().equals("Artist"))
		{
			artist = pair.getValue();
		}
		else if(pair.getKey().equals("Album"))
		{
			album = pair.getValue();
		}
		else if(pair.getKey().equals("Track"))
		{
			try
			{
				track = Integer.parseInt(pair.getValue());
			}
			catch(Exception ex)
			{
				track = 0;
			}
		}
		else if(pair.getKey().equals("Title"))
		{
			title = pair.getValue();
		}
	}
	
	public void flush()
	{
		raiseOnSongFound();
	}
	
	public void addListener(IFindScannerListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IFindScannerListener listener)
	{
		listeners.remove(listener);
	}

	private void raiseOnSongFound()
	{
		if(filename != null)
		{
			Song song = new Song(filename, artist, album, track, title);
			
			listeners.forEach((l) -> l.onSongFound(song));
		}
	}
}