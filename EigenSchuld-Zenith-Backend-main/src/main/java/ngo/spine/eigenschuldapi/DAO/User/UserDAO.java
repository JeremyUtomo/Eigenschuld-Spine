package ngo.spine.eigenschuldapi.DAO.User;

import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDAO {

    private final UserRepository userRepository;

    @Autowired
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getById(UUID id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> getById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public List<User> getAllHulpverleners() {
        return userRepository.findByRole(Role.HULPVERLENER);
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    public User getHulpverlenerWithClients(UUID userId) {
        return userRepository.findHulpverlenerWithClients(userId);
    }
}
