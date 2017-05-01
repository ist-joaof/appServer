package pt.ulisboa.tecnico.cmov.appServer;

import java.io.*;
import java.net.*;
import java.util.Random;

class ThreadHandler extends Thread {
	Socket newsock;
	Database db;
	UserAccounts accounts;
	int n;
	int aux;
	
	ThreadHandler(Socket s, int v, Database database) {
	    newsock = s;
	    n = v;
	    db = database;
	    accounts = db.getUserAccounts();
	}
	
	public boolean checkLogin(String line){
		String[] aux = line.split("_");
		String username = aux[1];
		String password = aux[2];
		if(!accounts.checkUsername(username))
			return false;
		if(password.equals(accounts.getPassword(username))){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean checkSignIn(String line){
		String[] aux = line.split("_");
		String username = aux[1];
		String password = aux[2];
		if(accounts.checkUsername(username)){
			return false;
		}else{
			accounts.newUser(username, password);
			return true;
		}
	}
	
	public int generateSession(String username){
		return db.getActiveUsers().activateUser(username);
	}
	
	public void logout(String line){
		int sessionID;
		String[] aux = line.split("_");
		sessionID = Integer.parseInt(aux[1]);
		db.getActiveUsers().deactivateUser(sessionID);
	}
	
	
	public void run() {
	    try {
	
	        PrintWriter outp = new PrintWriter(newsock.getOutputStream(), true);
	        BufferedReader inp = new BufferedReader(new InputStreamReader(
	                newsock.getInputStream()));
	
	        boolean more_data = true;
	        String line;
	
	        while (more_data) {
	            line = inp.readLine();
	            System.out.println("Message '" + line);
	            if (line == null) {
	                System.out.println("line = null");
	                more_data = false;
	            } else {
	                //outp.println("From server: " + line + ". \n");
	                if(line.trim().split("_")[0].equals("L")){
	                	if(checkLogin(line)){
	                		aux = generateSession(line.split("_")[1]);
	                		outp.println(String.valueOf(aux));
	                		System.out.println(aux);
	                	}else{
	                		outp.println("0");
	                		System.out.println("0");
	                	}
	                	more_data=false;
	                }
	                if(line.trim().split("_")[0].equals("S")){
	                	if(checkSignIn(line)){
	                		aux = generateSession(line.split("_")[1]);
	                		outp.println(String.valueOf(aux));
	                		System.out.println(aux);
	                	}else{
	                		outp.println("0");
	                		System.out.println(0);
	                	}
	                	more_data=false;
	                }
	                if(line.trim().split("_")[0].equals("O")){
	                	logout(line);
	                	System.out.println("Logout session: " + line.trim().split("_")[1]);
	                	more_data=false;
	                }
	            }
	        }
	        newsock.close();
	        System.out.println("Disconnected from client number: " + n);
	    } catch (Exception e) {
	        System.out.println("IO error " + e);
	    }
	
	}
}