package de.dixieflatline.mpcw.client;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		IConnection conn = Connection.create("mpd://localhost");

		conn.connect();

		IClient client = conn.getClient();
		IPlayer player = client.getPlayer();
		Status status = player.getStatus();

		System.out.println(status.getState());
		System.out.println(status.hasPrevious());
		System.out.println(status.hasNext());
		System.out.println(status.getArtist());
		System.out.println(status.getTitle());
		
		player.stop();
		
		conn.disconnect();
	}
}