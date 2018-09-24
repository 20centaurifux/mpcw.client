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

public class Trace implements Iterable<Vector<Integer>>
{
	private final List<Vector<Integer>> items = new ArrayList<Vector<Integer>>();

	public void add(Vector<Integer> v)
	{
		items.add(v);
	}

	@Override
	public Iterator<Vector<Integer>> iterator()
	{
		return new Iterator<Vector<Integer>>()
		{
			private int offset = items.size() - 1;

			@Override
			public boolean hasNext()
			{
				return offset >= 0;
			}

			@Override
			public Vector<Integer> next()
			{
				return items.get(offset--);
			}
		};
	}
}