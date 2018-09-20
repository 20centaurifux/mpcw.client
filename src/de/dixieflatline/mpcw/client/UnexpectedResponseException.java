package de.dixieflatline.mpcw.client;

public class UnexpectedResponseException extends Exception
{	
	private static final long serialVersionUID = 4787120159896572852L;

	public UnexpectedResponseException(String message)
	{
		super(message);
	}
}