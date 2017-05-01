package pt.ulisboa.tecnico.cmov.appServer;

import java.util.*;

public class UserAccounts {
	Map<String,User> userAccounts;
	
	public UserAccounts(){
		userAccounts = new HashMap<String,User>();
		createAdmin();
	}
	
	public void createAdmin(){
		User admin = newUser("admin","admin");
		admin.newKeyPair("curso", "METI");
		admin.newKeyPair("Campus", "TagusPark");
		admin.newKeyPair("Transporte", "carro");
		admin.newKeyPair("type", "admin");
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
