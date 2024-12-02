package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Component
public class RegisterErrorService {

    public void validateUserDoesNotExist(Optional<User> existingUser) {
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }

    public void validatePassword(String password) {
        if (!checkPassword(password)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Password does not meet the requirements");
        }
    }

    private Boolean checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean isAtLeast8 = password.length() >= 8;

        return hasDigit && isAtLeast8;
    }
}
