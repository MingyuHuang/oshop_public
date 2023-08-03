//package com.melon.configuration;
//
//import com.alibaba.druid.util.StringUtils;
//import com.melon.entity.Cart;
//import com.melon.service.CartService;
//import com.melon.service.impl.CartServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.swing.*;
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@ServerEndpoint("/websocket/{component}")
//public class WebSocketHandler {
//
//    private CartService cartService = SpringContext.getBean(CartService.class);
//    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
//
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("component") String component) {
//
//        sessionMap.put(component, session);
//
//    }
//
//    @OnClose
//    public void onClose(Session session, @PathParam("component") String component) throws Exception{
//
//        sessionMap.remove(component);
//        session.close();
//    }
//
//    @OnMessage
//    public void onMessage(String cartId) {
//
//        cartId = cartId.replace("\"", "");
//        if (!StringUtils.isEmpty(cartId) || cartId != null) {
//
//            Cart cart = cartService.getCart(cartId);
//            broadCast(cart);
//        }
//    }
//
//    private void broadCast(Cart cart) {
//
//        try {
//            for (Session session : sessionMap.values()) {
//                session.getAsyncRemote().sendObject(cart);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//}
