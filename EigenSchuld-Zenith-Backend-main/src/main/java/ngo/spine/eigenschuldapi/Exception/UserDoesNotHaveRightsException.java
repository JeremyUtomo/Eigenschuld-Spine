package ngo.spine.eigenschuldapi.Exception;

public class UserDoesNotHaveRightsException extends Exception {
    public UserDoesNotHaveRightsException() {
        super("User does not have rights to request this data");
    }
}
