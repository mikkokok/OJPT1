package ojpt1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Mikko Kokkonen & Ville Vahtera
 *
 */
public class Summauspalvelija extends Thread {

	private int portti; // Summauspalvelijan portti
	private int lukujenmaara = 0; // Summauspalvelijalle l‰hetettyjen lukujen m‰‰r‰
	private int kokonaissumma = 0; // Summauspalvelijalle l‰hetettyjen lukujen summa
	private int palvelijanumero = 0; // Palvelijan j‰rjestysnumero
	private int luettu; // Soketista luettu luku
	private ServerSocket welcomeSocket = null; 
	private Socket connectionSocket = null;
	private ObjectInputStream objectIn = null;
	private boolean running = true;

	/* Summauspalvelijan konstuktori, parametreina portti jota summauspalvelija
	 * kuuntelee sek‰ palvelijan j‰rjestysnumero
	 */
	public Summauspalvelija(int portti, int palvelijanumero) throws IOException {
		this.portti = portti;
		this.palvelijanumero = palvelijanumero;
		Thread tredi = this;
		tredi.start();
	}
	public void run() {
		try {
			welcomeSocket = new ServerSocket(this.portti);
			connectionSocket = welcomeSocket.accept();
			connectionSocket.setSoTimeout(60000); // porttiin minuutin timeout
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Luodaan virrat joiden sis‰ll‰ sovellus kommunikoi palvelimen kanssa
		try {
			InputStream inStream = connectionSocket.getInputStream();
			objectIn = new ObjectInputStream(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		GUI.updateTextArea("-------Aloitetaan lukemaan portista: "+this.portti);
		while(running)
		{
			// Yritet‰‰n lukea soketista lukuja jos yhteys on auki
			try {
				if (connectionSocket.isConnected()) {
					luettu = objectIn.readInt();
					// Suljetaan soketti jos sit‰ pyydet‰‰n
					if (luettu == 0) {
						welcomeSocket.close();
					}
					// Muussa tapauksessa lis‰t‰‰n vastaanotettu luku summaan 
					// sek‰ lis‰t‰‰n m‰‰r‰‰ yhdell‰
					else {
						kokonaissumma = kokonaissumma + luettu;
						lukujenmaara++;	
					}
				} else {
					GUI.updateTextArea(this.portti+" on suljettu");
				}
			} catch (EOFException e) {
				break;
			} // catch
			catch(IOException e){
				GUI.updateTextArea("--------TryCatch blokissa");
				e.printStackTrace();
				running = false;
			}
		} // while
		try {
			welcomeSocket.close();
		} catch (IOException e) {
			GUI.updateTextArea("Portin "+this.portti+" sulkeminen ep‰nnistui. "+e);
		}
	} // run
	/**
	 * @return the lukujenmaara
	 * Palauttaa summauspalvelimelle l‰hetettyjen lukujen m‰‰r‰n
	 */
	public int getLukujenmaara() {
		return lukujenmaara;
	}
	/**
	 * @return the kokonaissumma
	 * Palauttaa summauspalvelijan vastaanottamien lukujen summan
	 */
	public int getKokonaissumma() {
		return kokonaissumma;
	}
	/**
	 * @return the palvelijanumero
	 * Metodi palauttaa summauspalvelijan j‰rjestysnumeron
	 */
	public int getPalvelijanumero() {
		return palvelijanumero;
	}
	/**
	 * @param running the running to set
	 * Muuttaa summauspalvelijan tilaa (k‰ynniss‰ - ei k‰ynniss‰)
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}