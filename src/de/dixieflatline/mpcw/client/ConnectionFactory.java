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
package de.dixieflatline.mpcw.client;

import java.lang.reflect.Constructor;
import java.net.URI;

public final class ConnectionFactory implements IFactory<IConnection>
{
	@Override
	public IConnection create(URI uri) throws DispatchException
	{
		String className = buildClassName(uri);
		IConnection connection = null;
		
		try
		{
			Class<?> cls = Class.forName(className);
			Constructor<?> constructor = cls.getConstructor(URI.class);
			
			connection = (IConnection)constructor.newInstance(uri);
		}
		catch(Exception ex)
		{
			throw new DispatchException(ex);
		};

		return connection;
	}

	private static final String buildClassName(URI uri)
	{
		String packageName = Connection.class.getPackage().getName();
		String className = String.format("%s.%s.Connection", packageName, uri.getScheme());
		
		return className;
	}

	@Override
	public void idle(IConnection connection) throws Exception
	{
		if(!connection.isIdle())
		{
			connection.idle();
		}
	}

	@Override
	public IConnection recycle(IConnection connection) throws Exception
	{
		if(connection.isIdle())
		{
			connection.noidle();
		}

		return connection;
	}

	@Override
	public void dispose(IConnection connection)
	{
		try
		{
			if(connection.isConnected())
			{
				connection.disconnect();
			}
		}
		catch(CommunicationException ex) { }
	}

	@Override
	public boolean valid(IConnection connection)
	{
		boolean connected = false;
		
		try
		{
			if(connection.isConnected())
			{
				boolean wasIdle = connection.isIdle();

				if(wasIdle)
				{
					connection.noidle();
				}

				connected = connection.ping();

				if(wasIdle)
				{
					connection.idle();
				}
			}
		}
		catch(CommunicationException | ProtocolException ex)
		{
			connected = false;
		}

		return connected;
	}
}