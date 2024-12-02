package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.Model.Domain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AesEncrypterTest {

    @Autowired
    private DomainService domainService;


    @Test
    void insertData() {
        Domain domain = new Domain();
        domain.setId(UUID.fromString("e486538c-0b0a-4d10-9ffc-7489b5b3037d"));
        domain.setName("Test");
        domain.setLogo_location("test");
        domain.setPrimary_color_hex("Test");

        this.domainService.addNewDomain(domain);
    }

    @Test
    void retrieveData() {
        Optional<Domain> domain = domainService.getDomainById("e486538c-0b0a-4d10-9ffc-7489b5b3037d");

        System.out.println(domain);
    }
}
