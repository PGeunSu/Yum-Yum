package com.reservation.dto.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {

  private final Map<String, WebSocketSession> sessions = new HashMap<>();

  //최초 연결 시
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    final String sessionId = session.getId();
    final String enteredMessage = sessionId + "님이 입장하셨습니다.";
    sessions.put(sessionId, session);

    sessions.values().forEach((s) -> {
      try {
        if (!s.getId().equals(sessionId) && s.isOpen()) {
          s.sendMessage(new TextMessage(enteredMessage));
        }
      } catch (IOException e) {
      }
    });
  }

  //연결 후 메세지를 주고 받을 때 call
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    final String sessionId = session.getId();
    sessions.values().forEach((s) -> {
      if (!s.getId().equals(sessionId) && s.isOpen()) {
        try {
          s.sendMessage(message);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  //웹소켓이 끊기면 call
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    final String sessionId = session.getId();
    final String leaveMessage = sessionId + "님이 퇴장하였습니다.";
    sessions.remove(sessionId); //삭제

    //메세지 전송
    sessions.values().forEach((s) -> {
      if (!s.getId().equals(sessionId) && s.isOpen()) {
        try {
          s.sendMessage(new TextMessage(leaveMessage));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  //통신 에러 발생 시 call
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
  }

}
