package de.dixieflatline.mpcw.client;

public class PlaylistItem
{
	private String artist;
	private String title;
	
	public PlaylistItem(String artist, String title)
	{
		this.artist = artist;
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}
	
	public String getArtist()
	{
		return artist;
	}
}