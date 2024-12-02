package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Model.User;

import java.util.UUID;

public class ResponseAccessFilter extends ExerciseAccessFilter {

    private final QuestionaryDAO questionaryDAO;

    public ResponseAccessFilter(QuestionaryDAO questionaryDAO) {
        this.questionaryDAO = questionaryDAO;
    }

    @Override
    public boolean hasRights(
        HttpServletRequest request,
        User requestUser,
        String[] splitRequest
    ) throws UserDoesNotHaveRightsException {
        String questionaryId = splitRequest[splitRequest.length - 1];
        Questionary questionary = this.questionaryDAO.getById(UUID.fromString((questionaryId)));
        return this.isUserOrIsCaregiverOfClient(questionary.getUser(), requestUser);
    }
}
