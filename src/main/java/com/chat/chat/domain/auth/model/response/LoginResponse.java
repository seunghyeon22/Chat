package com.chat.chat.domain.auth.model.response;

import com.chat.chat.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login 응답")
public record LoginResponse (
        @Schema(description = "error code")
        ErrorCode description,
        @Schema(description = "jwt token")
        String token

     //   @Schema(description = "성공 유무")
     //   String code
){ }