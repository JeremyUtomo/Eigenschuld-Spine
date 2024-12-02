package ngo.spine.eigenschuldapi.DAO.Domain;

import ngo.spine.eigenschuldapi.Model.Domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DomainDAO {

    private final DomainRepository domainRepository;

    @Autowired
    public DomainDAO(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    public Optional<Domain> getById(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        return domainRepository.findById(uuid);
    }

    public ResponseEntity<Void> delete(String id) {
        domainRepository.deleteById(UUID.fromString(id));
        return null;
    }

    public Optional<Domain> update(String id, Domain domainUpdate) {
        return getById(id).map(existingDomain -> {
            existingDomain.setName(domainUpdate.getName());
            existingDomain.setLogo_location(domainUpdate.getLogo_location());
            existingDomain.setPrimary_color_hex(domainUpdate.getPrimary_color_hex());

            return domainRepository.save(existingDomain);
        });
    }

    public List<String> getAllNames() {
        return domainRepository.findAllNames();
    }
}
