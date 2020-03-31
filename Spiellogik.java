import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.Random;

public class Spiellogik {
	  	   
	   // Variablen für Spielphase
	   // 1=Schiffe verteilen
	   // 2=Schiffe angreifen
	   public static int phase = 0;
	   
	   // lege Klassenvariable für Spieler an
	   static Spieler spieler;
	   static Spieler computer;
	   // Lege Klassenvariable für GUI an
	   static GUI myGui;
	   
	   //Startmethode
	   public static void main (String args[]) {
		  // Erstelle Grafisches User Interface (GUI)
		  // Erzeuge neues Objekt aus der Klasse GUI
		  // Erzeuge mit Standardkonstruktor()-Gui wird hier initialisiert.
		  myGui = new GUI();
		   	      
	      // lege Spieler an
		  // Erzeuge zwei Spieler-Objekte aus der Klasse Spieler
		  // Konstruktor(String name) erzeugt direkt den Namen
	      spieler = new Spieler("Rolf");
		  computer = new Spieler("DummeKI");

	      // Starte in Phase 1
		  // Phase 1 ist zum Schiffe verteilen da
		  phase = 1;
		  
		  // Computer generiert direkt zu Anfang zufällig Schiffe auf dem Feld
		  // führe dazu die eigene Methode auf:
		  generateRandomSchiffe(); 
		  
		  // weitere Logik reagiert auf Benutzereingabe-Klicken
	   }
	   

	   /**
	    * Methode soll dem Spieler die Möglichkeit geben
	    * Schiffe auf dem Feld zu platzieren.
	    * Methode wird bei jedem Klick am Anfang aufgerufen.
	    * Jeder Klick auf ein Feld=Schiff wird platziert.
	    * @param: Klick-Event - Informationen, worauf geklickt wurde
	    * @return: void
	    */
	   public static void setzRunde(ActionEvent ae)	{
		   // Feld
		   int actButtonX = ((JButton)ae.getSource()).getX()/30;
		   int actButtonY = ((JButton)ae.getSource()).getY()/30;
		   
           // Füge Feld hinzu
		   // mache aus x=3, y=4 --> 3.4
		   double positionSchiff=castInttoDouble(actButtonX,actButtonY);
		   if(kannSpielerNochSchiffeverteilen(spieler)>0)	{
			   addInArray(positionSchiff,spieler);
			   // markiere in das entsprechende Feld grafisch
			   myGui.meineFelder[actButtonX][actButtonY].setText("S");
			   myGui.printLog1("Spieler: Setze deine "+kannSpielerNochSchiffeverteilen(spieler)+" Schiffe!");
		   }
		   else	{
			   phase=2;
			   myGui.printLog1("Alle Schiffe sind verteilt"); 
			   myGui.printLog2("...Rate-Runde beginnt, klick auf ein Feld..."); 
		   }
	   }
	   
