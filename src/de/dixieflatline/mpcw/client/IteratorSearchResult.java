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

public class IteratorSearchResult<T> implements ISearchResult<T>
{
	private final Filter[] filter;
	private final Iterator<T> iterator;
	
	public IteratorSearchResult(Iterator<T> iterator)
	{
		this.iterator = iterator;
		this.filter = new Filter[0];
	}
	
	public IteratorSearchResult(Iterator<T> iterator, Filter[] filter)
	{
		this.iterator = iterator;
		this.filter = filter;
	}

	@Override
	public Filter[] getFilter()
	{
		return filter;
	}

	@Override
	public Iterable<T> getItems()
	{
		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return iterator;
			}
		};
	}
}