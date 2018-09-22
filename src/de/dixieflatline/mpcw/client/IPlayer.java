package de.dixieflatline.mpcw.client;

public interface IPlayer
{
	Status getStatus() throws CommunicationException, ProtocolException;
	
	void play() throws CommunicationException, ProtocolException;
	
	void pause() throws CommunicationException, ProtocolException;
	
	void stop() throws CommunicationException, ProtocolException;
	
	void next() throws CommunicationException, ProtocolException;
	
	void previous() throws CommunicationException, ProtocolException;
}