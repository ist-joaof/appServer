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
 		String[] aux = line.split("_");
		String[] keys;
		String[] expiration = aux[5].split(",");
		this.day = Integer.parseInt(expiration[0].split("/")[0]);
		this.month = Integer.parseInt(expiration[0].split("/")[1]);
		this.hour = Integer.parseInt(expiration[1].split(":")[0]);
		this.minute = Integer.parseInt(expiration[1].split(":")[1]);
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
		out += "_" + day + "/" + month + "," + hour + ":" + minute;
		if(location.isGPS()){
			String coords = this.location.getLat().toMessage() + ":" + this.location.getLong().toMessage();
			out += "_" + coords;
		}else{
			out += "_" + this.location.getWifiID();
		}
		return out;
	}
	//ver se iguala um ou todos
	public boolean checkKeys(User user){
		String[] userKeys = user.getAllKeys();
		boolean out = false;
		if(userKeys != null){
			for(int i=0;i<userKeys.length;i++){
				String key = userKeys[i];
				if(keyPairs.containsKey(key)){
					String v1 = keyPairs.get(key);
					String v2 = user.getValue(key);
					if(v1.equals(v2))
						out = true;
				}
			}
			if(whiteList){
				return out;
			}else{
				return !out;
			}
		}
		return out;
	}
}
