package ojpt1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * @author Mikko Kokkonen
 *
 */
public class Summauspalvelija extends Thread {

	/**
	 * @param args
	 */
	private int portti;
	private int lukujenmaara = 0;
	private int kokonaissumma = 0;
	private int palvelijanumero = 0;
	private int luettu;
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;
	private ObjectInputStream objectIn = null;
	private boolean verbose = true;
	private boolean running = true;

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
			connectionSocket.setSoTimeout(60000); // porttin minuutin timeout

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Luodaan virrat joiden sis�ll� sovellus kommunikoi palvelimen kanssa

		try {
			InputStream inStream = connectionSocket.getInputStream();
			objectIn = new ObjectInputStream(inStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (verbose)
			GUI.updateTextArea("-------Aloitetaan lukemaan portista: "+this.portti);
			//System.out.println("-------Aloitetaan lukemaan portista: "+this.portti);
		while(running)
		{

			try {
				if (connectionSocket.isConnected()) {
					luettu = objectIn.readInt();
					if (verbose) 
						//Jos kutsutaan updateTextarea-metodia niin kyselyt saavat v��ri� vastauksia
						//ja system.out.prinln k�yttess� tulee java.io.EOFException
						//GUI.updateTextArea("-------Portti: "+this.portti+ " vastaanotti: " +luettu);
						System.out.println("-------Portti: "+this.portti+ " vastaanotti: " +luettu);
					if (luettu == 0) {
						welcomeSocket.close();
						GUI.printClosingMessage("Kommunikointi loppui sill� palvelin kysely l�hetti luvun: "+ luettu + " \nsuljetaan ohjelma...");
					}
					else {
						kokonaissumma = kokonaissumma + luettu;
						lukujenmaara++;	
					}
				} else {
					GUI.updateTextArea(this.portti+" on suljettu");
					//System.out.println(this.portti+" on suljettu");
				}
			} catch (IOException e) {
				if (verbose) {
					GUI.updateTextArea("--------TryCatch blokissa");
					//System.out.println("--------TryCatch blokissa");
					e.printStackTrace();
				}
				running = false;
				//break;
			} // catch
		} // while
		try {
			welcomeSocket.close();
		} catch (IOException e) {
			//GUI.updateTextArea("Portin "+this.portti+" sulkeminen ep�nnistui. "+e);
			System.out.println("Portin "+this.portti+" sulkeminen ep�nnistui. "+e);
		}
	} // setuptcplistener
	/**
	 * @return the lukujenmaara
	 */
	public int getLukujenmaara() {
		return lukujenmaara;
	}
	/**
	 * @return the kokonaissumma
	 */
	public int getKokonaissumma() {
		return kokonaissumma;
	}
	/**
	 * @param portti the portti to set
	 */
	public void setPortti(int portti) {
		this.portti = portti;
	}
	/**
	 * @return the portti
	 */
	public int getPortti() {
		return portti;
	}
	/**
	 * @return the palvelijanumero
	 */
	public int getPalvelijanumero() {
		return palvelijanumero;
	}
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
