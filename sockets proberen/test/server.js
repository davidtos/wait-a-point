var WebSocketServer = require('ws').Server
  , wss = new WebSocketServer({ port: 8080 });
 console.log('receivee');
 
 wss.broadcast = function(data) {
  for (var i in this.clients)
    this.clients[i].send(data);
};
 
wss.on('connection', function connection(ws) {
  ws.on('message', function incoming(message) {
    console.log('received: %s', message);
     wss.broadcast(message);
  });
 
  ws.send('something');
  
  ws.on("close", function() {
    console.log("websocket connection close")
  });
  
});
