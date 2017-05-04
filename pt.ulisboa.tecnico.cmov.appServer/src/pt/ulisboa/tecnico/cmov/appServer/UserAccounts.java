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
		admin.addKeyPair("curso", "METI");
		admin.addKeyPair("Campus", "TagusPark");
		admin.addKeyPair("Transporte", "carro");
		admin.addKeyPair("type", "admin");
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
