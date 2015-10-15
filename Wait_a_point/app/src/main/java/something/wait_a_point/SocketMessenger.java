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

    public void start(){
        t.start();
    }
    public void send(String message){
        socket.emit("chat message",message);
    }

    @Override
    public void run() {
        try {
            socket = IO.socket("http://145.144.240.188:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                        socket.emit("chat message", "hi");
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
