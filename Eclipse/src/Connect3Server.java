import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Connect3Server {
	
	//Opening welcome, mainly for debugging purposes.
	public static void main(String[] args) {
		System.out.println("Welcome to connect 3-- Just like connect 4, but worse.");
		connectToServer();
	}
	
	
	public static void connectToServer() {
		//used to check status of second port
		boolean web2open = false;
		//create new server socket
	    try(ServerSocket serverSocketOne = new ServerSocket(9990)) {
	    	//wait for connection
	    	System.out.println("Waiting for player one to connect...");
	        Socket connectionSocketOne = serverSocketOne.accept();
	        System.out.println("Player one has connected."); 
	        System.out.println("Setting up IO connections for player one...");
	        InputStream driverInput = connectionSocketOne.getInputStream();
	    	ObjectInputStream driverOIS = new ObjectInputStream(driverInput);
	        OutputStream outputFromServer = connectionSocketOne.getOutputStream();
	        ObjectOutputStream driverOOS = new ObjectOutputStream(outputFromServer);
	        System.out.println("IO done. Building Scanner...");
	        Scanner scannerOne = new Scanner(driverInput, "UTF-8");
	        System.out.println("Scanner done. Building printer...");
	        PrintWriter serverPrintOne = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
        	System.out.println("Printer done.\n\n");
        	
        	serverPrintOne.println("Welcome, driver.\n\n");
	        
        	boolean again = true;
        	while(again) {
        		ServerSocket web1Server = null;
        		ServerSocket web2Server = null;
        		Socket web1 = null;
        		Socket web2 = null;
    	        InputStream w1Input = null;
    	        InputStream w2Input = null;
    	        OutputStream w1Output = null;
    	        OutputStream w2Output = null;
    	    	ObjectInputStream w1OIS = null;
    	    	ObjectInputStream w2OIS = null;
    	    	ObjectOutputStream w1OOS = null;
    	    	ObjectOutputStream w2OOS = null;
    	    	//Player 1 setup
        		try{
        			web1Server = new ServerSocket(9996);
        			web1 = web1Server.accept();
        			w1Input = web1.getInputStream();
        			w1Output = web1.getOutputStream();
        			w1OIS = new ObjectInputStream(w1Input);
        			w1OOS = new ObjectOutputStream(w1Output);
        			System.out.println("Web 1 setup complete");
        		}
        		catch(Exception e) {
        			System.out.println("Web 1 setup failed");
        			e.printStackTrace();
        			System.exit(2);
        		}
        		
        		//Take p1 selection from web 1
        		String selection1 = "";
        		try {
        			selection1 = (String)w1OIS.readObject();
				} 
        		catch (Exception e1) {
        			System.out.println("Selection 1 failed");
					e1.printStackTrace();
					System.exit(3);
				}
        		
        		//Take p2 selection from web 1
        		String selection2 = "";
        		try {
        			selection2 = (String)w1OIS.readObject();
				} 
        		catch (Exception e1) {
        			System.out.println("Selection 2 failed");
					e1.printStackTrace();
					System.exit(4);
				}
        		//send selection to driver machine
        		driverOOS.writeObject(selection1);
        		driverOOS.writeObject(selection2);
        		
        		//Wait for response from driver machine
        		String ready1 = "";
        		try{
        			ready1 = (String)driverOIS.readObject();
        		}
        		catch(Exception e1) {
        			System.out.println("Driver readystatus 1 failed");
        			e1.printStackTrace();
					System.exit(5);
        		}
        		
        		String ready2 = "";
        		try{
        			ready2 = (String)driverOIS.readObject();
        		}
        		catch(Exception e1) {
        			System.out.println("Driver readystatus 2 failed");
        			e1.printStackTrace();
					System.exit(6);
        		}
        		
    			if(!ready1.equals("ready") || !ready2.equals("ready")) {
    				System.out.println("Driver implementation failed");
    				System.exit(7);
    			}
    			
    			if(selection2.equals("human")) {
	        		//Player 2 setup
	        		try{
	        			web2Server = new ServerSocket(9997);
	        			web2 = web2Server.accept();
	        			w2Input = web2.getInputStream();
	        			w2Output = web2.getOutputStream();
	        			w2OIS = new ObjectInputStream(w2Input);
	        			w2OOS = new ObjectOutputStream(w2Output);
	        			System.out.println("Web 2 setup complete");
	        		}
	        		catch(Exception e) {
	        			System.out.println("Web 2 setup failed");
	        			e.printStackTrace();
	        		}
	        		web2open = true;
    			}

        		//Play the game
        		
        	}
	        scannerOne.close();
	    } 
	    catch (IOException e) {
	    	System.out.println("Driver setup failed");
            e.printStackTrace();
            System.exit(1);
        }
	}
}
