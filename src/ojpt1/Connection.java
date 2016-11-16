package ojpt1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

//Yhteydenottoluokka
public class Connection {
	
	private int targetPort;
	private String myPort;
	private byte[] data;
	
	public Connection(int targetPort){
		this.targetPort = targetPort;
		data = new byte[256];
		myPort = "1025";
		data = myPort.getBytes();
	}
	
	public void Connect() throws IOException{
		
			//Luodaan udp soketti koneen vapaaseen porttiin
			System.out.println("Connect()");
			DatagramSocket udpSocket = new DatagramSocket();

			//Luodaan muuttuja palvelimen kohdeosoitelle
			InetAddress targetAddress = InetAddress.getByName("localhost");
			
			//Luodaan paketti joka sis‰lt‰‰ asiakassovelluksen TCP-portin numeron 
			//ja jonka kohteena on palvelin
			DatagramPacket packet = new DatagramPacket(data, data.length, targetAddress, targetPort);
			
			//L‰hetet‰‰n paketti palvelimelle.
			//T‰ss‰ kohtaa ohjelma syˆtt‰‰ virheen: "Connection refused: connect"
			udpSocket.send(packet);
			
			//Suljetaan soketti
			udpSocket.close();
	
		
	}

}
