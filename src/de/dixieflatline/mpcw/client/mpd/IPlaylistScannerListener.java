package de.dixieflatline.mpcw.client.mpd;
import de.dixieflatline.mpcw.client.PlaylistItem;

public interface IPlaylistScannerListener
{
	void onPlaylistItemFound(PlaylistItem item);
}