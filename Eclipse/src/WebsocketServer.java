import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//Starter code borrowed from https://stackoverflow.com/questions/41470482/java-server-javascript-client-websockets
public class WebsocketServer extends WebSocketServer {
	
	//Set TCP port connection
    private static int TCP_PORT = 9995;

    private Set<WebSocket> socks;
    //humans have weird interactions with the server and need to be treated special.
    private boolean p1Human, p2Human, p2Physical;
    private Connect3 c3Player1, c3Player2;
    private C3 game;
    private WebSocket p1Sock, p2Sock;
    //used to check if player 1 has connected (if they have we need to make the next connection player 2)
    private boolean player1Exists;
    
    public WebsocketServer() {
        super(new InetSocketAddress(TCP_PORT));
        socks = new HashSet<>();
        game = new C3(4, 5);
        player1Exists = false;
    }
    
    //Actions taken on socket open
    @Override
    public void onOpen(WebSocket sock, ClientHandshake handshake) {
    	if(!p2Physical && !p2Human && p1Sock != null) {
    		System.out.println(p1Sock);
    		sock.send("GTFO");
    	}
    	if(p1Sock != null && p2Sock != null) {
    		sock.send("Nice try bud");
    	}
    	else {
        	if(p2Physical && p2Sock == null) {
        		p2Sock = sock;
        	}
	        socks.add(sock);
	        System.out.println("New connection from " + sock.getRemoteSocketAddress().getAddress().getHostAddress());
    	}
    }

	//Actions taken on socket closure
    @Override
    public void onClose(WebSocket sock, int code, String reason, boolean remote) {
        socks.remove(sock);
        if(socks.size() <= 1) {
        	p2Sock = null;
        	p2Human = false;
        	c3Player2 = null;
        }
        if(socks.size() == 0) {
        	p1Sock = null;
        	p1Human = false;
        	c3Player1 = null;
        }
        System.out.println("Closed connection to " + sock.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /*
     * KEY FOR SIGNALS SENT TO JS (see readme for details): 
     * A = player 1 selection {A[playerSelection]}
     * B = player 2 selection {B[playerSelection]}
     * C = player 1 play (only used if player 1 is human)
     * D = player 2 play (only used if player 2 is human)
     * E = has player 1 joined
     * 
     * d = display board  
     * n = possible positions
     * g = game over
     * p = player
     * x = player2 joined
     * 
     * 
     */
    
    @Override
    public void onMessage(WebSocket sock, String message) {
        System.out.println("Message from client: " + message);
        switch(message.charAt(0)) {
        	case 'A':
        		p1Sock = sock;
        		initPlayer1(message.substring(1, message.length()));
        		break;
        	case 'B':
    			initPlayer2(message.substring(1, message.length()));
    			break;
        	case 'C':
        		if(!p1Human) {
        			System.out.println("Unexpected input: player 1 input given when none expected");
        			System.exit(1);
        		}
        		play(Integer.parseInt(message.substring(1, message.length())), sock);
        		break;
        	case 'D':
        		System.out.println("GOT A D");
        		if(!p2Human && !p2Physical) {
        			System.out.println("Unexpected input: player 2 input given when none expected");
        			System.exit(2);
        		}
        		if(!p2Physical) {
        			p2Sock = sock;
            		play(Integer.parseInt(message.substring(1, message.length())), sock);
        		}
        		else {
            		play(Integer.parseInt(message.substring(1, message.length())), p2Sock);	
        		}
        		break;
        	case 'E':
        		if(player1Exists) {
        			sock.send("p2");
        			p1Sock.send("x");
            		p2Sock = sock;
        		}
        		else {
        			sock.send("p1");
        			player1Exists = true;
        			p1Sock = sock;
        		}
        		break;
    		default:
    			System.out.println("Unknown command, exiting");
    			System.exit(3);        		
        }
    }

    @Override
    public void onError(WebSocket sock, Exception e) {
        if (sock != null) {
            socks.remove(sock);
            // do some thing if required
        }
        e.printStackTrace();
    }
    
    public void play(int playCol, WebSocket sock) {
    	try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	game = game.place(playCol);
    	String jStr = game.toJSString();
    	System.out.println("Socket: " + sock);
    	System.out.println(jStr);
    	p1Sock.send(jStr);
    	p1Sock.send(game.getValidsAsString());
    	if(p2Human) {
    		System.out.println("sending to p2");
    		p2Sock.send(jStr);
        	p2Sock.send(game.getValidsAsString());
    	}
    	else if(p2Physical && !sock.equals(p2Sock)) {
    		System.out.println("SENDING TO THE AI");
    		p2Sock.send("d" + game.lastCol);
    	}
    	if(game.gameOver) {
    		p1Sock.send("g"+game.winner);
    		if(p2Human || p2Physical) {
    			p2Sock.send("g"+game.winner);
    		}
    		System.exit(0);
    	}
    	if(!p1Human && p2Human && sock.equals(p2Sock)) {
    		System.out.println("player 1 is not human so they are being called directly");
    		play(c3Player1.play(game), p1Sock);
    	}
    	else if(!p1Human && !p2Human) {
    		System.out.println("player 2 is not human so they are being called directly");
    		play(c3Player1.play(game), p1Sock);
    	}
    	else if(!p2Physical && !p2Human && sock != null && sock.equals(p1Sock)) {
    		play(c3Player2.play(game), null);
    	}
    }
    
    public void initPlayer1(String type) {
    	System.out.println("Initializing player 1");
    	//set human to false and if they are human it gets overwritten
    	p1Human = false;
    	//find out what kind of player p1 is
    	switch(type) {
    	case "human":
    		System.out.println("Player 1 is a human");
    		p1Human = true;
    		break;
    	case "pruned-mm":
    		c3Player1 = new PrunedMinimax();
    		break;
    	case "minimax":
    		c3Player1 = new Minimax();
    		break;
    	case "random":
    		c3Player1 = new RandomNoAutowin();
    		break;
    	case "aw":
    		c3Player1 = new Random();
    		break;
		default:
			System.out.println("No valid input detected for player 1. Exiting.");
			System.exit(5);
    	}
    }
    
    public void initPlayer2(String type) {
		System.out.println("Initializing Player 2");
		//set human to false and if they are human it gets overwritten
    	p2Human = false;
    	//find out what kind of player p2 is
    	switch(type) {
    	case "human":
    		System.out.println("Player 2 is a human");
    		p2Human = true;
    		break;
    	case "pruned-mm":
    		c3Player2 = new PrunedMinimax();
    		break;
    	case "minimax":
    		c3Player2 = new Minimax();
    		break;
    	case "random":
    		c3Player2 = new RandomNoAutowin();
    		break;
    	case "aw":
    		c3Player2 = new Random();
    		break;
    	case "hw":
    		p2Physical = true;
    		System.out.println("THE PHYSICAL AI HAS BEEN ENGAGED");
    		break;
		default:
			System.out.println("No valid input detected for player 2. Exiting.");
			System.exit(6);
    	}
    	if(!p1Human) {
    		play(c3Player1.play(game), p1Sock);
    	}
    }

}