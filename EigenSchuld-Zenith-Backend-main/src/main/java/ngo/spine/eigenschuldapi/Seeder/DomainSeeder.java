package ngo.spine.eigenschuldapi.Seeder;

import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class DomainSeeder {

    @Autowired
    private DomainService domainService;

    public Domain seed() {
        Domain domain = new Domain();
        domain.setId(UUID.randomUUID());
        domain.setName("gmail");

        this.domainService.addNewDomain(domain);
        return domain;
    }
}
