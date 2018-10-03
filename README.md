# mpcw.client

mpcw.client is a [mpd](https://www.musicpd.org/) client written in Java. It implements only a
small subset of the protocol.

# API

## Connecting

The mpcw.client.Connection factory creates a connection by dispatching the
scheme from the URI of the specified connection string. This library only
supports mpd at the current stage.

```
import de.dixieflatline.mpcw.client.*;

IConnection connection = Connection.create("mpd://localhost:6600");

connection.connect();

// do stuff

connection.disconnect();
```

A password can be added as query parameter:

```
IConnection connection = Connection.create("mpd://localhost:6600?password=trustno1");
```

## Playback

Create a player instance to get the current playback status:

```
IClient client = connection.getClient();
IPlayer player = client.getPlayer();
Status status = player.getStatus();

System.out.println("Player state: " + status.getState());
System.out.println("Current artist: " + status.getArtist());
System.out.println("Current album: " + status.getTitle());
```

The player can also be used to change the state or go forwards/backwards:

```
if(status.getState() != EState.Play)
{			
	player.play();
}

if(status.hasNext())
{
	player.next();
}
```

## Playlist

Create a playlist object to receive and manipulate the current playlist:

```
IClient client = connection.getClient();
IPlaylist playlist = client.getCurrentPlaylist();

playlist.selectAndPlay(0, EnumSet.of(SelectAndPlayFlags.NoRangeCheck));
```

Synchronize the playlist to receive the latest changes. This generates a set
of insert and delete actions.

```
while(true)
{
	for(ITransformation transformation : playlist.synchronize())
	{
		if(transformation instanceof InsertPlaylistItem)
		{
			InsertPlaylistItem insert = (InsertPlaylistItem)transformation;
			PlaylistItem item = insert.getItem();
			
			System.out.format("insert item at position %d: %s - %s\n",
			                  insert.getOffset(),
			                  item.getArtist(),
			                  item.getTitle());
		}
		else if(transformation instanceof Delete)
		{
			Delete delete = (Delete)transformation;
			
			System.out.format("delete %d items, begin at position %d\n",
			                  delete.getLength(),
			                  delete.getOffset());
		}
	}
	
	Thread.sleep(1000);
}
```

## Querying

Create a browser to query the music database:

```
// find all artists:
ISearchResult<Tag> foundTags = browser.findTags(ETag.Artist);

for(Tag tag : foundTags.getItems())
{
	System.out.format("tag: %s, value: %s\n", tag.getTag(), tag.getValue());
}

// find albums filtered by artist:
foundTags = browser.findTags(ETag.Album,
                             new Filter[] { new Filter(ETag.AlbumArtist, "The Cure") });

for(Tag tag : foundTags.getItems())
{
	System.out.format("tag: %s, value: %s\n", tag.getTag(), tag.getValue());
}

// list songs from album:
ISearchResult<Song> foundSongs = browser.findSongs(new Filter[]
                                                   {
                                                   	new Filter(ETag.AlbumArtist, "The Cure"),
                                                   	new Filter(ETag.Album, "seventeen seconds")
                                                   });

for(Song song : foundSongs.getItems())
{
	System.out.format("album: %s, track: %d, title: %s\n",
	                  song.getAlbum(),
	                  song.getTrack(),
	                  song.getTitle());
}
```
