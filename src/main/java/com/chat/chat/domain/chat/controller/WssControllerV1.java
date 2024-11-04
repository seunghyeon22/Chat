package com.chat.chat.domain.chat.controller;

import com.chat.chat.domain.chat.model.Message;
import com.chat.chat.domain.chat.service.ChatServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WssControllerV1 {
    private final ChatServiceV1 chatServiceV1;

    @MessageMapping("/chat/message/{from}")
    @SendTo("/sub/chat")
    public Message receivedMessage(
            @DestinationVariable String from, Message msg
    ){
        log.info("Message Received -> From : {}, to : {}, msg :{}", from, msg.getTo(), msg.getFrom());
        //TODO -> save
        chatServiceV1.saveChatMessage(msg);
        return msg;
    }


}
