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

public class InsertFrom implements ITransformation
{
	private final int offset;
	private final int from;

	public InsertFrom(int offset, int from)
	{
		this.offset = offset;
		this.from = from;
	}

	public int getFrom()
	{
		return from;
	}
	
	@Override
	public int getOffset()
	{
		return offset;
	}
}