package de.dixieflatline.mpcw.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Ok implements IResponse
{
	private ArrayList<String> response = new ArrayList<String>();

	public Ok() { }
	
	public Ok(Collection<String> response)
	{
		this.response.addAll(response);
	}
	
	public void writeLine(String line)
	{
		response.add(line);
	}

	@Override
	public Iterator<String> iterator()
	{
		return response.iterator();
	}

	@Override
	public int size()
	{
		return response.size();
	}
}