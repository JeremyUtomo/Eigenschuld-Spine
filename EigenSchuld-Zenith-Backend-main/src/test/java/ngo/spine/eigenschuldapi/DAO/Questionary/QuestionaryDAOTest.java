package ngo.spine.eigenschuldapi.DAO.Questionary;

import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Services.ExerciseProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionaryDAOTest {

    public QuestionaryDAO SUT;

    @Mock QuestionaryRepository questionaryRepository;

    @BeforeEach
    public void init() {
        this.SUT = new QuestionaryDAO(questionaryRepository);
    }

    @Test
    void should_return_questionary_with_id() {
        Optional<Questionary> expected = Optional.of(new Questionary());
        expected.get().id = UUID.randomUUID();

        when(questionaryRepository.findById(any(UUID.class)))
            .thenReturn(expected);

        Questionary result = this.SUT.getById(expected.get().id);

        assertEquals(expected.get().id, result.id);
    }

    @Test
    void should_throw_when_id_is_not_found() {
        Optional<Questionary> expected = Optional.empty();

        when(questionaryRepository.findById(any(UUID.class)))
            .thenReturn(expected);

        RuntimeException thrown = assertThrows(
            RuntimeException.class,
            () -> this.SUT.getById(UUID.randomUUID()),
            "404 NOT_FOUND"
        );

        System.out.println(thrown);

        assertTrue(thrown.getMessage().contains("404 NOT_FOUND"));
    }
}
