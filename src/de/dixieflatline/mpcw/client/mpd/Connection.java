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

import java.net.Socket;
import java.net.URI;
import java.util.Map;

import de.dixieflatline.mpcw.client.*;

public class Connection implements IConnection
{
	private static int DEFAULT_PORT = 6600;

	private final String hostname;
	private final int port;
	private final String password;
	private Channel channel;
	private Version version;
	
	public Connection(URI uri)
	{
		hostname = uri.getHost();
		
		int port = uri.getPort();
		
		if(port == -1)
		{
			this.port = DEFAULT_PORT;
		}
		else
		{
			this.port = port;
		}
		
		Map<String, String> params = QueryParser.parse(uri.getQuery());

		password = params.getOrDefault("password", null);
	}

	public Version getVersion()
	{
		return version;
	}
	
	@Override
	public boolean isConnected()
	{
		return channel != null && channel.isConnected();
	}
	
	@Override
	public void connect() throws CommunicationException, AuthenticationException
	{
		openChannel();
	
		try
		{
			detectVersion();
			authenticate();
		}
		catch (AckException | ProtocolException ex)
		{
			throw new CommunicationException(ex);
		}
	}
	
	private void openChannel() throws CommunicationException
	{
		try
		{
			Socket socket = new Socket(hostname, port);

			channel = new Channel(socket);
		}
		catch(Exception ex)
		{
			throw new CommunicationException(ex);
		}
	}

	private void detectVersion() throws CommunicationException, AckException, ProtocolException
	{
		IResponse response = channel.receive();
		
		if(response.size() == 1)
		{
			String line = response.iterator().next();
			
			try
			{
				version = Version.parse(line);
			}
			catch(InvalidFormatException ex)
			{
				throw new ProtocolException(ex.getMessage());
			}	
		}
		else
		{
			throw new ProtocolException("Couldn't detect MPD version.");
		}			
	}

	private void authenticate() throws AuthenticationException, CommunicationException
	{
		if(password != null)
		{
			try
			{
				channel.send("password " + password);
			}
			catch(ProtocolException ex)
			{
				throw new AuthenticationException();
			}
		}
	}

	@Override
	public void disconnect() throws CommunicationException
	{
		channel.close();
	}
	
	@Override
	public IClient getClient()
	{
		return new Client(channel);
	}
}