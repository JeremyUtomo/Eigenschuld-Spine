package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Model.Letter;
import ngo.spine.eigenschuldapi.Services.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/letter")
public class LetterController {

    private final LetterService letterService;
    @Autowired
    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @HasRightsToUse
    @GetMapping("/{letterId}")
    public ResponseEntity<String> getLetterById(@PathVariable String letterId) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(this.letterService.getLetterById(letterId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }

    @HasRightsToUse
    @PostMapping(value = "/{letterId}", consumes = "text/plain")
    public ResponseEntity<String> saveLetterById(@PathVariable String letterId, @RequestBody String newLetter) {
        try {
            return ResponseEntity.ok(this.letterService.saveLetterById(letterId, newLetter));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("false");
        }
    }
}
