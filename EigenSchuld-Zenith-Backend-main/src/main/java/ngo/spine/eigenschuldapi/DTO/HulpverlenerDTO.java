package ngo.spine.eigenschuldapi.DTO;

import ngo.spine.eigenschuldapi.Model.*;

import java.util.*;

public class HulpverlenerDTO {
    public UUID userId;
    public String firstName;
    public String infix;
    public String lastName;

    public HulpverlenerDTO(UUID userId, String firstName, String infix, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
    }

    public HulpverlenerDTO() {

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
