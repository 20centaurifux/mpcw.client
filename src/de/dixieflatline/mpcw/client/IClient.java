package de.dixieflatline.mpcw.client;

public interface IClient
{
	IPlayer getPlayer();
	
	IPlaylist getCurrentPlaylist() throws CommunicationException, ProtocolException;
}