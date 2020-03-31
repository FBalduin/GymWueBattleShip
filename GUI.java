import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;


/**
 * Klasse GUI - Grapfisches User Interface
 * Erzeugt GUI und gibt entsprechende Eingaben/Aktionen
 * an Klasse Spiellogik weiter.
 * Erbt von der Klasse JFrame - so sind grafische Optionen 
 * von Vaterklasse hier verfügbar
 * Vaterklasse: vordefinierte Klasse von Java
 */
public class GUI extends JFrame implements ActionListener
{
	// Variable für Buttons für Spielfelder
    public JButton[][] meineFelder;
    // Variable für Textfelder zu Informationen/Abkürzungen
    public JLabel infos;
    // Variable für Textfelder - Log1&Log2
    public JLabel log1;
    public JLabel log2;
    // Variable für Anzahl der Gesamtfelder - 10&10 + Nummerierung
    int anzahlFelderX=11;
    int anzahlFelderY=11;
    // Variable für aktuelle Raterunde  
    public int aktuelleSpielrunde=0;
    // Variable für neues Fenster
    public JFrame frame;  
    
    // Standardkonstruktor zum initialisieren des Spielfeldes
    public GUI()	{
    	frame = new JFrame("GymWue BattleShip Ultra"); // Titel oben
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit-Button
        frame.setSize(325,650); // Größe des Fensters
        frame.setLayout(null); // kein vorgefertigtes Layout
               
        meineFelder  = new JButton[anzahlFelderX][anzahlFelderY]; // Neues Array an Felder initialisieren
        // Schleife zum Befüllen/Initialisieren der Felder
        for(int i=0;i<anzahlFelderX;i++)    {
           for(int j=0;j<anzahlFelderY;j++)  {
         	// Felder mit Zahlen zur Orientierung  
            if(j==0)	{
                meineFelder[i][j] = new JButton(""+i); // neues Button-Objekt

            }
         	// Felder mit Zahlen zur Orientierung  
            else if(i==0)	{
                meineFelder[i][j] = new JButton(""+j); // neues Button-Objekt
            }
            // Normales Spielfeld
            else {
            meineFelder[i][j] = new JButton(); // neues Button-Objekt
            }
            // gilt für alle Felder
            meineFelder[i][j].setBounds(i*30,j*30,30,30); //Position + Größe der Felder
            meineFelder[i][j].addActionListener(this); // Aktions-Horcher-s.U.
            meineFelder[i][j].setVisible(true); // ist sichtbar
            meineFelder[i][j].setContentAreaFilled(false); // Design
            meineFelder[i][j].setFocusPainted(false); // Design
            meineFelder[i][j].setBorder(new LineBorder(Color.BLACK)); // Rahmen

            frame.getContentPane().add(meineFelder[i][j]); // Füge Feld zum Fenster hinzu-Wichtig!
           } 
        }
        
        // Label für Spielinformationen
        log1 = new JLabel(); // Neues Objekt JLabel - Textfeld
        log1.setBounds(0,340,400,50); // setze Position und Größe
        log1.setText(""); // setze Text
        log1.setVisible(true); // Sichtbarkeit
        // Label für Spielinformationen
        log2 = new JLabel(); // Neues Objekt JLabel - Textfeld
        log2.setBounds(0,375,400,50); // setze Position und Größe
        log2.setText(""); // setze Text
        log2.setVisible(true); // Sichtbarkeit
           
        // Label für Spielinformationen
        infos = new JLabel(); // Neues Objekt JLabel - Textfeld
        infos.setBounds(0,375,240,300); // setze Position und Größe
        // Setze Text - hier kann man HTML einfügen, um Text zu designen - s. HTML-Editor
        infos.setText("<html><body><ul><li>S = eigenes Schiff</li></ul><p>Dein Angriff:</p><ul><li>D = daneben getroffen!</li><li>X-Schiff getroffen!</li></ul><p>Computer-Angriff:</p><ul><li>v = vorbeigeschossen</li><li># = getroffen</li></ul></ul></body></html>");
        infos.setVisible(true); // Sichtbarkeit

        frame.getContentPane().add(infos); // Füge JLabel Textfeld zum Fenster hinzu
        frame.getContentPane().add(log1); // Füge JLabel Textfeld zum Fenster hinzu
        frame.getContentPane().add(log2); // Füge JLabel Textfeld zum Fenster hinzu


        frame.setVisible(true); // Sichtbarkeit des Fensters
        
        // printLog1 - eigene Methode zum Textsetzen im JLabel
        this.printLog1("Spieler: Setze deine 10 Schiffe!"); // setze ersten Text des Logs
        this.printLog2("Computer hat schon Schiffe gesetzt"); // setze ersten Text des Logs
    } 
    // Setzt Erklärungstext von Log1 passend ins HTML
    public void printLog1(String text) {
    	log1.setText("<html><body><em>Console - Runde "+aktuelleSpielrunde+":</em><ul><li style=\"text-align: left;\"><em>"+text+"</em></li></body></html>");
    }
    // Setzt Erklärungstext von Log1 passend ins HTML
    public void printLog2(String text) {
    	log2.setText("<html><body><li><em>"+text+"</em></li></ul><span style=\\\"text-decoration: underline;\\\"><em>___________________________________________________</em></span></body></html>");
    }
    
    // wichtige Methode: ActionListener-horch auf "Aktionen"
    // Nach jeder Aktion (Klicken, Texteingabe, etc.) wird folgende Methode aufgerufen,
    // um auf Reaktion/Aktion des Nutzers passend zu reagieren.
    public void actionPerformed (ActionEvent ae){
    // hier werden die Methoden der Spiellogik passend zur Spielphase aufgerufen
    	 // Runde 0: Schiffe setzen/plazieren
	 	 if(Spiellogik.phase==1) {
	 		 // rufe passende Methode auf in Spiellogik
	 		 Spiellogik.setzRunde(ae);
	 	 }
	 	 
	 	 // Rate-Runden beginnen...
	 	 else if(Spiellogik.phase==2)	{
	 		// Spielzug-Methode (Spieler, Computer)
	 		// Übergebe Aktion an Spiellogik
	 		Spiellogik.rateRunde(ae);
	 		// Zähle Runden hoch - nach jeder Aktion ist eine Runde vorbei
	 		aktuelleSpielrunde++;
	 	 }
     }
    	     
}
           	 

         
     

