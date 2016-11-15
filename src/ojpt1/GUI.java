package ojpt1;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

//K‰yttˆliittym‰luokka
public class GUI {
	
	//Luodaan tarvittavat oliot ikkunalle, listalle
	//painikkeelle ja painikkeen toiminnallisuudelle
	private JFrame window;
	private JList<String> list;
	private JButton connectButton;
	private Handler handler;

	//Luokan konstruktori joka luo uuden ikkunan ohjelmalle
	public GUI(int width, int height, String header, Program program){
		
		//Alustetaan JFrame tyyppinen olio ikkunan muodostamista varten
		window = new JFrame(header);
		
		//Alustetaan lista ohjelman ilmoituksien vastaanottamista varten
		list = new JList<String>();
		
		//Alustetaan painike joka suorittaa ohjelman toiminnallisuudet
		connectButton = new JButton("Muodosta yhteys");
		
		//Alustetaan Handler tyyppinen olio painikkeen toiminnallisuutta
		//(yhteydenotto palvelimeen) varten
		handler = new Handler();
		
		//Asetetaan ikkunan koko ja m‰‰ritet‰‰n ettei 
		//k‰ytt‰j‰ pysty muokkaamaan ikkunan kokoa
		window.setSize(width, height);
		window.setResizable(false);
		
		//Varmistetaan ettei ikkuna j‰‰ taustalle pyˆrim‰‰n kun ohjelma suljetaan
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Asetetaan ikkunan n‰kyv‰ksi ja sijoitetaan se 
		//keskelle tietokoneen n‰yttˆ‰
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		
		//Sijoitetaan painike ja lista paikoilleen
		connectButton.setBounds(320, 50, 150, 80);
		list.setBounds(10, 50, 300, 350);
		
		//Lis‰t‰‰n yhteydenotto-toiminto painikkeelle
		connectButton.addActionListener(handler);
		
		//Sijoitetaan lista, painike ja 
		//sovellus ikkunan sis‰lle
		window.add(list);
		window.add(connectButton);
		window.add(program);
		
		//K‰ynnistet‰‰n sovellus
		program.start();
		
	}

}
