package ojpt1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mikko
 *
 */
public class Summauspalvelija extends Thread {

	/**
	 * @param args
	 */
	private int portti;
	private int lukujenmaara = 0;
	private int kokonaissumma = 0;
	private int luettu;
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;
	private ObjectInputStream objectIn = null;
	private boolean verbose = true;

	public Summauspalvelija(int portti) throws IOException {
		this.portti = portti;
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
			OutputStream outStream = connectionSocket.getOutputStream();
			InputStream inStream = connectionSocket.getInputStream();

			ObjectOutputStream objectOut = new ObjectOutputStream(outStream);
			objectIn = new ObjectInputStream(inStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true)
		{
			if (verbose)
				System.out.println("-------Aloitetaan lukemaan portista: "+this.portti);
			try {
				luettu = objectIn.readInt();
				if (verbose) 
					System.out.println("-------Portti: "+this.portti+ " vastaanotti: " +luettu);
				if (luettu == 0)
					welcomeSocket.close();
				kokonaissumma = kokonaissumma + luettu;
				lukujenmaara++;

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			welcomeSocket.close();
		} catch (IOException e) {
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
}