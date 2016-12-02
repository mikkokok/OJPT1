package ojpt1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


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
		
		GUI.updateTextArea("-------Aloitetaan lukemaan portista: "+this.portti);
		
		while(running)
		{

			try {
				if (connectionSocket.isConnected()) {

					luettu = objectIn.readInt();
					if (luettu == 0) {
						welcomeSocket.close();
					}
					else {
						kokonaissumma = kokonaissumma + luettu;
						lukujenmaara++;	
					}
				} else {
					GUI.updateTextArea(this.portti+" on suljettu");
				}
				
			} catch (EOFException e) {
				GUI.updateTextArea("Yhteys on suljettu");
				//System.out.println("Yhteys suljettu");
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
			GUI.updateTextArea("Portin "+this.portti+" sulkeminen ep�nnistui. "+e);
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
