package de.dixieflatline.mpcw.client;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Connection conn = new Connection("localhost", 6600);

		conn.connect();

		System.out.println(conn.getVersion().toString());
		
		conn.disconnect();
	}
}