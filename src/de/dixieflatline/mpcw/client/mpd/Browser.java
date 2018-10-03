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

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.ETag;
import de.dixieflatline.mpcw.client.Filter;
import de.dixieflatline.mpcw.client.IBrowser;
import de.dixieflatline.mpcw.client.ISearchResult;
import de.dixieflatline.mpcw.client.IteratorSearchResult;
import de.dixieflatline.mpcw.client.ProtocolException;
import de.dixieflatline.mpcw.client.Tag;
import de.dixieflatline.mpcw.client.TagIterator;

public class Browser implements IBrowser
{
	private final Channel channel;
	
	public Browser(Channel channel)
	{
		this.channel = channel;
	}

	@Override
	public ISearchResult<Tag> findTags(ETag tag) throws CommunicationException, ProtocolException
	{
		IResponse response = channel.send("list " + TagMapper.map(tag));
	
		return toSearchResult(tag, response);
	}

	@Override
	public ISearchResult<Tag> findTags(ETag tag, Filter[] filter) throws CommunicationException, ProtocolException
	{
		StringBuilder builder = new StringBuilder();
		String tagName = TagMapper.map(tag);
		
		builder.append("list ");
		builder.append(tagName);
		
		for(Filter f : filter)
		{
			String value = f.getValue()
			                .replaceAll("\"", "\\\\\"");

			builder.append(" ");
			builder.append(TagMapper.map(f.getTag()));
			builder.append(" \"");
			builder.append(value);
			builder.append("\"");
		}
		
		System.out.println(builder.toString());
		
		IResponse response = channel.send(builder.toString());
		
		return toSearchResult(tag,  filter, response);
	}
	
	private static IteratorSearchResult<Tag> toSearchResult(ETag tag, IResponse response) throws ProtocolException
	{
		return toSearchResult(tag, null, response);
	}
	
	private static IteratorSearchResult<Tag> toSearchResult(ETag tag, Filter[] filter, IResponse response) throws ProtocolException
	{
		Iterator<Tag> iterator = new TagIterator(tag, response);

		return filter == null ? new IteratorSearchResult<Tag>(iterator)
		                      : new IteratorSearchResult<Tag>(iterator, filter);
	}
}