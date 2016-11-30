package ojpt1;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

//Ohjelman p��luokka
public class Program extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	//Muuttujat sovelluksen ikkunan koolle
	public final int WIDTH = 500;
	public final int HEIGHT = 500;

	//Muuttujat sovelluksen k�ytt�liittym�n toimintaa varten
	private Thread mainThread;
	private boolean running = false;

	//Luokan konstruktori, joka implementoi sovelluksen ikkunan
	public Program(){
		new GUI(WIDTH, HEIGHT, "Harjoitusty� 1", this);

	}
	//Metodi, joka k�ynnist�� sovelluksen
	public synchronized void start(){
		running = true;
		mainThread = new Thread(this);
		mainThread.start();
	}
	//Metodi, joka lopettaa sovelluksen toiminnan
	public synchronized void stop(){
		try{
			running = false;
			mainThread.join();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//run-metodi, joka sovelluksen toiminnan jatkuvuudesta aina 
	//siihen saakka kunnes k�ytt�j� itse sulkee sovelluksen tai jos 
	//jokin muu teht�v�nannon sulkeutumis kriteeri t�yttyy
	@SuppressWarnings("static-access")
	public void run() {
		//Silmukka joka on k�ynniss� kun sovellus on k�ynniss�
		//periaatteessa sovelluksen syd�n
		while(running){
			try {
				mainThread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Kun sovellus ei ole k�ynniss� niin kutsutaan stop-metodia
		//joka sulkee sovelluksen
		stop();	
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new Program();
		Thread thread1 = new WorkDistributor();
		//Thread thread2 = new Listener(1025);
		thread1.start();
		//thread2.start();	
	}
}
