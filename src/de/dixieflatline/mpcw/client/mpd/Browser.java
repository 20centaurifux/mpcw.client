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

import java.util.ArrayList;
import java.util.List;

import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.ETag;
import de.dixieflatline.mpcw.client.Filter;
import de.dixieflatline.mpcw.client.IBrowser;
import de.dixieflatline.mpcw.client.ISearchResult;
import de.dixieflatline.mpcw.client.ProtocolException;
import de.dixieflatline.mpcw.client.SearchResult;

public class Browser implements IBrowser
{
	private final Channel channel;
	
	public Browser(Channel channel)
	{
		this.channel = channel;
	}

	@Override
	public ISearchResult find(ETag tag) throws CommunicationException, ProtocolException
	{
		IResponse response = channel.send("list " + mapTag(tag));
	
		return toSearchResult(tag, response);
	}

	@Override
	public ISearchResult find(ETag tag, Filter[] filter) throws CommunicationException, ProtocolException
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("list ");
		builder.append(mapTag(tag));
		
		for(Filter f : filter)
		{
			String value = f.getValue()
			                .replaceAll("\"", "\\\\\"");

			builder.append(" ");
			builder.append(mapTag(f.getTag()));
			builder.append(" \"");
			builder.append(value);
			builder.append("\"");
		}
		
		System.out.println(builder.toString());
		
		IResponse response = channel.send(builder.toString());
		
		return toSearchResult(tag,  filter, response);
	}
	
	private static String mapTag(ETag tag)
	{
		String tagName;
		
		switch(tag)
		{
			case Artist:
				tagName = "Artist";
				break;
			
			case Album:
				tagName = "Album";
				break;
			
			case AlbumArtist:
				tagName = "AlbumArtist";
				break;
			
			case Title:
				tagName = "Title";
				break;
			
			default:
				tagName = null;
		}
		
		return tagName;
	}
	
	private static SearchResult toSearchResult(ETag tag, IResponse response)
	{
		return toSearchResult(tag, new Filter[0], response);
	}
	
	private static SearchResult toSearchResult(ETag tag, Filter[] filter, IResponse response)
	{
		String tagName = mapTag(tag);
		String linePrefix = String.format("%s: ", tagName);
		List<String> items = new ArrayList<String>();
		
		for(String line : response)
		{
			if(line.startsWith(linePrefix))
			{
				items.add(line.substring(linePrefix.length()));
			}
		}
		
		return new SearchResult(tag, new Filter[0], items);
	}
}