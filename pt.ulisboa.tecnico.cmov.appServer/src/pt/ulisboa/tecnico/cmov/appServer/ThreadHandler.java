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
		list = user.getAllKeys();
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
		if(aux[0].equals("-"))
			add = false;		
		int sessionID = Integer.parseInt(aux[1]);
		aux = aux[2].split("=");
		User user = db.getUserFromSession(sessionID);
		if(add){
			user.addKeyPair(aux[0], aux[1]);
			System.out.println("New keypair: " + aux[0]+"="+aux[1]);
		}else{
			user.removeKeyPair(aux[0]);
		}
	}
	
	public void deleteKeys(String line){
		User user = db.getUserAccounts().getUser(line.split("_")[1]);
		user.deleteAllKeys();
		System.out.println("All keys deleted");
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
	            }
	        }
	        newsock.close();
	        System.out.println("Disconnected from client number: " + n);
	    } catch (Exception e) {
	        System.out.println("IO error " + e);
	    }
	
	}
}