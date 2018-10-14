var connection = new WebSocket('ws://590e2517.ngrok.io');

connection.onopen = function () {
  console.log('Connected!');
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

    // Respond to a move (indicated by a command led with 'd')
    if(string_data.charAt(0) == 'd'){

      // Parse code based on player (2 for player 1, or 1 for player 2)
      var p1 = 2;
      var p2 = 1;

      colorSpaces(string_data.slice(1, string_data.length));

    }else if(string_data.charAt(0) == 'n'){
      
    }
  };

  function colorSpaces(spaceList){
    for(var i = 0; i < 20; i++){
      changeCellColor(i);
    }
  }