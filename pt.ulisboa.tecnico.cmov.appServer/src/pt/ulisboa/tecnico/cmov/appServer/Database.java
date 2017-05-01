package pt.ulisboa.tecnico.cmov.appServer;

public class Database {
	
	private UserAccounts userAccounts;
    private ActiveUsers activeUsers;
	
	public Database(){
		userAccounts = new UserAccounts();
        activeUsers = new ActiveUsers();
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
