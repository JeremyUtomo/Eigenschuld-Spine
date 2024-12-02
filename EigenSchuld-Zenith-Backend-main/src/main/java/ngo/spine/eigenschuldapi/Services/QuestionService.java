package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DAO.Response.ResponseRepository;
import ngo.spine.eigenschuldapi.Model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {

    private final ResponseRepository responseRepository;
    private final ExerciseProgressService exerciseProgressService;
    private final QuestionaryDAO questionaryDAO;

    @Autowired
    public QuestionService(ResponseRepository responseRepository, ExerciseProgressService exerciseProgressService, QuestionaryDAO questionaryDAO) {
        this.responseRepository = responseRepository;
        this.exerciseProgressService = exerciseProgressService;
        this.questionaryDAO = questionaryDAO;
    }

    public Response save(Response response) {
        UUID id = this.questionaryDAO.getQuestionaryIdByQuestionId(response.getQuestion().id);
        this.exerciseProgressService.setIsDoneForExerciseProgress(id);

        response.setResponseTime(LocalDateTime.now());
        return responseRepository.save(response);
    }

    public void delete(UUID id) {
        responseRepository.deleteById(id);
    }

    public Response updateResponse(UUID id, Response updatedResponse) {
        Optional<Response> existingResponseOptional = responseRepository.findById(id);

        if (existingResponseOptional.isPresent()) {
            Response existingResponse = existingResponseOptional.get();
            existingResponse.setResponse(updatedResponse.getResponse());

            return responseRepository.save(existingResponse);
        } else {
            throw new RuntimeException("Response not found");
        }
    }
}
