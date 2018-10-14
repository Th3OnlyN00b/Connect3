var connection = new WebSocket('ws://1b34ccd2.ngrok.io:9995');
var player = "";
var availableSpaces = "01234";
var victory = -1;
var turn = 1;
var connected = false;

// Parse code based on player (2 for player 1, or 1 for player 2)
var p1 = 2;
var p2 = 1;
var possibleTypes = ["human", "random", "aw", "minimax", "pruned-mm"];
var formal_names = {"human": "Human", "random": "Random with Auto", "aw": "Automatic", "minimax": "Minmax", "pruned-mm": "Pruned Minmax"};
var waiting = (p1 == "human" && p2 == "human");

// Replace player placeholders with player names
$(document).ready(function() {
  var playerTypeList = parseURL();
  var p1Type = "";
  var p2Type = "";
  var joining = "";

  // If everything is correct, set the players and start the game
  if(playerTypeList != null){
    p1Type = playerTypeList[0];
    p2Type = playerTypeList[1];
    joining = playerTypeList[2];

    document.getElementById("player1Tag").innerHTML = formal_names[p1Type];
    document.getElementById("player2Tag").innerHTML = formal_names[p2Type];
  } 

  // Disable playing if the game was improperly entered
  else {
    victory = 0;
  }
})

// Confirm connection
connection.onopen = function (e) {
  console.log('Connected!');
  connection.send("E");
}

// Log errors
connection.onerror = function (error) {
  console.log('WebSocket Error ' + error);
}

// Log messages from the server
connection.onmessage = function (e) {
  console.log('Server: ' + e.data);
  var string_data = e.data;
  var first_char = string_data.charAt(0);

  if(first_char == 'x'){
    waiting = false;
  }

  // Set player data for the player
  else if(first_char == 'p'){
    player = e.data.charAt(1);

    if(joining && player == p1){
      alert("Error: There is no game to join!")
    }

    var playerLetter = player==1?"A":"B";
    connection.send(playerLetter + "human");
    console.log("Player Letter has been sent: " + playerLetter + "human");
    connected = true;

    document.getElementById("subheader").innerHTML = "Welcome, player " + player + "!"
  }

  // Update the grid if there is a command received to do so, and toggle the player turn
  else if(first_char == 'd'){
    colorSpaces(string_data.slice(1, string_data.length));
    turn = turn==1?2:1;
  }

  // Update the availability of the columns
  else if(first_char == 'n'){
    availableSpaces = string_data.slice(1,string_data.length);
  }

  // If an end state is reached, make sure players are notified accordingly
  else if(first_char == 'g'){
    victory = string_data.charAt(1);
    if(victory == 0){
      alert("Wow, the board is full! This game has resulted in a tie.");
    }
    if(victory == p1){
      alert("Congratulations, player 1! You have won the game.");
    }
     else if(victory == p2){
        alert("Congratulations, player 2! You have won the game.");
    }
  }

}

function colorSpaces(spaceList){
  for(var i = 0; i < 20; i++){
    changeCellColor(i, spaceList);
  }
}

// Changes an individual cell's color based on its id code (as its index in the parsed string data)
// and changes the board according to which player/if a player is there
function changeCellColor(cell, spaceList){
  var player = spaceList.charAt(cell);
  if(player == p1) document.getElementById(cell).style.backgroundColor = "#969696";
  else if(player == p2) document.getElementById(cell).style.backgroundColor = "#ff0066";
  else document.getElementById(cell).style.backgroundColor = "";
}

// Sends the move that would be cast by clicking on a given space
function sendClick(cell){
  var columnNum = cell % 5;
  console.log("Cell has been clicked on");

  // The game cannot start if there is no connection to the server!
  if(connected == false){
    alert("Error: Problem setting up the game. Check your URL or your connection.");
  }

  // Player 1, if a human, must wait until player 2 appears
  else if(waiting){
    alert("Waiting for the second player to join the server...");
  }

  // Makes sure space isn't already taken
  else if(document.getElementById(cell).style.backgroundColor != ""){
    showInvalidMoveModal();
  } 

  // Continue, as long as the game isn't over
  else if(victory == -1) {
    var playerLetter = player==1?"C":"D";
    console.log("Move is being played in column " + columnNum);
    connection.send(playerLetter + columnNum);
  }
}

// This function shows the modal for invalid moves
function showInvalidMoveModal(){
  $("#invalidMoveModal").modal("show");
}

// This parses the received URL, getting the types of players
function parseURL(){
  var decoded = decodeURIComponent(window.location.search);
  var queryString = decoded.substring(1);
  var queries = queryString.split("&");

  // Make sure that there is the right number and type of queries
  if(queries.length != 3 || queries[0].slice(0,2) != "p1" || queries[1].slice(0,2) != "p2"){
    alert("There seems to be something wrong with the URL. Please make sure that both players have been selected on a previous page.");
    return;
  }

  // Make sure that both players are valid types
  if(!possibleTypes.includes(queries[0].slice(3)) || !possibleTypes.includes(queries[1].slice(3))){
    alert("A player seems to be of an invalid type. Please make sure that both players have been selected on a previous page.");
    return;
  }
  else{
    var query1 = queries[0].slice(3);
    var query2 = queries[1].slice(3);
    var query2 = queries[2].slice(5);
    if(query2.charAt(query2.length - 1) == "#"){
      query2 = query2.slice(0, query2.length - 1);
    }
    if(query3.charAt(query3.length - 1) == "#"){
      query3 = query3.slice(0, query3.length - 1);
    }
    var queries = [query1, query2, query3];
    return(queries);
  }
}

