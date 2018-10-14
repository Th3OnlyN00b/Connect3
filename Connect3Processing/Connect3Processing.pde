import processing.serial.*;
import websockets.*;

Serial port;
WebsocketClient wsc;
/* change serialName to match yours */
String serialName = "COM4";
boolean playerTurn = true;

void setup() {
  port = new Serial(this, serialName, 9600);
  println("cheese");
  wsc= new WebsocketClient(this, "ws://b69079a0.ngrok.io");
}

void draw(){
  if(port.available() > 0 && playerTurn){
    byte playerMove = (byte)port.read();
    println(playerMove);
    String a = "D" + playerMove;
    wsc.sendMessage(a);
    println("Message sent: " + a);
  }
}

void webSocketEvent(String msg){
  println("Message received: " + msg);
  switch(msg.charAt(0)){
    case 'd':
      port.write(msg.charAt(1));
      break;
    default:
      println("NOPE");
      System.exit(0);
  }
}