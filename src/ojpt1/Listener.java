package ojpt1;

import java.io.IOException;
import java.net.ServerSocket;

//Kuuntelijaluokka
public class Listener extends Thread{

	private int port;
	//private DatagramSocket socket;
	private ServerSocket socket;
	
	public Listener(int port) throws IOException{
		
		GUI.updateTextArea("Kuuntelija luotu");
		//System.out.println("Listener olio luotu");
		this.port = port;
		//socket = new DatagramSocket(port);
		socket = new ServerSocket(port);
		socket.setSoTimeout(5000);
	}
	public void run() {
		GUI.updateTextArea("Kuuntelija käynnistetty");
		//System.out.println("Listener käynnistetty");
	}
	
}
