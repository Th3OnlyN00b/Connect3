import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Connect3Main {
	
	//Opening welcome, mainly for debugging purposes.
	public static void main(String[] args) {
		System.out.println("Welcome to connect 3-- Just like connect 4, but worse.");
		//Nobody needs concurrency
		/*while(true) {
			Worker w;
			try {
				w = new Worker(socket.accept());
				System.out.println("New connection from " + socket.getInetAddress().getLocalHost().getHostAddress());
				Thread t = new Thread(w);
				t.start();
				System.out.println("Thread started with ID: " + t.getId());
			} catch (Exception e) {
				System.out.println("Unable to accept connection");
				System.exit(3);
			}
			
		}*/
		
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
	        InputStream inputToServer = connectionSocketOne.getInputStream();
	        OutputStream outputFromServer = connectionSocketOne.getOutputStream();
	        System.out.println("IO done. Building Scanner...");
	        Scanner scannerOne = new Scanner(inputToServer, "UTF-8");
	        System.out.println("Scanner done. Building printer...");
	        PrintWriter serverPrintOne = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
        	System.out.println("Printer done.\n\n");
        	
        	serverPrintOne.println("Welcome, player one.\n\n");
	        
        	
	        System.out.println("Asking if player one wants to play an AI or another player...");
	        serverPrintOne.println("Would you like to play against a human or AI?");
	        String playerAIChoice = scannerOne.nextLine();
	        Connect3 c3Game3;
	        
        	boolean again = true;
        	while(again) {
        		//Play the game
        	}
	        scannerOne.close();
	    } 
	    catch (IOException e) {
            e.printStackTrace();
        }
	}
}
