package de.dixieflatline.mpcw.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.HashMap;

public final class QueryParser
{
	public static AbstractMap<String, String> parse(String query)
	{
		AbstractMap<String, String> m = new HashMap<String, String>();

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