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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Index<T>
{
	private final Map<Integer, T> idToObject;
	private final Map<T, Integer> objectToId;
	
	public Index()
	{
		idToObject = new HashMap<Integer, T>();
		objectToId = new HashMap<T, Integer>();
	}
	
	public int insert(T object)
	{
		int id;
		
		if(objectToId.containsKey(object))
		{
			id = objectToId.get(object);
		}
		else
		{
			id = objectToId.size() + 1;

			objectToId.put(object, id);
			idToObject.put(id, object);
		}
		
		return id;
	}
	
	
	public int[] insert(Collection<T> objects)
	{
		int[] ids = new int[objects.size()];
		int offset = 0;

		for(T object : objects)
		{
			ids[offset++] = insert(object);
		}

		return ids;
	}
	
	public int[] insert(T[] objects)
	{
		return insert(Arrays.asList(objects));
	}

	public T map(Integer id)
	{
		return idToObject.get(id);
	}
}