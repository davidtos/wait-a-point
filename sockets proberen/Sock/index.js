var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var clients = [];

app.get('/', function(req, res){
  res.sendfile('index.html');
});

io.on('connection', function(socket){
    console.log('a user connected');

  socket.on('chat message', function(msg){
    console.log('got a msg: ' + msg);
    io.emit('chat message', msg);
    });
    
    socket.on('send to', function (msg) {
        var json = JSON.parse(msg);
        console.log('Send a msg to ' + json.to +  " payload was "  + json.message);
        io.to(clients[json.to].socket).emit('chat message', msg);
    });

    socket.on('add-user', function (msg) {
        clients[msg] = {
            "socket": socket.id
        };
        console.log(msg);
        //socket.emit('chat message', Object.keys(clients));       
    });
});

http.listen(3000, function(){
  console.log('listening on *:3000');
});