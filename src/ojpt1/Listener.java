package ojpt1;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

//Kuuntelijaluokka
public class Listener extends Thread{

	private int port;
	//private DatagramSocket socket;
	private ServerSocket socket;
	
	public Listener(int port) throws Exception{
		System.out.println("Listener olio luotu");
		this.port = port;
		//socket = new DatagramSocket(port);
		socket = new ServerSocket(port);
		socket.setSoTimeout(5000);
	}


	public void run() {
		System.out.println("Listener käynnistetty");
		
	}
	
}
