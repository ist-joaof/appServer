package pt.ulisboa.tecnico.cmov.appServer;

import java.util.*;

public class ActiveUsers {
	
	Map<Integer,String> activeUsers;
	private int sessionID;
	
	public ActiveUsers(){
		activeUsers = new HashMap<Integer,String>();
		sessionID = 0;
	}
	
	public int activateUser(String username){
		sessionID ++;
		activeUsers.put(sessionID, username);
		return sessionID;
	}
	
	public void deactivateUser(int sessionID){
		activeUsers.remove(sessionID);
	}
	
	public String getUsername(int sessionID){
		return activeUsers.get(sessionID);
	}

}
