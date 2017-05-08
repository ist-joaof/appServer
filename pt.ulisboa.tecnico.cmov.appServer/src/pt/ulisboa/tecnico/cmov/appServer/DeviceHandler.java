package pt.ulisboa.tecnico.cmov.appServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DeviceHandler extends Thread{

	Socket newsock;
	Database db;
	UserAccounts accounts;
	int aux;
	String out;
	
	DeviceHandler(Socket s, Database database) {
	    newsock = s;
	    db = database;
	    accounts = db.getUserAccounts();
	}
	
	public String updateUserLocation(String line){
		String[] aux = line.split("_");
		String[] msg1, msg2;
		User user = db.getUserFromSession(Integer.parseInt(aux[1]));
		String[] coords = new String[]{aux[2],aux[3]};
		String[] wifis = new String[aux.length-4];
		for(int i=4;i<aux.length;i++){
			wifis[i-4] = aux[i];
		}
		msg1 = getMessagesGPS(coords, user);
		msg2 = getMessagesWifi(wifis,user);
		if(msg1 == null){
			msg1 = msg2;
			if(msg2 == null)
				return "empty";
		}
		String out = msg1[0];
		for(int i=1;i<msg1.length;i++){
			out += "_" + aux[i];
		}
		
		return out;
	}
	
	public String[] getMessagesGPS(String[] coords, User user){
		Location loc = db.checkLocation(Double.parseDouble(coords[0]),Double.parseDouble(coords[1]));
		if(loc != null){
			Message[] messages = loc.checkKeys(user);
			if(messages != null){
				String[] out = new String[messages.length];
				for(int i=0;i<messages.length;i++){
					out[i] = messages[i].toMessage();
				}
				return out;
			}
		}
		return null;
	}
	
	public String[] getMessagesWifi(String[] wifis, User user){
		Location loc = db.checkLocation(wifis);
		if(loc != null){
			Message[] messages = loc.checkKeys(user);
			if(messages != null){
				String[] out = new String[messages.length];
				for(int i=0;i<messages.length;i++){
					out[i] = messages[i].toMessage();
				}
				return out;
			}
		}
		return null;
	}
	
	public void run(){
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
	                if(operation.equals("U")){
	                	String answer = updateUserLocation(line);
	                	outp.println(answer);
	                	System.out.println(answer);
	                }
	            }
	        }
	        newsock.close();
	        System.out.println("Disconnected from device");
	    } catch (Exception e) {
	        System.out.println("IO error " + e);
	    }
	}
}
