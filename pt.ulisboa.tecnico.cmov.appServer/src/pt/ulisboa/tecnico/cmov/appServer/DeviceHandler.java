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
	
	public Message[] updateUserLocation(String line){
		String[] aux = line.split("_");
		Message[] msg1, msg2;
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
				return null;
		}
		return msg1;
	}
	
	public Message[] getMessagesGPS(String[] coords, User user){
		Location loc = db.checkLocation(Double.parseDouble(coords[0]),Double.parseDouble(coords[1]));
		if(loc != null){
			Message[] messages = loc.checkKeys(user);
			if(messages != null){
				return messages;
			}
		}
		return null;
	}
	
	public Message[] getMessagesWifi(String[] wifis, User user){
		Location loc = db.checkLocation(wifis);
		if(loc != null){
			Message[] messages = loc.checkKeys(user);
			if(messages != null){
				return messages;
			}
		}
		return null;
	}
	
	public String getMsgNames(Message[] msgs){
		String out = msgs[0].getName();
		for(int i=1;i<msgs.length;i++){
			out += "_" + msgs[i].getName();
		}
		return out;
	}
	
	public String getMessages(String line, Message[] msgs){
		String[] names = line.split("_");
		String out = new String();
		boolean first = true;
		for(String name : names){
			for(Message msg : msgs){
				if(msg.getName().equals(name)){
					if(!first){
						out += "_";
					}
					out += msg.toMessage();
					first = false;
				}
			}
		}
		return out;
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
	                	Message[] messages = updateUserLocation(line);
	                	String answer;
	                	if(messages != null){
	                		answer = getMsgNames(messages);
	                		outp.println(answer);
		                	System.out.println(answer);
		                	line = inp.readLine();
		                	System.out.println("Message '" + line);
		                	if(!line.equals("none")){
		                		answer = getMessages(line, messages);
		                		outp.println(answer);
		                		System.out.println(answer);
		                	}		    
	                	}else{
	                		outp.println("empty");
	                		System.out.println("empty");
	                	}
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
