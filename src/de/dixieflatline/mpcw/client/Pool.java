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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Pool<T extends IURI>
{
	private static final int AUTOCLEAN_INTERVAL = 15;

	private final IFactory<T> factory;
	private final HashMap<URI, Queue<T>> pool = new HashMap<URI, Queue<T>>();
	private int autocleanCounter;
	
	public Pool(IFactory<T> factory)
	{
		this.factory = factory;
	}
	
	public synchronized T create(URI uri) throws Exception
	{
		autoclean();

		T obj = tryRecycle(uri);

		if(obj == null)
		{
			obj = factory.create(uri);
		}

		return obj;
	}

	public synchronized void dispose(T obj) throws Exception
	{
		if(factory.valid(obj))
		{
			URI uri = obj.getURI();

			if(!pool.containsKey(uri))
			{
				pool.put(uri, new LinkedList<T>());
			}

			factory.idle(obj);

			pool.get(uri).add(obj);
		}
	}

	private void autoclean()
	{
		++autocleanCounter;
		
		if(autocleanCounter == AUTOCLEAN_INTERVAL)
		{
			removeInvalidResources();

			autocleanCounter = 0;
		}
	}
	
	private void removeInvalidResources()
	{
		List<URI> obsoleteQueues = new ArrayList<URI>();

		for(URI uri : pool.keySet())
		{
			int validResourcesLeft = popInvalidResources(uri);
			
			if(validResourcesLeft == 0)
			{
				obsoleteQueues.add(uri);
			}
		}

		for(URI uri : obsoleteQueues)
		{
			pool.remove(uri);
		}
	}

	private int popInvalidResources(URI uri)
	{
		Queue<T> currentQueue = pool.get(uri);
		LinkedList<T> newQueue = new LinkedList<T>();

		while(!currentQueue.isEmpty())
		{
			T resource = currentQueue.remove();
			
			if(factory.valid(resource))
			{
				newQueue.addFirst(resource);
			}
		}

		pool.replace(uri, newQueue);
		
		return newQueue.size();
	}

	private T tryRecycle(URI uri) throws Exception
	{
		T obj = null;

		if(pool.containsKey(uri))
		{
			Queue<T> resources = pool.get(uri);

			while(!resources.isEmpty() && obj == null)
			{
				T resource = resources.remove();

				if(factory.valid(resource))
				{
					obj = factory.recycle(resource);	
				}
			}
		}

		return obj;
	}
}