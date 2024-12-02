package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.Model.Domain;
import ngo.spine.eigenschuldapi.Services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/domain")
public class DomainController {

    private final DomainService domainService;

    @Autowired
    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping("/all")
    public List<Domain> getAllDomains() {
        return domainService.getAllDomains();
    }

    @GetMapping("/{domainId}")
    public Optional<Domain> getDomain(@PathVariable String domainId) {
        return domainService.getDomainById(domainId);
    }

    @PostMapping
    public Domain addDomain(@RequestBody Domain newDomain) {
        return domainService.addNewDomain(newDomain);
    }

    @DeleteMapping("/{domainId}")
    public ResponseEntity<Void> deleteDomain(@PathVariable String domainId) {
        return domainService.deleteDomain(domainId);
    }

    @PutMapping("/{domainId}")
    public Optional<Domain> putDomain(@PathVariable String domainId, @RequestBody Domain domain) {
        return domainService.updateDomain(domainId, domain);
    }

}
