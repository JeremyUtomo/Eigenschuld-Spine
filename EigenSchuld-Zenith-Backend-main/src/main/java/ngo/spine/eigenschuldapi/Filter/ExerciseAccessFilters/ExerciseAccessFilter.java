package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Model.User;

public abstract class ExerciseAccessFilter {
    public abstract boolean hasRights(
        HttpServletRequest request,
        User requestUser,
        String[] splitRequest
    ) throws UserDoesNotHaveRightsException;

    protected boolean isUserOrIsCaregiverOfClient(User user, User requestUser) {
        boolean isUser = user.getId() == requestUser.getId();

        if (user.getHulpverlener() != null) {
            boolean isHisCaregiver = requestUser.getId() == user.getHulpverlenerId();
            return isUser || isHisCaregiver;
        }

        return isUser;
    }

    protected boolean isCaregiverOfClient(User requestUser, User questionUser) throws UserDoesNotHaveRightsException {
        boolean isHisCaregiver = questionUser.getHulpverlenerId().equals(requestUser.getId());

        if (isHisCaregiver) {
            return true;
        }

        throw new UserDoesNotHaveRightsException();
    }
}
