package wdefassio.io.statfulanyapi.core.service;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wdefassio.io.statfulanyapi.core.dto.AnyResponse;
import wdefassio.io.statfulanyapi.core.dto.AuthUserResponse;

@Service
@AllArgsConstructor
public class AnyService {

    private final TokenService tokenService;


    public AnyResponse getData(String accessToken){
        tokenService.validateToken(accessToken);
        AuthUserResponse authenticatedUser = tokenService.getAuthenticatedUser(accessToken);
        HttpStatus ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authenticatedUser);
    }


}
