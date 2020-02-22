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

import java.util.ArrayList;
import java.util.List;

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.IBrowser;
import de.dixieflatline.mpcw.client.IClient;
import de.dixieflatline.mpcw.client.IPlayer;
import de.dixieflatline.mpcw.client.IPlaylist;
import de.dixieflatline.mpcw.client.PlaylistItem;
import de.dixieflatline.mpcw.client.ProtocolException;

public class Client implements IClient
{
	private final Channel channel;

	public Client(Channel channel)
	{
		this.channel = channel;
	}

	@Override
	public IPlayer getPlayer()
	{
		return new Player(channel);
	}
	
	@Override
	public IPlaylist getCurrentPlaylist() throws CommunicationException, ProtocolException
	{
		return new CurrentPlaylist(channel);
	}
	
	@Override
	public IPlaylist resyncCurrentPlaylist(IPlaylist playlist) throws CommunicationException, ProtocolException
	{
		List<PlaylistItem> list = new ArrayList<PlaylistItem>();

		playlist.getPlaylistItems().forEach((item) -> list.add(item));
	
		return new CurrentPlaylist(channel, list.toArray(new PlaylistItem[0]));
	}

	@Override
	public IBrowser getBrowser()
	{
		return new Browser(channel);
	}
}