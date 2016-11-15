package ojpt1;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

//Ohjelman pääluokka
public class Program extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	//Muuttujat sovelluksen ikkunan koolle
	public final int WIDTH = 500;
	public final int HEIGHT = 500;
	
	//Muuttujat sovelluksen käyttöliittymän toimintaa varten
	private Thread mainThread;
	private boolean running = false;
	
	//Luokan konstruktori, joka implementoi sovelluksen ikkunan
	public Program(){
		new GUI(WIDTH, HEIGHT, "Harjoitustyö 1", this);
		
	}
	
	//Metodi, joka käynnistää sovelluksen
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
	//siihen saakka kunnes käyttäjä itse sulkee sovelluksen tai jos 
	//jokin muu tehtävänannon sulkeutumis kriteeri täyttyy
	public void run() {
		
		 //Silmukka joka on käynnissä kun sovellus on käynnissä
		 //periaatteessa sovelluksen sydän
	     while(running){
	    	 
	    	 try {
	    		 mainThread.sleep(10);;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	     }
	     
	     //Kun sovellus ei ole käynnissä niin kutsutaan stop-metodia
	     //joka sulkee sovelluksen
	     stop();
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Program();

	}

}
