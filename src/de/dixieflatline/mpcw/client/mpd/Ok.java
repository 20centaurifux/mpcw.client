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

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Ok implements IResponse
{
	private final List<String> response;
	
	public Ok(Collection<String> response)
	{
		this.response = new ArrayList<String>(response);
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