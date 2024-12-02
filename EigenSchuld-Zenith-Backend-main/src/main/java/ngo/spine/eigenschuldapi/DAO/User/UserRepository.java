package ngo.spine.eigenschuldapi.DAO.User;

import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    List<User> findByRole(Role role);

    @Query("SELECT a FROM account a LEFT JOIN FETCH a.clients WHERE a.id = :userId")
    User findHulpverlenerWithClients(UUID userId);
}
