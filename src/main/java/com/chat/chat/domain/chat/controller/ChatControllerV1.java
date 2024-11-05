package com.chat.chat.domain.chat.controller;

import com.chat.chat.domain.auth.model.request.CreateUserRequest;
import com.chat.chat.domain.auth.model.request.LoginRequest;
import com.chat.chat.domain.auth.model.response.CreateUserReponse;
import com.chat.chat.domain.auth.model.response.LoginResponse;
import com.chat.chat.domain.auth.service.AuthService;
import com.chat.chat.domain.chat.model.response.ChatListResponse;
import com.chat.chat.domain.chat.service.ChatServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat API", description = "V1 Chat API")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatControllerV1 {

    private final ChatServiceV1 chatServiceV1;

    @Operation(
            summary = "채팅 리스트를 가져옵니다.",
            description = "가장 최근 10개의 채팅 리스트를 가져옵니다."
    )
    @GetMapping("/chat-list")
    public ChatListResponse chatList(
            @RequestParam("name") @Valid String to,
            @RequestParam("from") @Valid String from){
        return chatServiceV1.chatList(from, to);
    }

}
