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
package de.dixieflatline.mpcw.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Vector<T> implements Iterable<T>
{
	private final List<T> items;
	private final int from;
	private final int to;
	
	public Vector(int from, int to)
	{
		if(from >= to)
		{
			throw new IllegalArgumentException();
		}

		this.from = from;
		this.to = to;
		
		int capacity = capacity();
		
		items = new ArrayList<T>(capacity);
		
		for(int i = 0; i < capacity(); ++i)
		{
			items.add(null);
		}
	}
	
	public Vector(Vector<T> vector)
	{
		from = vector.from;
		to = vector.to;
		items = new ArrayList<T>(vector.items);
	}

	public int capacity()
	{
		return to - from + 1;
	}

	public void set(int index, T item)
	{
		int offset = offsetFromIndex(index);
		
		items.set(offset, item);
	}

	public T get(int index)
	{
		int offset = offsetFromIndex(index);
		
		return items.get(offset);
	}
	
	private int offsetFromIndex(int index)
	{
		if(index < from || index > to - 1)
		{
			throw new IndexOutOfBoundsException();
		}

		int offset = index;
		
		if(offset < 0)
		{
			offset += capacity();
		}

		return offset;
	}

	@Override
	public Iterator<T> iterator()
	{
		return items.iterator();
	}
}