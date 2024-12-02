package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Services.*;
import ngo.spine.eigenschuldapi.DTO.UserDTO;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ExerciseProgressService exerciseProgressService;
    private final UserService userService;
    private final HulpverlenerService hulpverlenerService;

    @Autowired
    public UserController(ExerciseProgressService exerciseProgressService, UserService userService, HulpverlenerService hulpverlenerService) {
        this.exerciseProgressService = exerciseProgressService;
        this.userService = userService;
        this.hulpverlenerService = hulpverlenerService;
    }

    @HasRightsToUse
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        try {
            User user = this.userService.getUserById(userId);
            return ResponseEntity.ok(user.getDTO());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/hulpverleners/{domainId}")
    public ResponseEntity<List<HulpverlenerDTO>> getHulpverlenersByDomainId(@PathVariable String domainId) {
        List<HulpverlenerDTO> hulpverlenerDTO = hulpverlenerService.getHulpverlenersByDomainId(domainId);

        return ResponseEntity.ok(hulpverlenerDTO);
    }

    @GetMapping("/getRole/{userId}")
    public ResponseEntity<RoleDTO> getUserRoleById(@PathVariable String userId) {
        Role role = userService.getUserRoleById(userId);
        RoleDTO dto = new RoleDTO(role);
        if (role != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
