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
package de.dixieflatline.mpcw.client.mpd;

import de.dixieflatline.mpcw.client.ETag;

public final class TagMapper
{	
	public static String map(ETag tag)
	{
		String tagName;
		
		switch(tag)
		{
			case Artist:
				tagName = "Artist";
				break;
			
			case Album:
				tagName = "Album";
				break;
			
			case AlbumArtist:
				tagName = "AlbumArtist";
				break;
			
			case Title:
				tagName = "Title";
				break;
			
			default:
				tagName = null;
		}
		
		return tagName;
	}
}