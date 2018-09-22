package de.dixieflatline.mpcw.client.mpd;

import java.util.Iterator;
import java.util.Arrays;

public class Ack implements IResponse
{
	final private String[] response;
	
	public Ack(String message)
	{
		response = new String[] { message };
	}
	
	@Override
	public Iterator<String> iterator()
	{
		return Arrays.asList(response).iterator();
	}

	@Override
	public int size()
	{
		return 1;
	}
}