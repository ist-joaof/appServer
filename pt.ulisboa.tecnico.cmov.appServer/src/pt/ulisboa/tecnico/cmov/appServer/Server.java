package pt.ulisboa.tecnico.cmov.appServer;

import java.net.*;

public class Server {

	public static void main(String[] args) {

        int nreq = 1;
        Database db = new Database();
        try
        {
            ServerSocket sock = new ServerSocket (8080);
            for (;;)
            {
                Socket newsock = sock.accept();
                System.out.println("Creating thread ...");
                Thread t = new ThreadHandler(newsock,nreq,db);
                t.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("IO error " + e);
        }
        System.out.println("End!");
    }
}
