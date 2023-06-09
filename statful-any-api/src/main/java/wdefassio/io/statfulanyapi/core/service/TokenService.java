package wdefassio.io.statfulanyapi.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wdefassio.io.statfulanyapi.core.client.TokenClient;
import wdefassio.io.statfulanyapi.core.dto.AuthUserResponse;
import wdefassio.io.statfulanyapi.core.dto.TokenDto;
import wdefassio.io.statfulanyapi.infra.exception.AuthenticationException;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService {

    private final TokenClient tokenClient;


    public void validateToken(String token) {
        try {
            log.info("Sending request for token validation {}", token);
            TokenDto tokenDto = tokenClient.validateToken(token);
            log.info("Token is valid: {}", tokenDto.accessToken());
        } catch (Exception e) {
            log.error("Error trying validate token");
            throw new AuthenticationException("Auth error: " + e.getMessage());
        }
    }

    public AuthUserResponse getAuthenticatedUser(String token) {
        try {
            log.info("Sending request for auth user: {}", token);
            AuthUserResponse authenticatedUser = tokenClient.getAuthenticatedUser(token);
            log.info("Auth user found: {} and token: {}", authenticatedUser.toString(), token);
            return authenticatedUser;
        } catch (Exception e) {
            log.error("Error trying authenticated user");
            throw new AuthenticationException("Error to get authenticated user : " + e.getMessage());
        }
    }


}
