package de.dixieflatline.mpcw.client;

import java.util.AbstractMap;
import java.util.HashMap;

public final class ResponseConverter
{
	public static AbstractMap<String, String> reponseToMap(IResponse response) throws ProtocolException
	{
		AbstractMap<String, String> m = new HashMap<String, String>();
		
		for(String line : response)
		{
			if(!line.equals("OK"))
			{
				Pair<String, String> pair = parseLine(line);

				m.put(pair.getKey(), pair.getValue());
			}
		}

		return m;
	}

	static Pair<String, String> parseLine(String line) throws ProtocolException
	{
		int offset = line.indexOf(':');

		if(offset == -1)
		{
			throw new ProtocolException("No colon found in received line.");
		}
		
		String key = line.substring(0, offset);
		String value = line.substring(offset + 1, line.length())
				           .trim();
		
		return new Pair<String, String>(key, value);
	}
}
