import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Connect3Main {
	
	//Opening welcome, mainly for debugging purposes.
	public static void main(String[] args) {
		System.out.println("Welcome to connect 3-- Just like connect 4, but worse.");
		connectToServer();
	}
	
	
	public static void connectToServer() {
		//create new server socket
	    try(ServerSocket serverSocketOne = new ServerSocket(9990)) {
	    	//wait for connection
	    	System.out.println("Waiting for player one to connect...");
	        Socket connectionSocketOne = serverSocketOne.accept();
	        System.out.println("Player one has connected."); 
	        System.out.println("Setting up IO connections for player one...");
	        InputStream hostInput = connectionSocketOne.getInputStream();
	    	ObjectInputStream ois = new ObjectInputStream(hostInput);
	        OutputStream outputFromServer = connectionSocketOne.getOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(outputFromServer);
	        System.out.println("IO done. Building Scanner...");
	        Scanner scannerOne = new Scanner(hostInput, "UTF-8");
	        System.out.println("Scanner done. Building printer...");
	        PrintWriter serverPrintOne = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
        	System.out.println("Printer done.\n\n");
        	
        	serverPrintOne.println("Welcome, host.\n\n");
	        
        	boolean again = true;
        	while(again) {
        		ServerSocket player1Server = null;
        		ServerSocket player2Server = null;
        		Socket player1 = null;
        		Socket player2 = null;
    	        InputStream p1Input = null;
    	        InputStream p2Input = null;
    	        OutputStream p1Output = null;
    	        OutputStream p2Output = null;
    	    	ObjectInputStream p1OIS = null;
    	    	ObjectInputStream p2OIS = null;
    	    	ObjectOutputStream p1OOS = null;
    	    	ObjectOutputStream p2OOS = null;
    	    	//Player 1 setup
        		try{
        			player1 = player1Server.accept();
        			p1Input = player1.getInputStream();
        			p1Output = player1.getOutputStream();
        			p1OIS = new ObjectInputStream(p1Input);
        			p1OOS = new ObjectOutputStream(p1Output);
        			System.out.println("Player 1 setup complete");
        		}
        		catch(Exception e) {
        			System.out.println("Player 1 setup failed");
        			e.printStackTrace();
        		}
        		//Player 2 setup
        		try{
        			player2 = player2Server.accept();
        			p2Input = player2.getInputStream();
        			p2Output = player2.getOutputStream();
        			p2OIS = new ObjectInputStream(p2Input);
        			p2OOS = new ObjectOutputStream(p2Output);
        			System.out.println("Player 2 setup complete");
        		}
        		catch(Exception e) {
        			System.out.println("Player 2 setup failed");
        			e.printStackTrace();
        		}
        		//Play the game
        	}
	        scannerOne.close();
	    } 
	    catch (IOException e) {
	    	System.out.println("Host setup failed");
            e.printStackTrace();
        }
	}
}
