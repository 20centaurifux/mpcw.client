package de.dixieflatline.mpcw.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Channel
{
	Socket socket;
	BufferedReader reader;
	DataOutputStream writer;
	
	public Channel(Socket socket) throws IOException
	{
		this.socket = socket;

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new DataOutputStream(socket.getOutputStream());
	}

	public void send(String... commands) throws CommunicationException, AckException
	{
		boolean isCommandList = commands.length > 0;
		
		if(isCommandList)
		{
			writeLine("command_list_begin");
		}
		
		for(String command : commands)
		{
			writeLine(command);
		}

		if(isCommandList)
		{
			writeLine("command_list_end");
		}
	}
	
	public IResponse request(String... commands) throws CommunicationException, AckException
	{
		send(commands);

		return receive();
	}
	
	private void writeLine(String line) throws CommunicationException
	{
		try
		{
			writer.writeBytes(line + '\n');	
		}
		catch(IOException ex)
		{
			throw new CommunicationException(ex);
		}
	}
	
	public IResponse receive() throws CommunicationException, AckException
	{
		ArrayList<String> lines = new ArrayList<String>();
		String line = null;
		IResponse response = null;
		
		while(response == null)
		{
			try
			{
				line = reader.readLine().trim();
			}
			catch(IOException ex)
			{
				throw new CommunicationException(ex);
			}
			
			lines.add(line);

			if(line.startsWith("ACK"))
			{
				throw new AckException(new Ack(line));
			}
			else if(line.startsWith("OK"))
			{
				response = new Ok(lines);
			}
		}
		
		return response;
	}

	public void close() throws CommunicationException
	{
		try
		{
			socket.close();
		}
		catch(IOException ex)
		{
			throw new CommunicationException(ex);
		}
	}
}