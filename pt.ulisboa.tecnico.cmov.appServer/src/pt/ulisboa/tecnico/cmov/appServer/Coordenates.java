package pt.ulisboa.tecnico.cmov.appServer;

public class Coordenates {
	
	private boolean latitude;
	private double degrees;
	private String direction;
	
	public Coordenates(boolean latLong, float degrees, String direction){
		
		this.latitude = latLong;;
		this.degrees = degrees;
		this.direction = direction;
	}

	public boolean getLatitude() {
		return latitude;
	}

	public double getDegrees(){
		return degrees;
	}
	
	public String getDirection(){
		return direction;
	}
	
	public String toMessage(){
		return String.valueOf(degrees) +"$"+ direction;
	}
	
	
	
	
}
