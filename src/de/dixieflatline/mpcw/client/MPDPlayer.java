package de.dixieflatline.mpcw.client;

import java.util.AbstractMap;

public class MPDPlayer implements IPlayer
{
	private Channel channel;

	public MPDPlayer(Channel channel)
	{
		this.channel = channel;
	}
	
	@Override
	public Status getStatus() throws CommunicationException, AckException, ProtocolException
	{
		Status status = new Status();
		
		IResponse response = channel.request("status");
		
		AbstractMap<String, String> m = ResponseConverter.reponseToMap(response);
	
		EState state = parseState(m.get("state"));
		
		status.setState(state);
		
		if(m.containsKey("song") && !m.get("song").equals("0"))
		{
			status.setHasPrevious(true);
		}
		
		if(m.containsKey("nextsong"))
		{
			status.setHasNext(true);
		}

		if(m.containsKey("songid"))
		{
			String songId = m.get("songid");

			response = channel.request("playlistid " + songId);

			m = ResponseConverter.reponseToMap(response);

			if(m.containsKey("Artist"))
			{
				status.setArtist(m.get("Artist"));
			}
			
			if(m.containsKey("Title"))
			{
				status.setTitle(m.get("Title"));
			}
		}
		
		return status;
	}
	
	@Override
	public void play() throws CommunicationException, AckException
	{
		channel.send("play");
	}
	
	@Override
	public void pause() throws CommunicationException, AckException
	{
		channel.send("pause");
	}
	
	@Override
	public void stop() throws CommunicationException, AckException
	{
		channel.send("stop");
	}
	
	@Override
	public void next() throws CommunicationException, AckException
	{
		channel.send("next");
	}
	
	@Override
	public void previous() throws CommunicationException, AckException
	{
		channel.send("previous");
	}

	static EState parseState(String name) throws ProtocolException
	{
		EState state = EState.Stop;
		
		if(name.equals("stop"))
		{
			state = EState.Stop;
		}
		else if(name.equals("play"))
		{
			state = EState.Play;
		}
		else if(name.equals("pause"))
		{
			state = EState.Pause;
		}
		else
		{
			throw new ProtocolException("Unknown state: " + name);
		}
		
		return state;
	}
}