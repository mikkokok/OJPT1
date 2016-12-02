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
import java.util.LinkedList;

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
		
		GUI.updateTextArea("Muodostetaan yhteys palvelimeen");

		//Luodaan udp-soketti koneen vapaaseen porttiin
		DatagramSocket udpSocket = new DatagramSocket();

		//Luodaan soketit TCP-yhteytt‰ varten
		ServerSocket receiverSocket = new ServerSocket(0);
		Socket clientSocket = new Socket();

		//Asetetaan aikaraja palvelinsoketille
		receiverSocket.setSoTimeout(timeOut);
		
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

		while(true){
			try{
				//Alustetaan asiakassokettti ottamalla vastaan palvelimen
				//l‰hett‰m‰ yhteyden muodostus pyyntˆ
				clientSocket = receiverSocket.accept();	
				
				GUI.updateTextArea("Yhteys muodostettu.");

				//Luodaan virrat joiden sis‰ll‰ sovellus kommunikoi palvelimen kanssa
				OutputStream outStream = clientSocket.getOutputStream();
				InputStream inStream = clientSocket.getInputStream();
				ObjectOutputStream objectOut = new ObjectOutputStream(outStream);
				ObjectInputStream objectIn = new ObjectInputStream(inStream);

				//Tallennetaan palvelimen l‰hett‰j‰ kokonaisluku
				int receivedNumber = objectIn.readInt();
				GUI.updateTextArea("Vastaanotettiin luku: "+receivedNumber);

				//Tehd‰‰n vaadittavat toimenpiteet kun kokonaisluku on saatu
				//Jos kokonaislukua ei saada niin l‰hetet‰‰n palvelimeen -1 ja suljetaan ohjelma
				if(receivedNumber > 2 || receivedNumber < 10){
					for (int i = 0; i < receivedNumber; i++) { // Tehd‰‰n pyydetty m‰‰r‰ portteja ja ilmoitetaan niiden numerot
						GUI.updateTextArea("Luodaan portti "+porttialoitus);
						palvelijat.add(new Summauspalvelija(porttialoitus, i+1));
						objectOut.writeInt(porttialoitus);
						objectOut.flush();
						porttialoitus++;
					}
				}
				else{
					objectOut.writeInt(-1);
					objectOut.flush();
					
					//Suljetaan soketit
					udpSocket.close();	
					receiverSocket.close();
					clientSocket.close();
					
					GUI.printClosingMessage("Sovellus vastaanotti luvattoman luvun: " + receivedNumber + "\nSuljetaan ohjelma...");
				} // else
				while (true) { // Luetaan viestej‰ portilta
					GUI.updateTextArea("Luetaan palvelimelta tulevia lukuja luotuihin portteihin...");
					int luettu = objectIn.readInt();

					if (luettu == 1) {
						GUI.updateTextArea("Palvelin l‰hetti kyselyn: " +  luettu + "\nVastataan kyselyyn l‰hett‰m‰ll‰ kokonaissumma: " + kokonaisSumma());
						objectOut.writeInt(kokonaisSumma());
						objectOut.flush();
					} else if (luettu == 2) {
						GUI.updateTextArea("Palvelin l‰hetti kyselyn: " +  luettu + "\nVastataan kyselyyn l‰hett‰m‰ll‰ suurin summa: " + missaSuurinSumma());
						objectOut.writeInt(missaSuurinSumma());
						objectOut.flush();
					} else if (luettu == 3) {
						GUI.updateTextArea("Palvelin l‰hetti kyselyn: " +  luettu + "\nVastataan kyselyyn l‰hett‰m‰ll‰ kokonaism‰‰r‰: " + kokonaisMaara());
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
						
						GUI.printClosingMessage("Kommunikointi loppui sill‰ palvelin kysely l‰hetti luvun: "+ luettu + " \nsuljetaan ohjelma...");
						
						
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
						GUI.printClosingMessage("Yhteytt‰ ei voitu muodostaa. Suljetaan ohjelma...");
					}
					GUI.updateTextArea("Yhteytt‰ ei voitu muodostaa.\nYritet‰‰n uudelleen l‰hett‰m‰ll‰ UDP-paketti uudestaan");
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
