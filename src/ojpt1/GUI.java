package ojpt1;

import java.awt.TextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//K�ytt�liittym�luokka
public class GUI {

	//Luodaan tarvittavat oliot ikkunalle, listalle
	//painikkeelle ja painikkeen toiminnallisuudelle
	private JFrame window;
	private static TextArea textarea;
	private JButton connectButton;
	private Handler handler;

	//Luokan konstruktori joka luo uuden ikkunan ohjelmalle
	public GUI(int width, int height, String header, Program program){

		//Alustetaan JFrame tyyppinen olio ikkunan muodostamista varten
		window = new JFrame(header);

		//Alustetaan lista ohjelman ilmoituksien vastaanottamista varten
		//list = new JList<String>();
		textarea = new TextArea();

		//Alustetaan painike joka suorittaa ohjelman toiminnallisuudet
		connectButton = new JButton("Muodosta yhteys");

		//Alustetaan Handler tyyppinen olio painikkeen toiminnallisuutta
		//(yhteydenotto palvelimeen) varten
		handler = new Handler();

		//Asetetaan ikkunan koko ja m��ritet��n ettei 
		//k�ytt�j� pysty muokkaamaan ikkunan kokoa
		window.setSize(width, height);
		window.setResizable(false);

		//Varmistetaan ettei ikkuna j�� taustalle py�rim��n kun ohjelma suljetaan
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Asetetaan ikkunan n�kyv�ksi ja sijoitetaan se 
		//keskelle tietokoneen n�ytt��
		window.setVisible(true);
		window.setLocationRelativeTo(null);

		//Sijoitetaan painike ja lista paikoilleen
		connectButton.setBounds(320, 50, 150, 80);
		textarea.setBounds(10, 50, 300, 350);
		
		textarea.setEditable(false);
		
		//Lis�t��n yhteydenotto-toiminto painikkeelle
		connectButton.addActionListener(handler);

		//Sijoitetaan lista, painike ja 
		//sovellus ikkunan sis�lle
		window.add(textarea);
		window.add(connectButton);
		window.add(program);

		//K�ynnistet��n sovellus
		program.start();
	}
	
	//Metodi joka p�ivitt�� tekstilaatikkoa
	public static void updateTextArea(String text){
		textarea.append(text + "\n");
	}
	
	//Metodi joka sulkee ohjelman ja tulostaa viestin siit�
	public static void printClosingMessage(String message){

		JOptionPane.showMessageDialog(new JFrame(), message);
		System.exit(1);
		
	}
}