	   /**
	    * Methode soll das Raten/Schiessen eines Schiffes für beides Spieler 
	    * darstellen. Computer & Spieler darf pro Methodenaufruf ein Feld beschiessen.
	    * Methode wird nach jeder Aktion in der GUI-Klasse aufgerufen. 
	    * @param: Klick-Event - Informationen, worauf geklickt wurde
	    * @return: void
	    */
	   public static void rateRunde(ActionEvent ae)	{
		   
		   // 1) Spielzug des Spielers
		   	 // filtere geklicktes Feld heraus --> z.B. x=5/y=4
		   	 int actButtonX = ((JButton)ae.getSource()).getX()/30;
		   	 int actButtonY = ((JButton)ae.getSource()).getY()/30;
		   	 
		   	 //überprüfe, ob Feld im Schiffs-Array des Gegenspielers ist
		   	 if(isInArray(castInttoDouble(actButtonX,actButtonY), computer)==true)	{ // wenn ja

	 			 	myGui.meineFelder[actButtonX][actButtonY].setText("X");
	 			 	// gebe Rückmeldung ins Log
	 			 	myGui.printLog1("Hurra! Du hast getroffen!");
	 		 }
	 		 else	{ // wenn nein
	 			 	// gebe Rückmeldung ins Log
	 			 	myGui.printLog1("Du hast daneben geschossen!");
	 			 	myGui.meineFelder[actButtonX][actButtonY].setText("D");
	 		 }	 
	 		 // Ende Spielzug von spieler
	 		 
	 	  // 2) Spielzug des Computers
	 		  // wählt zufällig eine Feld aus
			  Random wuerfel = new Random();	// neues Random-Objekt
			  int schiffX = 1 + wuerfel.nextInt(10);	   // Random x
			  int schiffY = 1 + wuerfel.nextInt(9);		   // Random y
			  
			  //überprüfe, ob Feld im Schiffs-Array des Gegenspielers ist
			  if(isInArray(castInttoDouble(schiffX,schiffY),spieler)==true)	{
				  myGui.printLog2("Computer hat eines deiner Schiffe versenkt!");
				  myGui.meineFelder[schiffX][schiffY].setText("#");
			  }
			  else	{
				  myGui.printLog2("Computer hat daneben getroffen!");
			  }
		 	  // Ende Spielzug von computer
			  
			  // Pruefe zum Schluss jeder Runde, ob ein Spieler verloren hat...
			  // rufe dazu passende Methode auf
			  if(checkVerlierer()!=null)	{
				  // Verlierer
				  String name = checkVerlierer().getName();
				  JOptionPane.showMessageDialog(null, "Das Spiel ist aus - verloren hat " + name); 
			  }
	   }
	   
	   /**
	    * Methode gibt den Verlierer aus, falls es einen gibt.
	    * Rückgaben:s.U.
	    * checkVerlierer: null -->keiner hat verloren
	    * checkVerlierer: spieler -->spieler ist Verlierer
	    * checkVerlierer: computer --> computer ist Verlierer
	    * @return: SPIELER: null, spieler oder computer --> Spieler-Objekt
	    */
	   private static Spieler checkVerlierer()	{
		   // Schaue nach, ob im Schiffe-Objekt des Spielers noch 
		   // mindestens ein Feld ist, welches nicht den Wert -1.0 hat
		   // dann kann derjenige Spieler ja nicht verloren haben.
		    
		   int checkVerlierer = 1;	// Annahme spieler hat verloren
		   // Spieler spieler
		   for(int i=0;i<spieler.schiffe.length-1;i++)	{	// durchlaufe Array
			   if(spieler.schiffe[i]!=-1.0)	{	// prüfe, ob ein Feld ungleich -1.0
				   checkVerlierer=0; // wenn ja setze Prüfvariable auf 0
			   }
		   }
		   if(checkVerlierer == 1)	{	// Wenn Prüfvariable immer noch 1
			   return spieler;	// gebe ihn direkt als Verlierer zurück
		   }
		   else	{	// wenn spieler nicht verloren hat - prüfe, ob computer verloren hat
			   checkVerlierer = 2; 	// Annahme computer hat verloren
			   for(int i=0;i<computer.schiffe.length-1;i++)	{ // durchlaufe Array
				   if(computer.schiffe[i]!=-1.0)	{ // prüfe, ob ein Feld ungleich -1.0
					   checkVerlierer=0; // wenn ja setze Prüfvariable auf 0
				   }
			   }
		   }
		   if(checkVerlierer==2)	{ // Wenn Prüfvariable immer noch 1
			   return computer;	// gebe ihn  als Verlierer zurück
		   }
		   return null;	// wenn nicht - dann hat noch keiner verloren/gebe null zurück
	   }

	   /**
	    * Prüft, ob Wert im Array von einem Spieler ist 
	    * @param: double rateSchiffPosition --> z.B. 2.1
	    * @param: Spieler gegner --> computer/spieler
	    * @return: true-->ja Wert ist im Array / false --> nein Wert ist nicht im Array
	    */
	   private static boolean isInArray(double rateSchiffPosition, Spieler gegner)	{
		   // Durchlaufe Array mit Schleife
		   for(int i=0;i<gegner.schiffe.length-1;i++)	{
			   if(rateSchiffPosition==gegner.schiffe[i])	{	// Wenn wert = rateSchiffposition
				  gegner.schiffe[i]=-1.0;	// Ändere Wert, da Schiff getroffen wurde
				  return true; // gebe true zurück
			   }
		   }
		   return false;	// ansonsten false
	   }
	   	   
