package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/invite")
public class InviteClientController {

    private final EmailService emailService;

    public InviteClientController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendEmail(@PathVariable UUID userId, @RequestBody EmailDTO emailDTO) {
        this.emailService.sendEmail(
            emailDTO.getEmail(),
            userId
        );
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
