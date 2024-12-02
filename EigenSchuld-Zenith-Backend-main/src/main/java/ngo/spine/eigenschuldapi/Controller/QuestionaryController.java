package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DTO.QuestionDTO;
import ngo.spine.eigenschuldapi.DTO.QuestionaryDTO;
import ngo.spine.eigenschuldapi.DTO.ResponseDTO;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Model.Question;
import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Model.Response;
import ngo.spine.eigenschuldapi.Services.QuestionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/questionary")
public class QuestionaryController {

    private final QuestionaryService questionaryService;

    @Autowired
    public QuestionaryController(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @HasRightsToUse
    @GetMapping("/{id}")
    public QuestionaryDTO getQuestionaryById(@PathVariable UUID id) {
        return this.questionaryService.getQuestionaryById(id);
    }

    @HasRightsToUse
    @PostMapping("/{id}")
    public ResponseEntity<String> saveNewQuestionary(@PathVariable UUID id, @RequestBody QuestionaryDTO requestBody) {
        this.questionaryService.saveNewQuestionary(id, requestBody);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
