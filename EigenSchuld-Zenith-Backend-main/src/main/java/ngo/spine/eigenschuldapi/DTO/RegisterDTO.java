package ngo.spine.eigenschuldapi.DTO;

public class RegisterDTO {
    private String firstName;
    private String infix;
    private String lastName;
    private String password;
    private String email;
    private String inviteToken;

    public RegisterDTO(
        String firstname,
        String infix,
        String lastname,
        String password,
        String email,
        String invitetoken
    ) {
        this.firstName = firstname;
        this.infix = infix;
        this.lastName = lastname;
        this.password = password;
        this.email = email;
        this.inviteToken = invitetoken;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }
}
