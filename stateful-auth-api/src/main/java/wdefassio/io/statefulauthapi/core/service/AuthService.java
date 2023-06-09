package wdefassio.io.statefulauthapi.core.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wdefassio.io.statefulauthapi.core.dto.AuthRequest;
import wdefassio.io.statefulauthapi.core.dto.AuthUserResponse;
import wdefassio.io.statefulauthapi.core.dto.TokenData;
import wdefassio.io.statefulauthapi.core.dto.TokenDto;
import wdefassio.io.statefulauthapi.core.model.User;
import wdefassio.io.statefulauthapi.core.repository.UserRepository;
import wdefassio.io.statefulauthapi.infra.exception.AuthenticationException;
import wdefassio.io.statefulauthapi.infra.exception.ValidationException;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;


    public TokenDto login(AuthRequest request) {
        var user = findByUsername(request.username());
        var accessToken = tokenService.createToken(user.getUsername());
        validatePassword(request.password(), user.getPassword());
        return new TokenDto(accessToken);
    }


    private User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ValidationException("User not found!"));
    }


    private void validatePassword(String rawPassword, String encodedPassword) {
        if (ObjectUtils.isEmpty(rawPassword)) {
            throw new ValidationException("The password must be informed!");
        }
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password is incorrect!");
        }
    }

    public AuthUserResponse getAuthenticatedUser(String accessToken) {
        TokenData tokenData = tokenService.getTokenData(accessToken);
        User user = findByUsername(tokenData.username());
        return new AuthUserResponse(user.getId(),user.getUsername());
    }

    public TokenDto validateToken(String accessToken) {
        validateExistingToken(accessToken);
        boolean isValid = tokenService.validateAccessToken(accessToken);
        if(isValid) {
            return new TokenDto(accessToken);
        }
        throw new AuthenticationException("invalid token");
    }

    public void logout(String token) {
        tokenService.deleteRedisToken(token);
    }

    private void validateExistingToken(String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new ValidationException("Access token must be informed!");
        }
    }


}
