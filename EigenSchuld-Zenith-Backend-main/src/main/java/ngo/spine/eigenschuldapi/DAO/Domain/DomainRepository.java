package ngo.spine.eigenschuldapi.DAO.Domain;

import ngo.spine.eigenschuldapi.Model.Domain;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface DomainRepository extends JpaRepository<Domain, UUID> {
    @Query("SELECT d.name FROM Domain d")
    List<String> findAllNames();
}
