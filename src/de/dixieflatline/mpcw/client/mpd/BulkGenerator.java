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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BulkGenerator<T>
{
	private final int maxSize;
	private T[] bulk;
	private int offset;
	private final List<IBulkGeneratorListener<T>> listeners = new ArrayList<IBulkGeneratorListener<T>>();

	public BulkGenerator(int maxSize)
	{
		this.maxSize = maxSize;
	}
	
	public int getMaxSize()
	{
		return maxSize;
	}

	public void addListener(IBulkGeneratorListener<T> listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(IBulkGeneratorListener<T> listener)
	{
		listeners.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	public void add(T item)
	{
		if(bulk == null)
		{
			bulk = (T[])Array.newInstance(item.getClass(), maxSize);
		}
		
		bulk[offset++] = item;

		if(offset == maxSize)
		{
			listeners.forEach(l -> l.onBulkGenerated(bulk));
			
			offset = 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void flush()
	{
		if(offset > 0)
		{
			T[] lastBulk = (T[])Array.newInstance(bulk[0].getClass(), offset);

			System.arraycopy(bulk, 0, lastBulk, 0, offset);
			
			listeners.forEach(l -> l.onBulkGenerated(lastBulk));
		}
	}
}