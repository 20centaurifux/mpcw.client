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

import java.util.EnumSet;

import de.dixieflatline.mpcw.diff.ITransformation;

public interface IPlaylist
{
	Iterable<ITransformation> synchronize() throws CommunicationException, ProtocolException;

	int selectedIndex();

	Iterable<PlaylistItem> getPlaylistItems();

	void appendSong(Song song) throws CommunicationException, ProtocolException;

	void appendSong(String filename) throws CommunicationException, ProtocolException;

	void appendSongs(Iterable<Song> songs) throws CommunicationException, ProtocolException;

	Iterable<ITransformation> selectAndPlay(int offset, EnumSet<SelectAndPlayFlags> flags) throws CommunicationException, ProtocolException;
}