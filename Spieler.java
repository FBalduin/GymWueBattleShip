public class Spieler {
	// Name des Spielers
	String name;
	// Array der eigenen Schiffe (max. 10)
	double[] schiffe = new double[10];
	
	public Spieler(String spielername)	{
		this.setName(spielername);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
