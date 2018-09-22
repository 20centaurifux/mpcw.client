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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.ProtocolException;;

public class Channel
{
	private Socket socket;
	private final BufferedReader reader;
	private final DataOutputStream writer;

	public Channel(Socket socket) throws IOException
	{
		this.socket = socket;

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new DataOutputStream(socket.getOutputStream());
	}

	public IResponse send(String... commands) throws CommunicationException, ProtocolException
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
			throwCommunicationExceptionAndShutdown(ex);
		}
	}
	
	public IResponse receive() throws CommunicationException, ProtocolException
	{
		ArrayList<String> lines = new ArrayList<String>();
		String line = null;
		IResponse response = null;
		
		try
		{
			while(response == null)
			{
				try
				{
					line = reader.readLine().trim();
				}
				catch(IOException ex)
				{
					throwCommunicationExceptionAndShutdown(ex);
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
		}
		catch(AckException ex)
		{
			throw new ProtocolException(ex);
		}
			
		return response;
	}
	
	private void throwCommunicationExceptionAndShutdown(Exception cause) throws CommunicationException
	{
		try
		{
			socket.close();
		}
		catch (IOException ex) { }

		socket = null;
		
		throw new CommunicationException(cause);
	}
	
	public boolean isConnected()
	{
		return socket != null && socket.isConnected();
	}

	public void close() throws CommunicationException
	{
		if(socket != null)
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
}