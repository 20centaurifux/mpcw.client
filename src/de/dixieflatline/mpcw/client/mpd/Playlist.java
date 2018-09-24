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
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import de.dixieflatline.mpcw.client.*;

public class Playlist implements IPlaylist
{
	private final Channel channel;
	private final List<PlaylistItem> items;
	private int selectedIndex = -1;
	
	public Playlist(Channel channel)
	{
		items = new ArrayList<PlaylistItem>();
		this.channel = channel;
	}

	@Override
	public void synchronize() throws CommunicationException, ProtocolException
	{		
		IResponse response = channel.send("status");
		
		try
		{
			Map<String, String> m = ResponseParser.reponseToMap(response);
			
			selectedIndex = Integer.parseInt(m.getOrDefault("song", "-1"));
			
			response = channel.send("playlistinfo");
			
			PlaylistScanner scanner = new PlaylistScanner();
			
			scanner.addListener(new IPlaylistScannerListener()
			{	
				@Override
				public void onPlaylistItemFound(PlaylistItem item)
				{
					items.add(item);	
				}
			});

			for(String line : response)
			{
				if(!line.equals("OK"))
				{
					scanner.feed(line);
				}
			}

			scanner.flush();
		}
		catch(InvalidFormatException ex)
		{
			throw new ProtocolException(ex);
		}
	}
	
	@Override
	public Iterator<PlaylistItem> iterator()
	{
		return items.iterator();
	}

	@Override
	public int size()
	{
		return items.size();
	}
	
	@Override
	public PlaylistItem get(int offset)
	{
		return items.get(offset);
	}

	@Override
	public int selectedIndex()
	{
		return selectedIndex;
	}
	
	@Override
	public void selectAndPlay(int offset) throws ProtocolException, CommunicationException
	{
		if(offsetIsInRange(offset))
		{
			channel.send("play " + offset);
		}
	}
	
	private boolean offsetIsInRange(int offset)
	{
		int size = items.size();

		return size > 0 && offset >= 0 && offset < size;
	}
}