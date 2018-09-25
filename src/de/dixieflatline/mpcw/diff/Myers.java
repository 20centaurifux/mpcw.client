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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Myers
{
	private final int[] a;
	private final int[] b;
	
	public Myers(int[] a, int[] b)
	{
		this.a = a;
		this.b = b;
	}
	
	public Trace shortestEdit()
	{
		final int n = a.length;
		final int m = b.length;
		final int max = n + m;
		final Vector<Integer> v = new Vector<Integer>(-max, max + 1);
		final Trace trace = new Trace();

		v.set(1, 0);

		for(int d = 0; d <= max + 1; ++d)
		{
			for(int k = -d; k <= d; k += 2)
			{
				int x, y;
				
				if(moveRight(k, d, v.get(k - 1), v.get(k + 1)))
				{
					x = v.get(k + 1);
				}
				else
				{
					x = v.get(k - 1) + 1;
				}

				y = x - k;

				while(x < n && y < m && a[x] == b[y])
				{
					++x;
					++y;
				}

				v.set(k, x);
				
				if(x >= n && y >= m)
				{
					return trace;
				}
			}

			trace.add(new Vector<Integer>(v));
		}

		return null;
	}
	
	public Edge[] backtrack(Trace trace)
	{
		int d = 0;
		int x = a.length;
		int y = b.length;
		final List<Edge> edges = new ArrayList<Edge>();

		for (Iterator<Vector<Integer>> iterator = trace.iterator(); iterator.hasNext(); ++d)
		{
			Vector<Integer> v = iterator.next();
			int k = x - y;
			int prevK;
			
			if(moveRight(k, d, v.get(k - 1), v.get(k + 1)))
			{
				prevK = k + 1;
			}
			else
			{
				prevK = k - 1;
			}
			
			int prevX = v.get(prevK);
			int prevY = prevX - prevK;
			
			while(x > prevX && y > prevY)
			{
				edges.add(new Edge(new Vertex(x - 1, y - 1), new Vertex(x, y)));
				--x;
				--y;
			}
			
			if(d >= 0)
			{
				edges.add(new Edge(new Vertex(prevX, prevY), new Vertex(x, y)));
			}
			
			x = prevX;
			y = prevY;
		}
		
		Collections.reverse(edges);

		return edges.toArray(new Edge[0]);
	}
	
	public ITransformation[] editScript(Edge[] edges)
	{
		final List<ITransformation> script = new ArrayList<ITransformation>();
		int offset = edges[0].getFrom().getX();
		boolean beginDelete = false;
		int beginDeleteOffset = -1;
		
		for(Edge edge : edges)
		{
			if(edge.getTo().getY() == edge.getFrom().getY())
			{
				if(!beginDelete)
				{
					beginDelete = true;
					beginDeleteOffset = offset;
				}
			}
			else
			{
				if(beginDelete)
				{
					script.add(new Delete(beginDeleteOffset, offset - beginDeleteOffset));
					offset = beginDeleteOffset;
					beginDelete = false;
				}
				
				if(edge.getTo().getX() == edge.getFrom().getX())
				{
					script.add(new Insert<Integer>(offset, b[edge.getFrom().getY()]));
				}
			}

			++offset;
		}
		
		if(beginDelete)
		{
			script.add(new Delete(beginDeleteOffset, offset - beginDeleteOffset));
		}

		return script.toArray(new ITransformation[0]);
	}

	private static boolean moveRight(Integer k, Integer d, Integer top, Integer right)
	{
		boolean moveRight = false;
		
		if(k == -d || k != d)
		{
			if(top == null && right != null)
			{
				moveRight = true;
			}
			else if(top != null && right != null)
			{
				moveRight = top < right;
			}
		}

		return moveRight;
	}
}