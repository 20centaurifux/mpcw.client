package de.dixieflatline.mpcw.client;

public interface IPlaylist extends Iterable<PlaylistItem>
{
	void synchronize() throws CommunicationException, ProtocolException;
	
	int size();

	PlaylistItem get(int offset);

	int selectedIndex();
		
	void selectAndPlay(int offset) throws CommunicationException, ProtocolException;
}