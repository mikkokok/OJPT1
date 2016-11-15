package ojpt1;

import java.net.DatagramSocket;
import java.net.SocketException;

//Kuuntelijaluokka
public class Listener implements Runnable{

	private int port;
	private DatagramSocket socket;
	
	public Listener(int port) throws SocketException{
		this.port = port;
		socket = new DatagramSocket(port);
		socket.setSoTimeout(5000);
	}


	public void run() {
		
		
	}
	
}
