package com.chat.chat.domain.auth.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 생성 response")
public record CreateUserReponse (

        @Schema(description = "성공 유무")
        String code
){ }
