package de.dixieflatline.mpcw.client.mpd;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;

import de.dixieflatline.mpcw.client.*;

public class Playlist implements IPlaylist
{
	private final Channel channel;
	private final ArrayList<PlaylistItem> items = new ArrayList<PlaylistItem>();
	private int selectedIndex = -1;
	
	public Playlist(Channel channel)
	{
		this.channel = channel;
	}

	@Override
	public void synchronize() throws CommunicationException, ProtocolException
	{		
		IResponse response = channel.send("status");
		
		try
		{
			AbstractMap<String, String> m = ResponseParser.reponseToMap(response);
			
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
					scanner.addLine(line);
				}
			}

			scanner.flush();
		}
		catch(FormatException ex)
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