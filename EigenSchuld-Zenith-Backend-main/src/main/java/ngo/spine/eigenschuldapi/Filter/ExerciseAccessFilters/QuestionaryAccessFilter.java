package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Model.User;

import java.util.UUID;

public class QuestionaryAccessFilter extends ExerciseAccessFilter {

    private final QuestionaryDAO questionaryDAO;

    public QuestionaryAccessFilter(QuestionaryDAO questionaryDAO) {
        this.questionaryDAO = questionaryDAO;
    }

    @Override
    public boolean hasRights(HttpServletRequest request, User requestUser, String[] splitRequest) {
        String questionaryId = splitRequest[splitRequest.length - 1];

        try {
            Questionary questionary = this.questionaryDAO.getById(UUID.fromString((questionaryId)));

            if (request.getMethod().equals("POST")) {
                return this.isCaregiverOfClient(requestUser, questionary.getUser());
            }

            return this.isUserOrIsCaregiverOfClient(questionary.getUser(), requestUser);
        } catch (UserDoesNotHaveRightsException e) {
            return false;
        }
    }
}
