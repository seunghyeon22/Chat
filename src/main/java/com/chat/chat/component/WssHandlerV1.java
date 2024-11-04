package com.chat.chat.component;

import com.chat.chat.domain.chat.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//low한 웹 소켓 통신
@Component
@RequiredArgsConstructor
public class WssHandlerV1 extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try {
            String payload = message.getPayload();
            Message msg =  objectMapper.readValue(payload, Message.class);
            // 1. DB에 있는 데이터 인지(from, to)
            // 2. 채팅 메시지 데이터 저장
            session.sendMessage(new TextMessage(payload));
        }catch (Exception e){

        }

        super.handleTextMessage(session, message);
    }
}
