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

import java.util.*;

import de.dixieflatline.mpcw.client.*;
import de.dixieflatline.mpcw.diff.*;

public class CurrentPlaylist implements IPlaylist
{
	private final Channel channel;
	private PlaylistItem[] items;
	private int selectedIndex = -1;
	private final Index<PlaylistItem> index;
	
	public CurrentPlaylist(Channel channel)
	{
		items = new PlaylistItem[0];
		this.channel = channel;
		index = new Index<PlaylistItem>();
	}

	public CurrentPlaylist(Channel channel, PlaylistItem[] items)
	{
		this.items = items;
		this.channel = channel;
		index = new Index<PlaylistItem>();
	}

	@Override
	public Iterable<PlaylistItem> getPlaylistItems()
	{
		return Arrays.asList(items);
	}

	@Override
	public void appendSong(Song song) throws CommunicationException, ProtocolException
	{
		appendSong(song.getFilename());
	}

	@Override
	public void appendSong(String filename) throws CommunicationException, ProtocolException
	{
		channel.send("add " + EscapeUtil.quote(filename));
	}
	
	@Override
	public void appendSongs(Iterable<Song> songs) throws CommunicationException, ProtocolException
	{
		BulkGenerator<String> generator = new BulkGenerator<String>(50);
		
		generator.addListener(bulk ->
		{
			try
			{
				channel.send(bulk);
			}
			catch (CommunicationException | ProtocolException ex)
			{
				throw new RuntimeException(ex);
			}
		});

		try
		{
			for(Song song : songs)
			{
				generator.add("add " + EscapeUtil.quote(song.getFilename()));
			}

			generator.flush();
		}
		catch(RuntimeException ex)
		{
			Throwable cause = ex.getCause();
	
			if(cause instanceof CommunicationException)
			{
				throw (CommunicationException)cause;
			}
			if(cause instanceof ProtocolException)
			{
				throw (ProtocolException)cause;
			}

			throw ex;
		}
	}

	@Override
	public Iterable<ITransformation> synchronize() throws CommunicationException, ProtocolException
	{
		try
		{
			selectedIndex = loadSelectedIndex();

			PlaylistItem[] newItems = loadPlaylistItems();

			Iterable<ITransformation> iterable = new Iterable<ITransformation>()
			{
				private final ITransformation[] patch = createPatch(newItems);
				private int offset = 0;

				@Override
				public Iterator<ITransformation> iterator()
				{					
					return new Iterator<ITransformation>()
					{	
						@Override
						public boolean hasNext()
						{
							return offset < patch.length;
						}

						@Override
						public ITransformation next()
						{
							ITransformation transformation = patch[offset++];

							if(transformation instanceof InsertFrom)
							{
								InsertFrom insertFrom = (InsertFrom)transformation;

								transformation = new InsertPlaylistItem(insertFrom.getOffset(), index.map(insertFrom.getFrom()));
							}
							
							return transformation;
						}
					};
				}
			};

			items = newItems;
			
			return iterable;
		}
		catch(InvalidFormatException ex)
		{
			throw new ProtocolException(ex);
		}
	}

	private PlaylistItem[] loadPlaylistItems() throws CommunicationException, ProtocolException, InvalidFormatException
	{
		IResponse response = channel.send("playlistinfo");		
		PlaylistScanner scanner = new PlaylistScanner();
		List<PlaylistItem> items = new ArrayList<PlaylistItem>();

		scanner.addListener((item) -> items.add(item));

		for(String line : response)
		{
			if(!line.equals("OK"))
			{
				scanner.feed(line);
			}
		}

		scanner.flush();
		
		return items.toArray(new PlaylistItem[0]);
	}

	private ITransformation[] createPatch(PlaylistItem[] newItems)
	{
		int[] a = index.insert(items);
		int[] b = index.insert(newItems);

		Myers myers = new Myers(a, b);
		Trace trace = myers.shortestEdit();
		Edge[] edges = myers.backtrack(trace);
		ITransformation[] patch = myers.editScript(edges);

		return patch;
	}

	private int loadSelectedIndex() throws CommunicationException, ProtocolException, InvalidFormatException
	{
		IResponse response = channel.send("status");
		Map<String, String> m = ResponseParser.reponseToMap(response);

		return Integer.parseInt(m.getOrDefault("song", "-1"));		
	}

	@Override
	public int selectedIndex()
	{
		return selectedIndex;
	}

	@Override
	public Iterable<ITransformation> selectAndPlay(int offset, EnumSet<SelectAndPlayFlags> flags) throws ProtocolException, CommunicationException
	{
		if(flags.contains(SelectAndPlayFlags.NoRangeCheck) || offsetIsInRange(offset))
		{
			channel.send("play " + offset);
		}
		
		return synchronize();
	}
	
	private boolean offsetIsInRange(int offset)
	{
		int size = items.length;

		return size > 0 && offset >= 0 && offset < size;
	}
}