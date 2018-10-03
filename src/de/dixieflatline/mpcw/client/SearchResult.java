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

import java.util.Collection;

public class SearchResult implements ISearchResult
{
	private final ETag tag;
	private final Filter[] filter;
	private final String[] items;
	
	public SearchResult(ETag tag, Filter[] filter, Collection<String> items)
	{
		this.tag = tag;
		this.filter = filter;
		this.items = items.toArray(new String[0]);
	}

	@Override
	public ETag getTag()
	{
		return tag;
	}
	
	@Override
	public Filter[] getFilter()
	{
		return filter;
	}

	@Override
	public String[] getItems()
	{
		return items;
	}

}