package pt.ulisboa.tecnico.cmov.appServer;

import java.util.*;

public class UserAccounts {
	Map<String,User> userAccounts;
	
	public UserAccounts(){
		userAccounts = new HashMap<String,User>();
	}
	
	
	public User newUser(String username, String password){
		User newUser = new User(username,password);
		userAccounts.put(username, newUser);
		return newUser;
	}
	
	public String getPassword(String username){
		User user = userAccounts.get(username);
		return user.getPassword();
	}
	
	public boolean checkUsername(String username){
		return userAccounts.containsKey(username);
	}
	
	public User getUser(String username){
		return userAccounts.get(username);
	}
}
