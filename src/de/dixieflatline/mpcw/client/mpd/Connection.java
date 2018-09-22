package de.dixieflatline.mpcw.client.mpd;

import java.net.Socket;
import java.net.URI;
import java.util.AbstractMap;

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
		
		AbstractMap<String, String> params = QueryParser.parse(uri.getQuery());

		password = params.getOrDefault("password", null);
	}

	@Override
	public boolean isConnected()
	{
		return channel.isConnected();
	}
	
	@Override
	public void connect() throws CommunicationException
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
			catch(InvalidVersionFormatException ex)
			{
				throw new ProtocolException(ex.getMessage());
			}	
		}
		else
		{
			throw new ProtocolException("Couldn't detect MPD version.");
		}			
	}

	private void authenticate()
	{
		// @TODO
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