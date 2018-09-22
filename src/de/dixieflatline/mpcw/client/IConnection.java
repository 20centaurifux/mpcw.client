package de.dixieflatline.mpcw.client;

public interface IConnection
{	
	boolean isConnected();

	void connect() throws CommunicationException;
	
	void disconnect() throws CommunicationException;
	
	IClient getClient();
}