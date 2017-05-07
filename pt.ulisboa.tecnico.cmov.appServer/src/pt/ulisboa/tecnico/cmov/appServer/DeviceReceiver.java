package pt.ulisboa.tecnico.cmov.appServer;

import java.net.ServerSocket;
import java.net.Socket;

public class DeviceReceiver extends Thread{
	
	Database db;
	
	DeviceReceiver(Database database){
		db = database;
	}
	
	public void run(){
		try{
	        ServerSocket sock = new ServerSocket (8081);
	        for (;;){
	            Socket newsock = sock.accept();
	            System.out.println("Creating thread for device...");
	            Thread t = new DeviceHandler(newsock,db);
	            t.start();
	        }
	    }catch (Exception e){
	        System.out.println("IO error " + e);
	    }
	    System.out.println("End!");
	}

}
