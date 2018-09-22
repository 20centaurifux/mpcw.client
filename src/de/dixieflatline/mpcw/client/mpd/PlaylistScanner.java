package de.dixieflatline.mpcw.client.mpd;

import java.util.ArrayList;

import de.dixieflatline.mpcw.client.PlaylistItem;

public class PlaylistScanner
{
	private boolean firstItemFound;
	private String artist;
	private String title;
	private final ArrayList<IPlaylistScannerListener> listeners = new ArrayList<IPlaylistScannerListener>();
	
	public void addListener(IPlaylistScannerListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IPlaylistScannerListener listener)
	{
		listeners.remove(listener);
	}

	public void addLine(String line) throws FormatException
	{
		Pair<String, String> pair = ResponseParser.splitLineIntoPair(line);
		
		if(pair.getKey().equals("file"))
		{
			if(firstItemFound)
			{
				invokeListeners();
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
		invokeListeners();
	}
	
	private void invokeListeners()
	{
		if((artist != null && !artist.isEmpty())|| (title != null && !title.isEmpty()))
		{
			for(IPlaylistScannerListener listener : listeners)
			{
				PlaylistItem item = new PlaylistItem(artist, title);
				
				listener.onPlaylistItemFound(item);
			}
		}
	}
}