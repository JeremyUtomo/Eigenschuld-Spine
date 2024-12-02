package ngo.spine.eigenschuldapi.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserDoesNotHaveRightsException.class)
    public ResponseEntity<String> handleUserDoesNotHaveRightsException() {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("You dont have rights to access this endpoint");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNameNotFound() {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Could not find user for this JWT");
    }
}
