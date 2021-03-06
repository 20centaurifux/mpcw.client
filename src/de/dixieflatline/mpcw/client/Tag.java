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

public class Tag
{
	private final ETag tag;
	private String value;

	public Tag(ETag tag)
	{
		this.tag = tag;
	}
	
	public Tag(ETag tag, String value)
	{
		this.tag = tag;
		this.value = value;
	}
	
	public ETag getTag()
	{
		return tag;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}