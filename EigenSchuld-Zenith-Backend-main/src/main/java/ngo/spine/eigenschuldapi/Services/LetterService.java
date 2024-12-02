package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.Model.Letter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LetterService {
    private final LetterDAO letterDAO;
    private final ExerciseProgressService exerciseProgressService;

    @Autowired
    public LetterService(LetterDAO letterDAO, ExerciseProgressService exerciseProgressService) {
        this.letterDAO = letterDAO;
        this.exerciseProgressService = exerciseProgressService;
    }

    public String getLetterById(String letterId){
        UUID id = UUID.fromString(letterId);
        return this.letterDAO.getLetterById(id).getLetter();
    }

    public String saveLetterById(String letterId, String newLetter){
        UUID id = UUID.fromString(letterId);

        Letter letter = this.letterDAO.getLetterById(id);
        letter.setLetter(newLetter);

        this.letterDAO.saveLetter(letter);
        this.exerciseProgressService.setIsDoneForExerciseProgress(id);

        return letter.getLetter();
    }
}
