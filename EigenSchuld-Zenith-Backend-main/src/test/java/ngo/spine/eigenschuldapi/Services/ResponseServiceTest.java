package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DAO.Response.ResponseDAO;
import ngo.spine.eigenschuldapi.DAO.Response.ResponseRepository;
import ngo.spine.eigenschuldapi.Model.Question;
import ngo.spine.eigenschuldapi.Model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResponseServiceTest {

    @Mock
    private ResponseRepository responseRepository;

    @Mock
    private ResponseDAO responseDAO;

    @Mock
    private ExerciseProgressService exerciseProgressService;

    @Mock
    private QuestionaryDAO questionaryDAO;

    @InjectMocks
    private ResponseService responseService;

    private Response response;
    private UUID questionaryId;
    private UUID responseId;
    private Question question;

    @BeforeEach
    public void setUp() {
        responseId = UUID.randomUUID();
        questionaryId = UUID.randomUUID();
        question = new Question();
        question.setId(UUID.randomUUID());
        response = new Response();
        response.setId(responseId);
        response.setQuestion(question);
    }

    @Test
    public void testSaveResponse() {
        when(questionaryDAO.getQuestionaryIdByQuestionId(any(UUID.class))).thenReturn(questionaryId);
        when(responseDAO.save(any(Response.class))).thenReturn(response);

        Response savedResponse = responseService.saveResponse(response);

        assertNotNull(savedResponse);
        assertEquals(responseId, savedResponse.getId());
        verify(exerciseProgressService, times(1)).setIsDoneForExerciseProgress(questionaryId);
        verify(responseDAO, times(1)).save(response);
        assertNotNull(savedResponse.getResponseTime());
    }

    @Test
    public void testUpdateResponse() {
        Response updatedResponse = new Response();
        updatedResponse.setResponse("Updated response text");

        when(responseRepository.findById(any(UUID.class))).thenReturn(Optional.of(response));
        when(responseDAO.save(any(Response.class))).thenReturn(response);

        Response result = responseService.updateResponse(responseId, updatedResponse);

        assertNotNull(result);
        assertEquals(updatedResponse.getResponse(), result.getResponse());
        verify(responseRepository, times(1)).findById(responseId);
        verify(responseDAO, times(1)).save(response);
    }

    @Test
    public void testUpdateResponse_NotFound() {
        Response updatedResponse = new Response();
        updatedResponse.setResponse("Updated response text");

        when(responseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            responseService.updateResponse(responseId, updatedResponse);
        });

        assertEquals("Response not found", exception.getMessage());
        verify(responseRepository, times(1)).findById(responseId);
        verify(responseDAO, times(0)).save(any(Response.class));
    }
}
