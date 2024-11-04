package com.chat.chat.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chat.chat.common.Constants.Constants;
import com.chat.chat.common.exception.CustomException;
import com.chat.chat.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 JWT를 생성, 검증 및 디코딩하는 클래스
 */
@Slf4j
@Component
public class JWTProvider {
    // JWT 서명에 사용되는 비밀 키
    private static String secretKey;
    // 리프레시 토큰 서면에 사용되는 비밀 키
    private static String refreshSecretKey;
    // 엑세스 토큰의 유효 기간
    private static long tokenTimeForMinute;
    // 리프레시 토큰의 유효 기간
    private static long refreshTokenTimeForMinute;

    @Value("${token.secret-key}")
    public void setSecretKey(String secretKey) {
        JWTProvider.secretKey = secretKey;
    }

    @Value("${token.refresh-secret-key}")
    public void setRefreshSecretKey(String refreshSecretKey) {
        JWTProvider.refreshSecretKey = refreshSecretKey;
    }

    @Value("${token.token-time}")
    public void setTokenTime(long tokenTime) {
        JWTProvider.tokenTimeForMinute = tokenTime;
    }

    @Value("${token.refresh-token-time}")
    public void setRefreshTokenTime(long refreshTokenTime) {
        JWTProvider.refreshTokenTimeForMinute = refreshTokenTime;
    }

    // 토큰 생성
    public static String createToken(String name) {
        return JWT.create()
                .withSubject(name)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenTimeForMinute + Constants.ON_MINUTE_TO_MILLIS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    // 리프레시 토큰 생성
    public static String createRefreshToken(String name) {
        return JWT.create()
                .withSubject(name)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenTimeForMinute + Constants.ON_MINUTE_TO_MILLIS))
                .sign(Algorithm.HMAC256(refreshSecretKey));
    }

    // 리프레시 토큰을 발행하기 위한 메서드
    // 기본적으로 에러가 발생해야함, 즉 토큰이 만료가 된 상태
    public static DecodedJWT checkTokenForRefresh(String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            log.error("token must be expired : { }", decoded.getSubject());
            throw  new CustomException(ErrorCode.ACCESS_TOKEN_IS_NOT_EXPIRED);

        }catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e){
            throw new CustomException(ErrorCode.TOKEN_IS_INVALID);

        }catch (TokenExpiredException e){
            return JWT.decode(token);
        }
    }
    // 엑세스 토큰 디코딩
    public static DecodedJWT decodeAccessToken(String token){
        return decodedTokenAfterVerify(token,secretKey);
    }
    // 리프레시 토큰 디코딩
    public static DecodedJWT decodeRefreshToken(String token){
        return decodedTokenAfterVerify(token,refreshSecretKey);
    }

    // 키로 토큰을 검증
    public static DecodedJWT decodedTokenAfterVerify(String token, String key) {

        try {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token);

        }catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e){
            throw new CustomException(ErrorCode.TOKEN_IS_INVALID);
        }catch (TokenExpiredException e){
            throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
        }
    }
    // 토큰을 디토딩하여 DecodedJWT로 반환
    public static DecodedJWT decodedJWT(String token) {
        return JWT.decode(token);
    }

    public static String getUserFromToken(String token){
        DecodedJWT jwt  = decodedJWT(token);
        return jwt.getSubject();
    }
}
