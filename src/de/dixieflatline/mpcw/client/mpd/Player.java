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

import java.util.Map;

import de.dixieflatline.mpcw.client.*;

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
			
			Map<String, String> m = ResponseParser.reponseToMap(response);
		
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
		catch(InvalidFormatException ex)
		{
			throw new ProtocolException(ex);
		}
			
		return status;
	}
	
	@Override
	public void play() throws CommunicationException, ProtocolException
	{
		channel.send("play");
	}
	
	@Override
	public void pause() throws CommunicationException, ProtocolException
	{
		channel.send("pause");
	}
	
	@Override
	public void stop() throws CommunicationException, ProtocolException
	{
		channel.send("stop");
	}
	
	@Override
	public void next() throws CommunicationException, ProtocolException
	{
		channel.send("next");
	}
	
	@Override
	public void previous() throws CommunicationException, ProtocolException
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