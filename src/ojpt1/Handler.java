package ojpt1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Luokka joka sis‰lt‰‰ toiminnallisuuden 
//"Muodosta yhteys"-painikkeelle
public class Handler implements ActionListener {

	//Muuttuja joka sis‰lt‰‰ palvelimen portin numeron
	private final int targetPort = 3126;	
	
	public Handler(){
		
	}

	//Metodi joka muodostaa yhteyden palvelimeen kun
	//painiketta "Muodosta yhteys"-painetaan
	public void actionPerformed(ActionEvent arg0) {
		
		//Luodaan Connection-olio palvelimen portin numeron avulla
		Connection udpConnection = new Connection(targetPort);
		
		//Luodaan yhteys
		try {
			udpConnection.Connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
