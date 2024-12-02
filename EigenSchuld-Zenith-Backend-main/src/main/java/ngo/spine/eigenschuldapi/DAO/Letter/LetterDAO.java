package ngo.spine.eigenschuldapi.DAO.Letter;

import ngo.spine.eigenschuldapi.Model.Letter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class LetterDAO {

    private final LetterRepository letterRepository;

    @Autowired
    public LetterDAO(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    public void saveLetter(Letter l) {
        this.letterRepository.save(l);
    }

    public Letter getLetterById(UUID id) throws ResponseStatusException {
        Letter foundLetter = this.letterRepository.getReferenceById(id);

        if (foundLetter.getId() == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }

        return foundLetter;
    }
}
