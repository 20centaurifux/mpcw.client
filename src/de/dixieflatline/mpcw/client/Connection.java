package de.dixieflatline.mpcw.client;

import java.net.Socket;

public class Connection
{
	private String hostname;
	private int port;
	private Channel channel;
	private Version version;
	
	public Connection(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname()
	{
		return hostname;
	}
	
	public int getPort()
	{
		return port;
	}

	public boolean isConnected()
	{
		return false;
	}
	
	public Version getVersion()
	{
		return version;
	}
	
	public void connect() throws CommunicationException, AckException, ProtocolException
	{
		openChannel();
		detectVersion();
	}
	
	public void connect(Credentials credentials) throws CommunicationException, AckException, ProtocolException
	{
		connect();
		authenticate(credentials);
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

	private void authenticate(Credentials credentials)
	{
	}
	
	public void disconnect() throws CommunicationException
	{
		channel.close();
	}
	
	public IClient createClient()
	{
		return new MPDClient(channel);
	}
}