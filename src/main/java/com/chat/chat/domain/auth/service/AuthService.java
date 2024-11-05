package com.chat.chat.domain.auth.service;

import com.chat.chat.common.exception.CustomException;
import com.chat.chat.common.exception.ErrorCode;
import com.chat.chat.domain.auth.model.request.CreateUserRequest;
import com.chat.chat.domain.auth.model.request.LoginRequest;
import com.chat.chat.domain.auth.model.response.CreateUserReponse;
import com.chat.chat.domain.auth.model.response.LoginResponse;
import com.chat.chat.domain.repository.UserRepository;
import com.chat.chat.domain.repository.entity.User;
import com.chat.chat.domain.repository.entity.UserCredentials;
import com.chat.chat.security.Hasher;
import com.chat.chat.security.JWTProvider;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final Hasher hasher;

    @Transactional(transactionManager = "createUserTranscationManager")
    public CreateUserReponse createUser(CreateUserRequest request) {

        Optional<User> user = userRepository.findByName(request.name());
        if (user.isPresent()) {
            log.error("USER_ALREADY_EXISTS : {}", request.name());
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        try {
            User newUser = this.newUser(request.name());
            UserCredentials newUserCredentials = this.newUserCredentials(request.password(), newUser);
            newUser.setCredentials(newUserCredentials);
            User savedUser = userRepository.save(newUser);

            if (savedUser == null) {
                throw new CustomException(ErrorCode.USER_SAVED_FAILED);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_SAVED_FAILED);
        }

        return new CreateUserReponse(request.name());
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> user = userRepository.findByName(request.name());

        if (!user.isPresent()) {
            log.error("USER_ALREADY_EXISTS : {}", request.name());
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user.map(u -> {
            String hashedValue = hasher.getHashingValue(request.password());
            if (!u.getUserCredentials().getHashed_password().equals(hashedValue)) {
                throw new CustomException(ErrorCode.MIS_MATCH_PASSWORD);
            }
            return hashedValue;
        }).orElseThrow(() -> {
            throw new CustomException(ErrorCode.MIS_MATCH_PASSWORD);
        });

        // JWT
        String token = JWTProvider.createRefreshToken(request.name());
        return new LoginResponse(ErrorCode.SUCCESS, token);
    }

    public String getUserFromToken(String token){
        return JWTProvider.getUserFromToken(token);
    }



    private User newUser(String name) {
        User newUser = User.builder()
                .name(name)
                .created_at(new Timestamp(System.currentTimeMillis()))
                .build();
        return newUser;
    }

    private UserCredentials newUserCredentials(String password, User user) {
        // TODO Hash
        String hashedValue = hasher.getHashingValue(password);

        UserCredentials cre = UserCredentials.builder()
                .user(user)
                .hashed_password(hashedValue)
                .build();


        return cre;
    }
}
