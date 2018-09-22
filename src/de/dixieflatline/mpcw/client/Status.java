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

public class Status
{
	private EState state;
	private boolean hasNext;
	private boolean hasPrevious;
	private String artist;
	private String title;
	
	public EState getState()
	{
		return state;
	}
	
	public void setState(EState state)
	{
		this.state = state;
	}
	
	public boolean hasNext()
	{
		return hasNext;
	}
	
	public void setHasNext(boolean hasNext)
	{
		this.hasNext = hasNext;
	}
	
	public boolean hasPrevious()
	{
		return hasPrevious;
	}
	
	public void setHasPrevious(boolean hasPrevious)
	{
		this.hasPrevious = hasPrevious;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
}