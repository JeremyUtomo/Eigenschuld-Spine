package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Services.UserService;

public class UserAccessFilter extends ExerciseAccessFilter {
    private final UserService userService;

    public UserAccessFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasRights(
        HttpServletRequest request,
        User requestUser,
        String[] splitRequest
    ) throws UserDoesNotHaveRightsException {
        try {
            String userIdString = splitRequest[splitRequest.length - 1];
            User user = this.userService.getUserById(userIdString);

            return this.isUserOrIsCaregiverOfClient(user, requestUser);
        } catch (Exception e) {
            throw new UserDoesNotHaveRightsException();
        }
    }
}
