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

import java.util.Map;
import java.util.HashMap;

public final class ResponseParser
{
	public static Map<String, String> reponseToMap(IResponse response) throws InvalidFormatException
	{
		Map<String, String> m = new HashMap<String, String>();
		
		for(String line : response)
		{
			if(!line.equals("OK"))
			{
				Pair<String, String> pair = splitLineIntoPair(line);

				m.put(pair.getKey(), pair.getValue());
			}
		}

		return m;
	}
	
	public static Pair<String, String> splitLineIntoPair(String line) throws InvalidFormatException
	{
		int offset = line.indexOf(':');

		if(offset == -1)
		{
			throw new InvalidFormatException("No colon found in line.");
		}
		
		String key = line.substring(0, offset);
		String value = line.substring(offset + 1).trim();

		return new Pair<String, String>(key, value);
	}
}
