package de.dixieflatline.mpcw.client;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;

public final class Connection
{
	public static IConnection create(String connectionString) throws URISyntaxException, DispatchException
	{
		URI uri = new URI(connectionString);
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
}