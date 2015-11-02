/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package something.wait_a_point;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.Observable;

/**
 *
 * @author david
 */
public class SocketMessenger extends Observable implements Runnable {

    Socket socket;
    Thread t;

    public SocketMessenger() {
        t = new Thread(this);
    }

    private String username;

    public void start(String username){
        this.username = username;
        t.start();

    }
    public void send(String message){
        socket.emit("chat message", message);
    }

    public void addUser(String message){
        socket.emit("add-user",message);
    }

    public void SendTo(String message){
        socket.emit("send to",message);
    }

    @Override
    public void run() {
        try {
            socket = IO.socket("http://145.144.240.98:3000");
            //socket = IO.socket("http://145.93.113.34:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    addUser(username);
                    socket.emit("chat message", "New User Connected");
                }

            }).on("chat message", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    setChanged();
                    notifyObservers(args);
                }

            });
            if (!socket.connected()) {
                socket.connect();
            }
        }
        catch (URISyntaxException ex) {
        }
    }
}
