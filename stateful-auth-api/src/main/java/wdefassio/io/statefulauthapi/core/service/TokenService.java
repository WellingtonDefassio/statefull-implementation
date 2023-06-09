package wdefassio.io.statefulauthapi.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wdefassio.io.statefulauthapi.core.dto.TokenData;
import wdefassio.io.statefulauthapi.infra.exception.AuthenticationException;
import wdefassio.io.statefulauthapi.infra.exception.ValidationException;

import java.time.Instant;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class TokenService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Long ONE_DAY_IN_SECONDS = 86400L;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    public String createToken(String username) {
        var accessToken = UUID.randomUUID().toString();
        TokenData tokenData = new TokenData(username);
        String jsonData = getJsonData(tokenData);
        redisTemplate.opsForValue().set(accessToken, jsonData);
        redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));
        return accessToken;
    }

    public TokenData getTokenData(String token) {
        String extractToken = extractToken(token);
        String redisTokenValue = getRedisTokenValue(extractToken);
        try {
            return objectMapper.readValue(redisTokenValue, TokenData.class);
        } catch (Exception e) {
            throw new AuthenticationException("Error extracting authentication user " + e.getMessage());
        }

    }

    public boolean validateAccessToken(String token) {
        String extractToken = extractToken(token);
        String redisTokenValue = getRedisTokenValue(extractToken);
        return !ObjectUtils.isEmpty(redisTokenValue);
    }

    private String getRedisTokenValue(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteRedisToken(String token) {
        String extractToken = extractToken(token);
        redisTemplate.delete(extractToken);
    }


    private String getJsonData(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            return "";
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }


}
