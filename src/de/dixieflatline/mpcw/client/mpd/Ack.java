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