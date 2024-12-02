package ngo.spine.eigenschuldapi.DTO;

import ngo.spine.eigenschuldapi.Model.Role;

import java.util.*;

public class UserDTO {
    public UUID userId;
    public String firstname;
    public String infix;
    public String lastname;
    public Role role;
    public Date lastlogin;
    public ArrayList<ExerciseProgressDTO> exerciseProgressIds;

    public UserDTO(
        UUID userId,
        String firstname,
        String infix,
        String lastname,
        Role role,
        Date lastlogin,
        ArrayList<ExerciseProgressDTO> exerciseIds
    ) {
        this.userId = userId;
        this.firstname = firstname;
        this.infix = infix;
        this.lastname = lastname;
        this.role = role;
        this.lastlogin = lastlogin;
        this.exerciseProgressIds = exerciseIds;
    }
}
