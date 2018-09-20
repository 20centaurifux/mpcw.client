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