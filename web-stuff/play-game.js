var connection = new WebSocket('ws://995bcfe6.ngrok.io');
var player = "";
var availableSpaces = "01234";
var victory = -1;
var turn = 1;

// Parse code based on player (2 for player 1, or 1 for player 2)
var p1 = 2;
var p2 = 1;
var possibleTypes = ["human", "random", "aw", "minimax", "pruned-mm"]
var playerTypeList = parseURL();
var p1Type = playerTypeList[1];
var p2Type = playerTypeList[2];
var waiting = (p1 == "human" && p2 == "human");

connection.onopen = function (e) {
  console.log('Connected!');
  connection.send("E");
};

// Log errors
connection.onerror = function (error) {
  console.log('WebSocket Error ' + error);
};

// Log messages from the server
connection.onmessage = function (e) {
  console.log('Server: ' + e.data);
  var string_data = e.data;
  var first_char = string_data.charAt(0);

  if(first_char == 'x'){
    waiting = false;
  }
  else if(first_char == 'p'){
    player = e.data.charAt(1);
    var playerLetter = player==1?"A":"B";
    connection.send(playerLetter + "human");
    console.log("Player Letter has been sent: " + playerLetter + "human");
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

};

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

  // Player 1, if a human, must wait until player 2 appears
  if(waiting){
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
function parseURL(parameter){
  var decoded = decodeURIComponent(window.location.search);
  var queryString = decoded.substring(1);
  var queries = queryString.split("&");

  // Make sure that there is the right number and type of queries
  if(queries.length != 2 || queries[0].slice(0,2) != "p1" || queries[1].slice(0,2) != "p2"){
    alert("There seems to be something wrong with the URL. We will assume that both players are human.");
    return(["human", "human"]);
  }

  // Make sure that both players are valid types
  if(!possibleTypes.includes(queries[0].slice(3)) || !possibleTypes.includes(queries[1].slice(3))){
    alert("A player seems to be of an invalid type. We will assume that both players are human.");
    return(["human", "human"]);
  }
  else{
    return([queries[0].slice(3), queries[1].slice(3)]);
  }
}

