package de.dixieflatline.mpcw.client;

public class DispatchException extends Exception
{
	private static final long serialVersionUID = 6918523498784957085L;

	public DispatchException(Exception cause)
	{
		super(cause);
	}
}