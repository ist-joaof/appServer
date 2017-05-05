package pt.ulisboa.tecnico.cmov.appServer;

public class Coordenates {
	
	private boolean latitude;
	private int degrees, minutes;
	private float seconds;
	private String direction;
	
	public Coordenates(boolean latLong, int degrees, int minutes, float seconds, String direction){
		
		this.latitude = latLong;;
		this.degrees = degrees;
		this.minutes = minutes;
		this.seconds = seconds;
		this.direction = direction;
	}

	public boolean getLatitude() {
		return latitude;
	}

	public int getDegrees(){
		return degrees;
	}
	
	public int getMinutes(){
		return minutes;
	}
	
	public float getSeconds(){
		return seconds;
	}
	
	public String getDirection(){
		return direction;
	}
	
	
	
	
}
