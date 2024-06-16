package com.reservation.config.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

  private final WebSocketBrokerInterceptor interceptor;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("ws/init").setAllowedOrigins("*"); //최초 webSocket 을 연결할 때 보내는 endPoint
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(interceptor); //webSocket 이 연결되거나, client 에서 메세지를 보내게 될 때 핸들링 하게됨
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/sub"); // sub/** 형태로 구독
    registry.setApplicationDestinationPrefixes("/pub"); //반대로 메세지를 보낼 떄는 pub/** 형식으로 보내게됨
  }
}
