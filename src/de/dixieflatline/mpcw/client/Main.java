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
package de.dixieflatline.mpcw.client;

import de.dixieflatline.mpcw.diff.*;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Index<String> index = new Index<String>();
		
		int[] a = index.insert(new String[] { "to", "be", "or", "not", "to", "be" });
		int[] b = index.insert(new String[] { "to", "fnord", "or", "not", "to", "fnord" });

		Myers myers = new Myers(a, b);
		Trace trace = myers.shortestEdit();
		Edge[] edges = myers.backtrack(trace);
		ITransformation[] script = myers.editScript(edges);
		
		for(ITransformation transformation : script)
		{
			if(transformation instanceof Insert)
			{
				@SuppressWarnings("unchecked")
				Insert<Integer> insert = (Insert<Integer>)transformation;
				
				System.out.format("INSERT %s at %d\n", index.map(insert.getItem()), insert.getIndex());
			}
			else
			{
				Delete del = (Delete)transformation;
				System.out.format("DELETE %d-%d\n", del.getIndex(), del.getLength());
			}
		}

		/*
		IConnection conn = Connection.create("mpd://localhost:6600");

		conn.connect();

		IClient client = conn.getClient();
		IPlayer player = client.getPlayer();
		Status status = player.getStatus();

		System.out.println(status.getState());
		System.out.println(status.hasPrevious());
		System.out.println(status.hasNext());
		System.out.println(status.getArtist());
		System.out.println(status.getTitle());
		
		player.stop();
		
		IPlaylist playlist = client.getCurrentPlaylist();
		
		System.out.println(playlist.size());
		System.out.println(playlist.selectedIndex());

		for(PlaylistItem item : playlist)
		{
			System.out.println(item.getArtist());
			System.out.println(item.getTitle());
		}

		playlist.selectAndPlay(0);

		conn.disconnect();
		*/
	}
}