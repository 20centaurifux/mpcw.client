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

public class Delete implements ITransformation
{
	private final int offset;
	private final int length;
	
	public Delete(int offset, int length)
	{
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int getOffset()
	{
		return offset;
	}
	
	public int getLength()
	{
		return length;
	}
}