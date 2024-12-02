package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Role getUserRoleById(String id) {
        Optional<User> user = userDAO.getById(id);
        return user.map(User::getRole).orElse(null);
    }

    public User createUser(User request, Role role) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setInfix(request.getInfix());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }

    public User getUserById(String id) throws Exception {
        Optional<User> user = userDAO.getById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new Exception();
    }

    public User getUserByEmail(String email) {
        Optional<User> user = this.userDAO.getUserByEmail(email);
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));

    }
}
