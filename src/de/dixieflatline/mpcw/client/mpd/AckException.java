package de.dixieflatline.mpcw.client.mpd;

public class AckException extends Exception
{
	private static final long serialVersionUID = -7251901791447424761L;
	private final IResponse response;
	
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