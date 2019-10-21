//package com.bk.olympia.controller;
//
//import com.bk.olympia.model.message.Message;
//import com.bk.olympia.model.type.MessageType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//
//public class WebSocketEventListener {
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
//
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        int username = (int) headerAccessor.getSessionAttributes().get("username");
//        if (username != 0) {
//            logger.info("User " + username + " disconnected!");
//            Message message = new Message();
//            message.setType(MessageType.LOGOUT.getValue());
//            message.setSender(username);
//
//            messagingTemplate.convertAndSend("/topic/public", message);
//        }
//    }
//}
