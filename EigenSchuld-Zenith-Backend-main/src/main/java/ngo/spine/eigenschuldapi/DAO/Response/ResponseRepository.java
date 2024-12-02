package ngo.spine.eigenschuldapi.DAO.Response;

import ngo.spine.eigenschuldapi.Model.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResponseRepository extends JpaRepository<Response, UUID> {
}
