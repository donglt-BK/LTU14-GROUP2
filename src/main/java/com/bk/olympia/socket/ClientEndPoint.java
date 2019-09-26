package com.bk.olympia.socket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class ClientEndPoint {
    Session userSession = null;
    private MessageHandler messageHandler;

    public ClientEndPoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("opening websocket");
        userSession = session;

        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            String name = "Duke";
            System.out.println("Sending message to endpoint: " + name);
            session.getBasicRemote().sendText(name);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("closing websocket cause by code " + reason.getCloseCode());
        userSession = null;
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}
