//package com.reservation.controller;
//
//import com.reservation.dto.websocket.MessageDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class BrokerController {
//
//  private final SimpMessagingTemplate template;
//
//  @MessageMapping("/test")
//  public void test(SimpMessageHeaderAccessor accessor){
//    log.info("### test method {}", accessor);
//  }
//
//  @MessageMapping("/room/{roomId}/entered") //방에 입장했을 때
//  public void entered(@DestinationVariable(value = "roomId") String roomId, MessageDto message){
//    log.info("# roomId {}" , roomId);
//    log.info("# message {}" , message);
//
//    final String payLoad = message.getWriter() + "님이 입장하셨습니다.";
//    template.convertAndSend("/sub/room/" + roomId, payLoad);
//  }
//
//  @MessageMapping("/room/{roomId}")
//  public void sendMessage(@DestinationVariable(value = "roomId") String roomId, MessageDto message){
////    log.info("# roomId {}" , roomId);
////    log.info("# message {}" , message);
//
//    template.convertAndSend("/sub/room/" + roomId, message.getMessage());
//  }
//
//
//
//
//
//
//}
