/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.springcookbook.mavenproject1;

/**
 *
 * @author david
 */
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestApp {

    public static void main(String[] args) {

//        new Thread(
//                new Runnable() {
//
//                    public void run() {
//                        try {
//                            // open websocket
//                            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://145.144.242.141:8080"));
//
//                            // add listener
//                            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
//                                public void handleMessage(String message) {
//                                    System.out.println(message);
//                                }
//                            });
//
//                            // send message to websocket
//                            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");
//
//                            // wait 5 seconds for messages from websocket
//                            Thread.sleep(5000);
//
//                        }
//                        catch (InterruptedException ex) {
//                            System.err.println("InterruptedException exception: " + ex.getMessage());
//                        }
//                        catch (URISyntaxException ex) {
//                            System.err.println("URISyntaxException exception: " + ex.getMessage());
//                        }
//                    }
//                }).start();
        try {
            Socket socket = IO.socket("http://145.144.242.141:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        Socket socket = IO.socket("http://145.144.242.141:3000");
                        socket.emit("chat message", "hi");
                        //socket.disconnect();
                    }
                    catch (URISyntaxException ex) {
                        Logger.getLogger(TestApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();
           // socket.send("chat message","sup sup");
        }
        catch (URISyntaxException ex) {
            Logger.getLogger(TestApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
