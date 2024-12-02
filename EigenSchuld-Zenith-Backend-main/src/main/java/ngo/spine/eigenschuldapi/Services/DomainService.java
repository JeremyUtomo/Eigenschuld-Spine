package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.DomainDAO;
import ngo.spine.eigenschuldapi.Model.Domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DomainService {
    private final DomainDAO domainDAO;

    @Autowired
    public DomainService(DomainDAO domainDAO) {
        this.domainDAO = domainDAO;
    }

    public List<Domain> getAllDomains() {
        return domainDAO.getAllDomains();
    }

    public Optional<Domain> getDomainById(String id) {
        return domainDAO.getById(id);
    }

    public Domain addNewDomain(Domain newDomain) {
        return domainDAO.save(newDomain);
    }

    public ResponseEntity<Void> deleteDomain(String id) {
        return domainDAO.delete(id);
    }

    public Optional<Domain> updateDomain(String id, Domain domain) {
        return domainDAO.update(id, domain);
    }

}
