package de.dixieflatline.mpcw.client.mpd;

import java.util.AbstractMap;

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.EState;
import de.dixieflatline.mpcw.client.IPlayer;
import de.dixieflatline.mpcw.client.ProtocolException;
import de.dixieflatline.mpcw.client.Status;

public class Player implements IPlayer
{
	private final Channel channel;

	public Player(Channel channel)
	{
		this.channel = channel;
	}
	
	@Override
	public Status getStatus() throws CommunicationException, ProtocolException
	{
		Status status = new Status();
		
		try
		{
			IResponse response = channel.send("status");
			
			AbstractMap<String, String> m = ResponseParser.reponseToMap(response);
		
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
	
				response = channel.send("playlistid " + songId);
	
				m = ResponseParser.reponseToMap(response);
	
				if(m.containsKey("Artist"))
				{
					status.setArtist(m.get("Artist"));
				}
				
				if(m.containsKey("Title"))
				{
					status.setTitle(m.get("Title"));
				}
			}
		}
		catch(FormatException ex)
		{
			throw new ProtocolException(ex);
		}
			
		return status;
	}
	
	@Override
	public void play() throws CommunicationException
	{
		channel.send("play");
	}
	
	@Override
	public void pause() throws CommunicationException
	{
		channel.send("pause");
	}
	
	@Override
	public void stop() throws CommunicationException
	{
		channel.send("stop");
	}
	
	@Override
	public void next() throws CommunicationException
	{
		channel.send("next");
	}
	
	@Override
	public void previous() throws CommunicationException
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