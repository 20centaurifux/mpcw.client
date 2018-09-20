package de.dixieflatline.mpcw.client;

public class AckException extends Exception
{
	private static final long serialVersionUID = -7251901791447424761L;
	private IResponse response;
	
	public AckException(Ack response)
	{
		super();

		this.response = response;
	}
	
	public IResponse getResponse()
	{
		return response;
	}
}