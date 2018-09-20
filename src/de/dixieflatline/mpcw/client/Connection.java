package de.dixieflatline.mpcw.client;

import java.io.*;
import java.net.*;

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
	
	public void connect() throws UnknownHostException, IOException, AckException, UnexpectedResponseException, InvalidVersionFormatException
	{
		openChannel();
		detectVersion();
	}
	
	public void connect(Credentials credentials) throws UnknownHostException, IOException, AckException, UnexpectedResponseException, InvalidVersionFormatException
	{
		connect();
		authenticate(credentials);
	}

	private void openChannel() throws UnknownHostException, IOException
	{
		Socket socket = new Socket(hostname, port);

		channel = new Channel(socket);
	}

	private void detectVersion() throws IOException, AckException, UnexpectedResponseException, InvalidVersionFormatException
	{
		IResponse response = channel.receive();
		
		if(response.size() != 1)
		{
			throw new UnexpectedResponseException("Couldn't detect MPD version.");
		}
			
		String line = response.iterator().next();
		
		version = Version.parse(line);
	}

	private void authenticate(Credentials credentials)
	{
	}
	
	public void disconnect() throws IOException
	{
		channel.close();
	}
	
	public IClient createClient()
	{
		return null;
	}
}