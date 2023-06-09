package wdefassio.io.statefulauthapi.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wdefassio.io.statefulauthapi.core.dto.AuthRequest;
import wdefassio.io.statefulauthapi.core.dto.AuthUserResponse;
import wdefassio.io.statefulauthapi.core.dto.TokenDto;
import wdefassio.io.statefulauthapi.core.service.AuthService;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public TokenDto login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/token/validate")
    public TokenDto validateToken(@RequestHeader String accessToken) {
        return authService.validateToken(accessToken);
    }

    @PostMapping("/logout")
    public HashMap<String, Object> logout(@RequestHeader String accessToken) {
        authService.logout(accessToken);
        HashMap<String, Object> response = new HashMap<>();
        HttpStatus ok = HttpStatus.OK;
        response.put("status", ok.name());
        response.put("code", ok.value());
        return response;
    }

    @GetMapping("user")
    public AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken) {
        return authService.getAuthenticatedUser(accessToken);
    }

}
