package de.dixieflatline.mpcw.client;

public interface IPlayer
{
	Status getStatus() throws CommunicationException, AckException, ProtocolException;
	
	void play() throws CommunicationException, AckException;
	
	void pause() throws CommunicationException, AckException;
	
	void stop() throws CommunicationException, AckException;
	
	void next() throws CommunicationException, AckException;
	
	void previous() throws CommunicationException, AckException;
}