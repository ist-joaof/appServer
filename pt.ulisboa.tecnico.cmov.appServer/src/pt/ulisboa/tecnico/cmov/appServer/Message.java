package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;



public class Message {
	
	private String name, text;
	private boolean whiteList, centralized;
	private Map<String,String> keyPairs;
	private Location location;
	private User owner;
	private int day, month, hour, minute;
	
	public Message (String line, Location location, User user){
		this.location = location;
		this.owner = user;
		//this.day = day;
		//this.month = month;
		//this.hour = hour;
		//this.minute = minute;
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
		
		user.addMessage(this);
		location.addMessage(this);
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
	
	public int[] getExpiration(){
		return new int[]{day,month,hour,minute};
	}
	
	public String toMessage(){
		String out;
		out = this.name;
		out += "_" + this.text;
		String coords = this.location.getLat().toMessage() + ":" + this.location.getLong().toMessage();
		out += "_" + coords;
		out += "_" + day + "/" + month + "," + hour + ":" + minute;
		return out;
	}
	//ver se iguala um ou todos
	public boolean checkKeys(User user){
		String[] userKeys = user.getAllKeys();
		boolean out = false;
		for(int i=0;i<userKeys.length;i++){
			String key = userKeys[i];
			if(keyPairs.containsKey(key)){
				if(keyPairs.get(key).equals(user.getValue(key)))
					out = true;
			}
		}
		if(whiteList){
			return out;
		}else{
			return !out;
		}
	}
}
