package wdefassio.io.statfulanyapi.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ve) {
        var details = new ExceptionDetails(HttpStatus.BAD_REQUEST.value(), ve.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException un) {
        var details = new ExceptionDetails(HttpStatus.UNAUTHORIZED.value(), un.getMessage());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

}
