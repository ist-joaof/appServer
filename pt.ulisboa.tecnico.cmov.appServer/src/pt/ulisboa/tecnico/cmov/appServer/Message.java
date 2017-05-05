package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;



public class Message {
	
	private String name, text;
	private boolean whiteList, centralized;
	private Map<String,String> keyPairs;
	private Location location;
	private User owner;
	
	public Message (String line, Location location, User user){
		this.location = location;
		this.owner = user;
		String[] aux = line.split("_");
		String[] keys;
		name = aux[0];
		text = aux[1];
		whiteList = true; 
		if (aux[2].equals("blackList"))
			whiteList = false;
		
		keys = aux[3].split("$");
		keyPairs = new HashMap<String,String>();
		for (int i =0; i < keys.length; i++){
			String[] temp = keys[i].split("=");
			keyPairs.put(temp[0], temp[1]);
			
		}
		centralized = true;
		if (aux[4].equals("descentralized"))
			centralized = false;
	}
	
	public String getName(){
		return name;
	}
	
	public String getText(){
		return text;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public User getOwner(){
		return owner;
	}

	public boolean getListType(){
		return whiteList;
	}
	
	public boolean getDeliveryType(){
		return centralized;
	}
}
