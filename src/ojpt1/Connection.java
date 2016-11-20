package ojpt1;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

//Yhteydenottoluokka
public class Connection {
	
	private int targetPort;
	private String myPort;
	private byte[] data;
	private int timeOut;
	private int counter;
	
	public Connection(int targetPort, int timeOut){
		this.targetPort = targetPort;
		this.timeOut = timeOut;
		data = new byte[256];
		counter = 0;
	}
	
	@SuppressWarnings("resource")
	public void Connect() throws IOException{
		
		//Luodaan udp soketti koneen vapaaseen porttiin
		System.out.println("Connect()");
		DatagramSocket udpSocket = new DatagramSocket();
			
		//Luodaan soketit TCP-yhteytt‰ varten
		ServerSocket receiverSocket = new ServerSocket(0);
		Socket clientSocket = new Socket();
		
		//Tallennetaan vapaa portti ja muunnetaan se sopivaan muotoon
		//UDP paketin l‰hett‰mist‰ varten
		int getPort = receiverSocket.getLocalPort();
		myPort = Integer.toString(getPort);
		data = myPort.getBytes();

		//Luodaan muuttuja palvelimen kohdeosoitelle
		InetAddress targetAddress = InetAddress.getByName("localhost");
			
		//Luodaan paketti joka sis‰lt‰‰ asiakassovelluksen TCP-portin numeron 
		//ja jonka kohteena on palvelin
		DatagramPacket packet = new DatagramPacket(data, data.length, targetAddress, targetPort);
			
		//L‰hetet‰‰n paketti palvelimelle.
		udpSocket.send(packet);
			
		//Alustetaan palvelinsoketti palvelimen porttiin ja 
		//asetetaan sille aikarajaksi 5 sekunttia
		//receiverSocket = new ServerSocket(data.length);
		receiverSocket.setSoTimeout(timeOut);

		while(true){
			
			try{

				//Alustetaan asiakassokettti ottamalla vastaan palvelimen
				//l‰hett‰m‰ yhteyden muodostus pyyntˆ
				clientSocket = receiverSocket.accept();	
					
				//Luodaan virrat joiden sis‰ll‰ sovellus kommunikoi palvelimen kanssa
				OutputStream outStream = clientSocket.getOutputStream();
				InputStream inStream = clientSocket.getInputStream();
						
				ObjectOutputStream objectOut = new ObjectOutputStream(outStream);
				ObjectInputStream objectIn = new ObjectInputStream(inStream);
					
				//Tallennetaan palvelimen l‰hett‰j‰ kokonaisluku
				int receivedNumber = objectIn.readInt();
				
				//Odotetaan 5 sekunttia palvelimelta tulevaa kokonaislukua
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//Tehd‰‰n vaadittavat toimenpiteet kun kokonaisluku on saatu
				//Jos kokonaislukua ei saada niin l‰hetet‰‰n palvelimeen -1 ja suljetaan ohjelma
				if(receivedNumber != 0){
						//objectOut.writeInt(val);
				}
				else{
					objectOut.writeInt(-1);
					objectOut.flush();
					System.exit(1);
				}
						
				//Suljetaan soketit
				udpSocket.close();	
				receiverSocket.close();
				clientSocket.close();
				
			}catch(SocketTimeoutException e){
				
				//Mik‰li yhteytt‰ palvelimeen ei pystyt‰ 5:n sekunnin p‰‰st‰ muodostamaan niin
				//l‰hetet‰‰n UDP-paketti uudestaan, resetoidaan aikaraja ja yritet‰‰n muodostaa yhteys uudelleen
				while(clientSocket.isConnected() == false){
					
					//Kun yhteytt‰ on yritetty muodostaa 5 kertaa niin suljetaan ohjelma
					if(counter == 5){
						System.out.println("Yhteytt‰ ei voitu muodostaa. Suljetaan ohjelma...");
						System.exit(1);
					}
					
					System.out.println("Yhteytt‰ ei voitu muodostaa. Yritet‰‰n uudelleen l‰hett‰m‰ll‰ UDP-paketti uudestaan");
					udpSocket.send(packet);
					
					counter++;
					
					receiverSocket.setSoTimeout(timeOut);
					
					break;
					

				}
				
			}
				
		}
	}
}
