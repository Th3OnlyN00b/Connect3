import processing.serial.*;
import websockets.*;

Serial port;
WebsocketClient wsc;
/* Remember to change serialName to whatever the name of your USB port is! */
final String serialName = "COM4";
boolean playerTurn = true;

void setup() {
  port = new Serial(this, serialName, 9600);
  wsc= new WebsocketClient(this, "ws://b69079a0.ngrok.io");
}

void draw(){
  //Taking data from the Arduino when available and sends it to the server
  if(port.available() > 0 && playerTurn){
    char playerMove = (char)port.read();
    String a = "D" + playerMove;
    wsc.sendMessage(a);
    println("Message sent: " + a);
  }
}

void webSocketEvent(String msg){
  //Takes data from the server and passes it to the Arduino
  println("Message received: " + msg);
  switch(msg.charAt(0)){
    case 'd':
      port.write(msg.charAt(1));
      break;
    case 'g':
      port.write('G');
      break;
    default:
      println("NOPE");
      System.exit(0);
  }
}