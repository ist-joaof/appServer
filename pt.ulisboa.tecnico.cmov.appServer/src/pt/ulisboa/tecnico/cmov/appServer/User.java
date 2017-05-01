package pt.ulisboa.tecnico.cmov.appServer;

public class User {

	private String username;
	private String password;
	
	public User(String username , String password){
		this.username = username;
		this.password = password;
	}
	
	public void changePassword(String newPassword){
		password = newPassword;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getUsername(){
		return username;
	}
}
