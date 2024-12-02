package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DAO.Response.ResponseDAO;
import ngo.spine.eigenschuldapi.DAO.Response.*;
import ngo.spine.eigenschuldapi.Model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final ResponseDAO responseDAO;
    private final ExerciseProgressService exerciseProgressService;
    private final QuestionaryDAO questionaryDAO;

    @Autowired
    public ResponseService(
        ResponseRepository responseRepository,
        ExerciseProgressService exerciseProgressService,
        QuestionaryDAO questionaryDAO,
        ResponseDAO responseDAO
    ) {
        this.responseRepository = responseRepository;
        this.exerciseProgressService = exerciseProgressService;
        this.responseDAO = responseDAO;
        this.questionaryDAO = questionaryDAO;
    }

    public Response saveResponse(Response response) {
        UUID id = this.questionaryDAO.getQuestionaryIdByQuestionId(response.getQuestion().id);
        this.exerciseProgressService.setIsDoneForExerciseProgress(id);
        response.setResponseTime(LocalDateTime.now());
        return responseDAO.save(response);
    }

    public Response updateResponse(UUID id, Response updatedResponse) {
        Optional<Response> existingResponseOptional = responseRepository.findById(id);

        if (existingResponseOptional.isPresent()) {
            Response existingResponse = existingResponseOptional.get();
            existingResponse.setResponse(updatedResponse.getResponse());

            return responseDAO.save(existingResponse);
        } else {
            throw new RuntimeException("Response not found");
        }
    }
}
