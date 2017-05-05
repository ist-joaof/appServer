package pt.ulisboa.tecnico.cmov.appServer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Database {
	
	private UserAccounts userAccounts;
    private ActiveUsers activeUsers;
	private Map<String,Map<String,User>> keyPairs;
    
	public Database(){
		userAccounts = new UserAccounts();
        activeUsers = new ActiveUsers();
        keyPairs = new HashMap<String,Map<String,User>>();
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

}
