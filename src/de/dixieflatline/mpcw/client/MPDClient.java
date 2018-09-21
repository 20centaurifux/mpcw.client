package de.dixieflatline.mpcw.client;

public class MPDClient implements IClient
{
	private Channel channel;
	
	public MPDClient(Channel channel)
	{
		this.channel = channel;
	}
	
	public IPlayer createPlayer()
	{
		return new MPDPlayer(channel);
	}
}