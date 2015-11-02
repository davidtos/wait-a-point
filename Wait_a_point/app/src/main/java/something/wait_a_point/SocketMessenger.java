/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package something.wait_a_point;

import com.google.gson.Gson;

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
            socket = IO.socket("http://192.168.2.13:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    addUser(username);

                    //socket.emit("chat message", "New User Connected");

                    // let everyone know new user joined
                    SendAll sendToAll = new SendAll("NewUser",username,"New user joined : "+ username);

                    Gson gson = new Gson();
                    String sendToInString = gson.toJson(sendToAll);
                    send(sendToInString);
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
