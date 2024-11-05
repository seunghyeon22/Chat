package com.chat.chat.domain.chat.service;

import com.chat.chat.domain.chat.model.Message;
import com.chat.chat.domain.chat.model.response.ChatListResponse;
import com.chat.chat.domain.repository.ChatRepository;
import com.chat.chat.domain.repository.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatServiceV1 {

    private final ChatRepository chatRepository;

    public ChatListResponse chatList(String from, String to){
        List<Chat> chats = chatRepository.findTop10BySenderOrReceiverOrderByTIDDesc(from, to);
        List<Chat> chatss = chatRepository.findTop10BySenderOrReceiverOrderByTIDDesc(to, from);

        chats.addAll(chatss);

        Collections.sort(chats, new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                return o1.getCreated_at().compareTo(o2.getCreated_at());
            }
        });

        List<Message> res = chats.stream()
                .map(
                chat -> new Message(chat.getReceiver(), chat.getSender(), chat.getMessage())
        ).collect(Collectors.toList());
        return new ChatListResponse(res);
    }

    @Transactional(transactionManager = "createChatTransacationMansger")
    public void saveChatMessage(Message msg){
        Chat chat = Chat.builder()
                .sender(msg.getFrom())
                .receiver(msg.getTo())
                .message(msg.getMessage())
                .created_at(new Timestamp(System.currentTimeMillis())).build();
        chatRepository.save(chat);
    }

}
