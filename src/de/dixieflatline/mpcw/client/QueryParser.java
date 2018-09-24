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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;

public final class QueryParser
{
	public static Map<String, String> parse(String query)
	{
		Map<String, String> m = new HashMap<String, String>();

		if(query != null && !query.isEmpty())
		{
			for(String pair : query.split("&"))
			{
				int offset = pair.indexOf("=");
				
				if(offset != -1)
				{
					String key;
					String value;

					try
					{
						key = URLDecoder.decode(pair.substring(0, offset), "UTF-8")
								        .trim();

						value = URLDecoder.decode(pair.substring(offset + 1), "UTF-8")
								          .trim();
					}
					catch (UnsupportedEncodingException e)
					{
						key = null;
						value = null;
					}
					
					if(key != null && value != null)
					{
						m.put(key, value);
					}
				}
			}
		}

		return m;
	}
}