package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;

public class Database {
	
	private UserAccounts userAccounts;
    private ActiveUsers activeUsers;
	private Map<String,Map<String,User>> keyPairs;
	private Map<String, Location> locations;
    
	public Database(){
		userAccounts = new UserAccounts();
        activeUsers = new ActiveUsers();
        keyPairs = new HashMap<String,Map<String,User>>();
        locations = new HashMap<String, Location>();
	}
	
	public void newKey(String key, User user){
		Map<String,User> userList;
		if(keyPairs.containsKey(key)){
			userList = keyPairs.get(key);
			userList.put(user.getUsername(), user);
		}else{
			userList = new HashMap<String,User>();
			userList.put(user.getUsername(), user);
			keyPairs.put(key, userList);
		}
	}
	
	public void removeKeyPair(String key, User user){
		Map<String,User> userList = keyPairs.get(key);
		userList.remove(user.getUsername());
		if(userList.isEmpty()){
			keyPairs.remove(key);
		}
	}
	
	public void removeAllKeysFromUser(String[] keys,User user){
		for(int i=0;i<keys.length;i++){
			removeKeyPair(keys[i],user);
		}
	}
	
	public String[] listKeyPairs(){
		String[] aux = new String[keyPairs.size()];
		int i = 0;
		for(Map.Entry<String, Map<String,User>> entry: keyPairs.entrySet()){
			aux[i] = entry.getKey();
			i++;
		}
		return aux;
	}
	
	public UserAccounts getUserAccounts(){
		return userAccounts;
	}
	
	public ActiveUsers getActiveUsers(){
		return activeUsers;
	}
	
	public User getUserFromSession(int sessionID){
		String username = activeUsers.getUsername(sessionID);
		return userAccounts.getUser(username);
	}
	
	public void addLocation(String name, Location location){
		locations.put(name, location);
	}
	
	public void removeLocation(String name){
		locations.remove(name);
		
	}
	
	public String[] listLocation(){
		String[] aux = new String[locations.size()];
		int i = 0;
		for(Map.Entry<String, Location> entry:locations.entrySet()){
			aux[i] = entry.getKey();
			i++;
		}
		return aux;
		
	}
	
	public Location getLocation(String name){
		return locations.get(name);
	}

}
