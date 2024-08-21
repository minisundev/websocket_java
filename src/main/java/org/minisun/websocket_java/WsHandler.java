package org.minisun.websocket_java;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WsHandler extends TextWebSocketHandler {

    static int ID;
    private final Map<WebSocketSession, Integer> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established: " + session);
        sessionMap.put(session, ++ID);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        int id = sessionMap.get(session);
        TextMessage textMessage = new TextMessage(id + "번님: " + msg);

        for (WebSocketSession client : sessionMap.keySet()) {
            try {
                client.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed: " + session);
        sessionMap.remove(session);
        session.close();
    }
}