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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//Yhteydenottoluokka
public class Connection {

	private int targetPort;
	private String myPort;
	private byte[] data;
	private int timeOut;
	private int counter;
	private int porttialoitus = 1230; // tehd‰‰n portteja v‰lille 1230-1240
	public static LinkedList<Summauspalvelija> palvelijat = new LinkedList<Summauspalvelija>();
	public static LinkedList<Thread> tredit = new LinkedList<Thread>();
	
	public Connection(int targetPort, int timeOut){
		this.targetPort = targetPort;
		this.timeOut = timeOut;
		data = new byte[256];
		counter = 0;
		
	} // Constructor

	@SuppressWarnings("resource")
	public void Connect() throws IOException{
		// Tehd‰‰n mahdollisesti tarvittavat kymmenen tredi‰ valmiiksi
	/*	for (int i = 0; i < 10; i++) {
			tredit.add(new Thread());
		}
	*/
		//Luodaan udp soketti koneen vapaaseen porttiin
		System.out.println("Connection luokka aloitettu");
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
				System.out.println("Connection luokka vastaanotettu: "+receivedNumber);
				//Odotetaan 5 sekunttia palvelimelta tulevaa kokonaislukua
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//Tehd‰‰n vaadittavat toimenpiteet kun kokonaisluku on saatu
				//Jos kokonaislukua ei saada niin l‰hetet‰‰n palvelimeen -1 ja suljetaan ohjelma
				if(receivedNumber != 0){
					for (int i = 0; i < receivedNumber; i++) { // Tehd‰‰n pyydetty m‰‰r‰ portteja ja ilmoitetaan niiden numerot
					System.out.println("Luodaan portti "+porttialoitus);
					palvelijat.add(new Summauspalvelija(porttialoitus, i+1));
					objectOut.writeInt(porttialoitus);
					objectOut.flush();
					porttialoitus++;
					}
					//objectOut.writeInt(val);
				}
				else{
					objectOut.writeInt(-1);
					objectOut.flush();
					//Suljetaan soketit
					udpSocket.close();	
					receiverSocket.close();
					clientSocket.close();
					System.exit(1);
				} // else
				while (true) { // Luetaan viestej‰ portilta
					int luettu = objectIn.readInt();
					System.out.println("------------Connection luokka: "+luettu);
					if (luettu == 1) {
						objectOut.writeInt(kokonaisSumma());
						objectOut.flush();
					} else if (luettu == 2) {
						objectOut.writeInt(missaSuurinSumma());
						objectOut.flush();
					} else if (luettu == 3) {
						objectOut.writeInt(kokonaisMaara());
						objectOut.flush();
					} else if (luettu == 0) {
						for (int i = 0; i < palvelijat.size(); i++) {
							palvelijat.get(i).setRunning(false);
						}
						// Lopuksi suljetaan soketit
						udpSocket.close();	
						receiverSocket.close();
						clientSocket.close();
					} else {
						objectOut.writeInt(-1);
						objectOut.flush();
					}
				}

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
				} // while
			} // try-catch
		} // while
	} // Connect()
	public static int kokonaisSumma() { // mik‰ on t‰h‰n menness‰ v‰litettyjen lukujen kokonaissumma
		int summa = 0;
		for (int i = 0; i < palvelijat.size(); i++) {
			summa = summa + palvelijat.get(i).getKokonaissumma();
		}
		return summa;
	}
	public static int missaSuurinSumma() { // mille summauspalvelijalle v‰litettyjen lukujen summa on suurin
		int palvelinumero = palvelijat.get(0).getPalvelijanumero();
		int suurinsumma = palvelijat.get(0).getKokonaissumma();
		for (int i = 1; i < palvelijat.size(); i++) {
			if (suurinsumma < palvelijat.get(i).getKokonaissumma()) { // on lˆytynyt suurempi kokonaissumma
				suurinsumma = palvelijat.get(i).getKokonaissumma();
				palvelinumero = palvelijat.get(i).getPalvelijanumero();
			}
		}
		return palvelinumero;
	}
	public static int kokonaisMaara() { // mik‰ on t‰h‰n menness‰ kaikille summauspalvelimille v‰litettyjen lukujen kokonaism‰‰r‰
		int maara = 0;
		for (int i = 0; i < palvelijat.size(); i++) {
			maara = maara + palvelijat.get(i).getLukujenmaara();
		}
		return maara;
	}
} // class
