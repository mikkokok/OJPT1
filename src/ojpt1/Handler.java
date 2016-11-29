package ojpt1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Luokka joka sis‰lt‰‰ toiminnallisuuden 
//"Muodosta yhteys"-painikkeelle
public class Handler implements ActionListener {

	//M‰‰ritell‰‰n muuttujat palvelimen portille 
	//ja aikarajalle joka on 5 sekunttia
	private final int targetPort = 3126;
	private int timeOut = 5000;
	
	public Handler(){
	}
	//Metodi joka muodostaa yhteyden palvelimeen kun
	//painiketta "Muodosta yhteys"-painetaan
	public void actionPerformed(ActionEvent arg0) {

		//Luodaan Connection-olio palvelimen portin numeron ja aikarajan avulla
		Connection udpConnection = new Connection(targetPort, timeOut);

		//Luodaan yhteys
		try {
			udpConnection.Connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}