	   private static void addInArray(double positionSchiff, Spieler name)
	   {
			// Füge Schiff dem Array hinzu
			   for(int i=0;i<=name.schiffe.length;i++)	{
				   if(name.schiffe[i]==0.0)	{
					   name.schiffe[i]=positionSchiff;
					   break;
				   }
			   }
	   }  
	   
	   /**
	    * Prüft, ob spieler noch Schiffe verteilen darf - maximal Arraygröße
	    * Wenn noch ein Platz im Array frei ist, erkennt man das an Wert 0.0
	    * @param: Spieler name --> Spieler-Objekt
	    * @return: int --> Anzahl freier Plätze
	    */
	   private static int kannSpielerNochSchiffeverteilen(Spieler name)	{
		   int countFreiePlaetze=0;	// zähle freie Plätze
		   for(int i=0;i<name.schiffe.length;i++)	{	// durchlaufe Array
			   if(name.schiffe[i]==0.0)	{	// wenn ein Wert des Arrays noch 0.0 ist...
				   countFreiePlaetze++;	// zaehle hoch
			   }
		   }
		   return countFreiePlaetze;	// gebe Anzahl freiePlätze zurück
	   }
	   
	   /**
	    * wandelt zwei int in eine double um
	    * z.B. x=4, y=2 --> 4.2 
	    * wichtig für zur Speicherung ins Array
	    * @param: int x / int y
	    * @return: double --> zusammengesetze Zahl
	    */
	   private static double castInttoDouble(int x, int y) {
		// 1 wandel Integer (z.B. 2.5) in double um(z.B. 2.5)
		   double positionSchiff = x;
		   // Hilfsvariable
		   double Y=y;
			   positionSchiff = positionSchiff +(Y/10);
			   // Runde Zahl auf eine Stelle nach dem Komma
			   positionSchiff = (Math.round(100.0 * positionSchiff) / 100.0);
		  return positionSchiff;	// Gebe Double zurück
		   
	   }
	   
	   /**
	    * positioniert zufällig Schiffe für Spieler computer
	    * erzeugt dazu zufallszahlen und speichert sie im Array computer.schiffe

	    */
	   private static void generateRandomSchiffe()	{
		   Random wuerfel = new Random();	// neues Random-Objekt
		   
		// 1) 4er Schiff
		   int schiffX = 1 + wuerfel.nextInt(7);	// Zufallszahl zwischen 1-7	   
		   int schiffY = 1 + wuerfel.nextInt(7);	// Zufallszahl zwischen 1-7	      
		   double schiffPos=castInttoDouble(schiffX,schiffY); // wandle Zahl in double um
		   addInArray(schiffPos,computer); // speichere im Array
		   addInArray(castInttoDouble(schiffX,schiffY+1),computer); // erhöhe y+1
		   addInArray(castInttoDouble(schiffX,schiffY+2),computer); // erhöhe y+1
		   addInArray(castInttoDouble(schiffX,schiffY+3),computer); // erhöhe y+1
		   
		// 1) 3er Schiff --> Kommentare s.O.
		   schiffX = 1 + wuerfel.nextInt(8);	   
		   schiffY = 1 + wuerfel.nextInt(8);	   
		   schiffPos=castInttoDouble(schiffX,schiffY);
		   addInArray(schiffPos,computer);
		   addInArray(castInttoDouble(schiffX,schiffY+1),computer);
		   addInArray(castInttoDouble(schiffX,schiffY+2),computer);
		   
		// 1) 2er Schiff
		   schiffX = 1 + wuerfel.nextInt(9);	   
		   schiffY = 1 + wuerfel.nextInt(9);	   
		   schiffPos=castInttoDouble(schiffX,schiffY);
		   addInArray(schiffPos,computer);
		   addInArray(castInttoDouble(schiffX,schiffY+1),computer);
		   
		// 1) 1er Schiff
		   schiffX = 1 + wuerfel.nextInt(10);	   
		   schiffY = 1 + wuerfel.nextInt(10);	   
		   schiffPos=castInttoDouble(schiffX,schiffY);
		   addInArray(schiffPos,computer);
	   };
	   
}
