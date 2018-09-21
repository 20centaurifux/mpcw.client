package de.dixieflatline.mpcw.client;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Connection conn = new Connection("localhost", 6600);

		conn.connect();
		
		IClient client = conn.createClient();

		System.out.println(conn.getVersion().toString());
	
		IPlayer player = client.createPlayer();
		
		Status status = player.getStatus();

		System.out.println(status.getState());
		System.out.println(status.hasPrevious());
		System.out.println(status.hasNext());
		System.out.println(status.getArtist());
		System.out.println(status.getTitle());
		
		player.play();

		conn.disconnect();
	}
}