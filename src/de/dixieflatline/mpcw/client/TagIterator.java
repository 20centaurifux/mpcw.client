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

import java.util.Iterator;

import de.dixieflatline.mpcw.client.mpd.IResponse;
import de.dixieflatline.mpcw.client.mpd.TagMapper;

public class TagIterator implements Iterator<Tag>
{
	private final String tagName;
	private final String linePrefix;
	private Iterator<String> source;
	private Tag tag;
	private int lineCount = 0;
	private int count;

	public TagIterator(ETag tag, IResponse response)
	{
		tagName = TagMapper.map(tag);
		linePrefix = String.format("%s:", tagName);
		this.tag = new Tag(tag);
		source = response.iterator();
		lineCount = response.size() - 1;
	}

	@Override
	public boolean hasNext()
	{
		return count < lineCount;
	}
	@Override
	public Tag next()
	{
		String line = source.next();
		
		if(line.startsWith(linePrefix))
		{
			String value = line.substring(linePrefix.length())
			                   .trim();
			
			tag.setValue(value);
		}
		else
		{
			throw new RuntimeException(new ProtocolException("Unexpected line: " + line));
		}

		++count;
		
		return tag;
	}
}