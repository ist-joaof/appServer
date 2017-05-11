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
	String out;
	
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
	
	public String getKeys(String line){
		String[] list;
		String out;
		int sessionID = Integer.parseInt(line.split("_")[1]);
		User user = db.getUserFromSession(sessionID);
		list = user.getAllKeyPairs();
		if(list != null){
			out = list[0];
			for(int i=1;i<list.length;i++)
				out = out + "_" + list[i]; 
			return out;
		}else{
			return "empty";
		}
	}
	
	public void updateDB(String line){
		String[] aux = line.split("_");
		Boolean add = true;
		if(aux[0].equals("K-"))
			add = false;		
		int sessionID = Integer.parseInt(aux[1]);
		aux = aux[2].split("=");
		User user = db.getUserFromSession(sessionID);
		if(add){
			user.addKeyPair(aux[0], aux[1]);
			db.newKey(aux[0], user);
			System.out.println("New keypair: " + aux[0]+"="+aux[1]);
		}else{
			user.removeKeyPair(aux[0]);
			db.removeKeyPair(aux[0],user);
			System.out.println("Keypair " + aux[0]+"="+aux[1]+" removed");
		}
	}
	
	public void deleteKeys(String line){
		User user = db.getUserAccounts().getUser(line.split("_")[1]);
		user.deleteAllKeys();
		db.removeAllKeysFromUser(user.getAllKeyPairs(), user);
		System.out.println("All keys deleted");
	}
	
	public void updateLocation(String line){
		
		String operation = line.split("_")[0];
		line = line.substring(5, line.length());
		Location location;
		if (operation.equals("Loc+")){
			location = new Location (line);
			db.addLocation(location.getName(), location);
			System.out.println("New Location Added");
		}else{
			db.removeLocation(line.split("_")[0]);
			System.out.println("Location Removed");
			
		}
	}
	
	public String listLocation(){
		String aux = new String();
		String[] list = db.listLocation();
		aux = list[0];
		for(int i = 1; i < list.length; i++){
			aux += "_" + list[i];
		}
		return aux;
	}
	
	public void updateMessage(String line){
		String operation = line.split("_")[0];
		line = line.substring(3, line.length());
		Message message;
		if (operation.equals("M+")){
			String[] aux = line.split("_");
			Location location = db.getLocation(aux[6]);
			User user = db.getUserFromSession(Integer.parseInt(aux[7]));
			line = aux[0] + "_" + aux[1] + "_" + aux[2] + "_" + aux[3] + "_" + aux[4] + "_" + aux[5];
			message = new Message(line, location, user);
		} else{
			String[] aux = line.split("_");
			User user = db.getUserFromSession(Integer.parseInt(aux[1]));
			message = user.getMessage(aux[0]);
			message.removeMessage();
			
		}
	}
	
	public String listMessages(String line){
		int session = Integer.parseInt(line.split("_")[1]);
		User user = db.getUserFromSession(session);
		String[] aux = user.listMessages();
		if(aux.length > 0 && aux[0] != null){
			String out = aux[0];
			for(int i = 1; i < aux.length; i++){
				out += "_" + aux[i];
			}
			return out;
		}
		return "empty";
	}
	
	
	public void run() {
	    try {
	
	        PrintWriter outp = new PrintWriter(newsock.getOutputStream(), true);
	        BufferedReader inp = new BufferedReader(new InputStreamReader(
	                newsock.getInputStream()));
	
	        boolean more_data = true;
	        String line;
	        String operation;
	
	        while (more_data) {
	            line = inp.readLine();
	            System.out.println("Message '" + line);
	            if (line == null) {
	                System.out.println("line = null");
	                more_data = false;
	            } else {
	                operation = line.trim().split("_")[0];
	                if(operation.equals("L")){
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
	                if(operation.equals("S")){
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
	                if(operation.equals("O")){
	                	logout(line);
	                	System.out.println("Logout session: " + line.trim().split("_")[1]);
	                	more_data=false;
	                }
	                if(operation.equals("K")){
	                	out = getKeys(line);
                		outp.println(out);
                		System.out.println(out);
	                	more_data=false;
	                }
	                if(operation.equals("K+") || operation.equals("K-")){
	                	updateDB(line);
	                	more_data=false;
	                }
	                
	                if(operation.equals("D")){
	                	deleteKeys(line);
	                	more_data=false;
	                }
	                
	                if(operation.equals("Loc+") || operation.equals("Loc-")){
	                	updateLocation(line);
	                	more_data=false;
	                	
	                }
	                if(operation.equals("Loc")){
	                	out = listLocation();
	                	outp.println(out);
	                	System.out.println(out);
	                	more_data=false;
	                }
	                if(operation.equals("M+") || operation.equals("M-")){
	                	updateMessage(line);
	                	more_data = false;
	                }
	                if(operation.equals("M")){
	                	out = listMessages(line);
	                	outp.println(out);
	                	System.out.println(out);
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