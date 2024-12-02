package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/clientprogress")
public class ClientProgressController {
    private final ExerciseProgressService exerciseProgressService;

    public ClientProgressController(ExerciseProgressService exerciseProgressService) {
        this.exerciseProgressService = exerciseProgressService;
    }

    @HasRightsToUse
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserDTO>> getClientsProgressByHulpverlenerId(@PathVariable UUID userId) {
        return ResponseEntity.ok(this.exerciseProgressService.getClientsProgressByHulpverlenerId(userId));
    }

    @HasRightsToUse
    @GetMapping("client/{userId}")
    public ResponseEntity<ArrayList<ExerciseProgressDTO>> getClientProgressByClientId(@PathVariable UUID userId) {
        return ResponseEntity.ok(this.exerciseProgressService.getClientProgressByClientId(userId));
        }
}
