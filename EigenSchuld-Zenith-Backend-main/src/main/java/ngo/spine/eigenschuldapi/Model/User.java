package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.*;

@Entity(name = "account")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    @Convert(converter = AesEncrypter.class)
    private String firstName;

    @Column(name = "last_name")
    @Convert(converter = AesEncrypter.class)
    private String lastName;

    @Column(name = "infix")
    @Convert(converter = AesEncrypter.class)
    private String infix;

    @Column(name = "email")
    @Convert(converter = AesEncrypter.class)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "domain_id")
    private UUID domainId;

    @Column(name = "last_login")
    private Date lastLogin;

    @ManyToOne
    @JoinColumn(name = "hulpverlener_id")
    private User hulpverlener;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public List<ExerciseProgress> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "hulpverlener", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> clients = new ArrayList<>();


    public User() { }

    public void addExercise(ExerciseProgress exerciseProgress) {
        this.exercises.add(exerciseProgress);
    }

    public ArrayList<ExerciseProgressDTO> fetchExerciseIds() {
        ExerciseProgressDTO[] exercisDTOs = new ExerciseProgressDTO[5];

        for(ExerciseProgress exerciseProgress : exercises) {
            String[] split = exerciseProgress.exerciseData.name.split(" ");
            int id = Integer.parseInt(split[1])-1;

            exercisDTOs[id] = exerciseProgress.getDTO();
        }

        return new ArrayList<>(List.of(exercisDTOs));
    }

    public UserDTO getDTO() {
        return new UserDTO(
            this.getId(),
            this.getFirstName(),
            this.getInfix(),
            this.getLastName(),
            this.getRole(),
            this.getLastLogin(),
            this.fetchExerciseIds()
        );
    }

    public HulpverlenerDTO getHulpverlenerDTO() {
        return new HulpverlenerDTO(
            this.getId(),
            this.getFirstName(),
            this.getInfix(),
            this.getLastName()
        );
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public void setDomainId(UUID domainId) {
        this.domainId = domainId;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        this.clients = clients;
    }

    public User getHulpverlener() {
        return hulpverlener;
    }

    public UUID getHulpverlenerId() {
        return hulpverlener.getId();
    }

    public void setHulpverlener(User hulpverlener) {
        this.hulpverlener = hulpverlener;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(role.name()));
        return list;
    }
}
