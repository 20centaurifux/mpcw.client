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

public class PlaylistItem
{
	private final String artist;
	private final String title;
	private final int hashCode;
	
	public PlaylistItem(String artist, String title)
	{
		this.artist = artist;
		this.title = title;
		hashCode = calculateHashcode();
	}
	
	public PlaylistItem(PlaylistItem item)
	{
		this.artist = item.getArtist();
		this.title = item.getTitle();
		hashCode = calculateHashcode();
	}

	public String getTitle()
	{
		return title;
	}
	
	public String getArtist()
	{
		return artist;
	}

	int calculateHashcode()
	{
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		
		return result;
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		PlaylistItem other = (PlaylistItem) obj;

		if(artist == null)
		{
			if (other.artist != null)
			{
				return false;
			}
		}
		else if(!artist.equals(other.artist))
		{
			return false;
		}
		
		if(title == null)
		{
			if(other.title != null)
			{
				return false;
			}
		}
		else if(!title.equals(other.title))
		{
			return false;
		}

		return true;
	}
}