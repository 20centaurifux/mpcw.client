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

	private final URI uri;
	private final String hostname;
	private final int port;
	private final String password;
	private Channel channel;
	private Version version;
	private boolean _isIdle = false;

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

		this.uri = uri;
	}

	public Version getVersion()
	{
		return version;
	}

	@Override
	public URI getURI()
	{
		return this.uri;
	}
	
	@Override
	public boolean isConnected()
	{
		return channel != null && channel.isConnected();
	}
	
	@Override
	public void connect() throws CommunicationException, AuthenticationException
	{
		if(!isConnected())
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
				channel.close();

				throw new AuthenticationException("Authentication failed.");
			}
		}
	}

	@Override
	public void disconnect() throws CommunicationException
	{
		try
		{
			ConnectionPool.getInstance()
		                  .dispose(this);
		}
		catch(Exception ex)
		{
			throw new CommunicationException(ex);
		}
	}

	@Override
	public boolean ping() throws CommunicationException, ProtocolException
	{
		IResponse response = channel.send("ping");

		return response instanceof Ok;
	}

	@Override
	public void idle() throws CommunicationException, ProtocolException
	{
		if(!_isIdle)
		{
			channel.writeLine("idle");
			_isIdle = true;
		}
	}

	@Override
	public void noidle() throws CommunicationException, ProtocolException
	{
		if(_isIdle)
		{
			IResponse response = channel.send("noidle");

			if(response instanceof Ok)
			{
				_isIdle = false;
			}
		}
	}

	@Override
	public boolean isIdle()
	{
		return _isIdle;
	}

	@Override
	public IClient getClient()
	{
		return new Client(channel);
	}
}