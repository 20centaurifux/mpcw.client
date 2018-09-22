package de.dixieflatline.mpcw.client.mpd;

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.IClient;
import de.dixieflatline.mpcw.client.IPlayer;
import de.dixieflatline.mpcw.client.IPlaylist;
import de.dixieflatline.mpcw.client.ProtocolException;

public class Client implements IClient
{
	private final Channel channel;
	
	public Client(Channel channel)
	{
		this.channel = channel;
	}
	
	public IPlayer getPlayer()
	{
		return new Player(channel);
	}
	
	public IPlaylist getCurrentPlaylist() throws CommunicationException, ProtocolException
	{
		IPlaylist playlist = new Playlist(channel);
		
		playlist.synchronize();
		
		return playlist;
	}
}