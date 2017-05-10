package pt.ulisboa.tecnico.cmov.appServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math.*;
import java.lang.reflect.Array;



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
			lat = new Coordenates(true, Double.parseDouble(aux[1]),  aux[2]);
			longe = new Coordenates(false, Double.parseDouble(aux[3]), aux[4]);
			radius = Integer.parseInt(aux[5]);

		} else {
			gps = false;
			name = aux[0];
			wifiID = aux[1];
		} 
	}	
	
	public String getName(){
		return name;
	}
	
	public boolean isGPS(){
		return gps;
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
	
	public boolean isInRange(double lat, double longe){
		double d = distance(getLat().getDegrees(),getLong().getDegrees(),lat,longe);
		if(d > radius){
			return false;
		}else{
			return true;
		}
	}
	
	public double distance(double latc, double longc, double latd, double longd){
		double radiusE = 6371*Math.pow(10.0, 3.0);
		double deltaLat,deltaLong, a, c;
		latc = Math.toRadians(latc);
		latd = Math.toRadians(latd);
		deltaLat = Math.toRadians(latd-latc);
		deltaLong = Math.toRadians(longd-longc);
		a = Math.pow(Math.sin(deltaLat/2), 2) + Math.cos(latc)*Math.cos(latd)*Math.pow(Math.sin(deltaLong/2),2);
		c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return radiusE*c;
	}
	
	public Message[] checkKeys(User user){
		ArrayList<Message> out = new ArrayList<Message>();
		int ptr = -1;
		Message[] erase = new Message[messages.size()];
		for(Map.Entry<String, Message> entry : messages.entrySet()){
			Message message = entry.getValue();
			if(message.checkKeys(user) && message.getDeliveryType()){
				if(!message.getOwner().getUsername().equals(user.getUsername())){
					if(message.isValid()){
						ptr ++;
						out.add(message);
					}else{
						erase[ptr+1] = message;
					}
				}
			}
		}
		for(Message msg : erase){
			if(msg != null){
				msg.removeMessage();
			}
		}
		if(ptr >= 0){
			Message[] msgs = new Message[out.size()];
			int i = 0;
			for(Message msg : out){
				msgs[i] = msg;
				i++;
			}
			return msgs;
		}else{
		return null;
		}
	}

}
