package de.dixieflatline.mpcw.client;

public class CommunicationException extends Exception
{
	private static final long serialVersionUID = 8905520149177607239L;
	
	public CommunicationException(String message)
	{
		super(message);
	}
	
	public CommunicationException(Exception cause)
	{
		super(cause);
	}
}