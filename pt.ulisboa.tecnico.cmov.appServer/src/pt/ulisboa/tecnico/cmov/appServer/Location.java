package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;



public class Location {
	
	private String name;
	private int radius;
	private Coordenates lat, longe; 
	private String wifiID;
	private boolean gps;
	private Map<String, Message> messages;
	
	public Location(String line){
		
		messages = new HashMap<String, Message>();
		String[] aux = line.split("_");
		if (aux.length > 2) {
			gps = true;
			name = aux[0];
			lat = new Coordenates(true, Integer.parseInt(aux[1]), Integer.parseInt(aux[2]), Float.parseFloat(aux[3]), aux[4]);
			longe = new Coordenates(false, Integer.parseInt(aux[5]), Integer.parseInt(aux[6]), Float.parseFloat(aux[7]), aux[8]);
			radius = Integer.parseInt(aux[9]);

		} else {
			gps = false;
			name = aux[0];
			wifiID = aux[1];
		} 
	}
	
	
	
	public String getName(){
		return name;
	}
	
	public Coordenates getLat(){
		return lat;
	}
	
	public Coordenates getLong(){
		return longe;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public String getWifiID(){
		return wifiID;
	}
	
	public void addMessage(Message message){
		messages.put(message.getName(), message);
		
	}
	
	public void removeMessage(String name){
		messages.remove(name);
	}
	
	public Message getMessages(String name){
		
	return messages.get(name);
	}

}
