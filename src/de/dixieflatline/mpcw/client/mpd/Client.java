package de.dixieflatline.mpcw.client.mpd;

import de.dixieflatline.mpcw.client.IClient;
import de.dixieflatline.mpcw.client.IPlayer;

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
}