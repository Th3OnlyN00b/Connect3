import java.io.*;
import java.util.*;
import java.net.*;

public class Connect3Driver {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Host system starting up");
		Socket serverSock = null;
		InetAddress a;
		try{
			serverSock = new Socket("18.224.19.21", 9990);
			System.out.println("hi");
		}
		catch(Exception e) {
			System.out.println("Failure to connect to server");
			e.printStackTrace();
			System.exit(1);
		}
		a = serverSock.getInetAddress();
		System.out.println("Connected to " + a);
		scan.nextLine();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PrintWriter out;
		try { 
			out = new PrintWriter(serverSock.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan.nextLine();
	}
	
}
