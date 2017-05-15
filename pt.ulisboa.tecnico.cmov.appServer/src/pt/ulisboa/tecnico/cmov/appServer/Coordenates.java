package pt.ulisboa.tecnico.cmov.appServer;

public class Coordenates {
	
	private boolean latitude;
	private double degrees;
	private String direction;
	
	public Coordenates(boolean latLong, double degrees, String direction){
		
		if(direction.equals("S") || direction.equals("W"))
			degrees = -degrees;
		
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
		double degrees = this.degrees;
		if(direction.equals("S") || direction.equals("W"))
			degrees = -degrees;
		
		return String.valueOf(degrees) +"$"+ direction;
	}
	
	
	
	
}
