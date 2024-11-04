package com.chat.chat.domain.chat.service;

import com.chat.chat.domain.chat.model.Message;
import com.chat.chat.domain.repository.ChatRepository;
import com.chat.chat.domain.repository.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Service
public class ChatServiceV1 {

    private final ChatRepository chatRepository;

    @Transactional(transactionManager = "createChatTransacationMansger")
    public void saveChatMessage(Message msg){
        Chat chat = Chat.builder()
                .sender(msg.getFrom())
                .receiver(msg.getTo())
                .message(msg.getMessage())
                .create_at(new Timestamp(System.currentTimeMillis())).build();
        chatRepository.save(chat);
    }

}
