package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.Model.Letter;
import ngo.spine.eigenschuldapi.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LetterAccessFilter extends ExerciseAccessFilter {

    private final LetterDAO letterDAO;

    @Autowired
    public LetterAccessFilter(LetterDAO letterDAO) {
        this.letterDAO = letterDAO;
    }

    @Override
    public boolean hasRights(HttpServletRequest request, User requestUser, String[] splitRequest) {
        try {
            Letter letter = this.letterDAO.getLetterById(UUID.fromString(splitRequest[4]));
            return this.isUserOrIsCaregiverOfClient(letter.getUser(), requestUser);
        } catch (Exception e) {
            return false;
        }
    }
}
