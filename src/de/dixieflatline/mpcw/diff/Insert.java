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

public class Insert<T> implements ITransformation
{
	private final int index;
	private final T item;

	public Insert(int index, T item)
	{
		this.index = index;
		this.item = item;
	}

	public T getItem()
	{
		return item;
	}
	
	@Override
	public int getIndex()
	{
		return index;
	}
}