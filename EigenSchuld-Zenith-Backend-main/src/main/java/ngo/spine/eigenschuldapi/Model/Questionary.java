package ngo.spine.eigenschuldapi.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Questionary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public UUID id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "questionary_question",
        joinColumns = @JoinColumn(name = "questionary_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id"))
    public Set<Question> questions = new TreeSet<>(Comparator.naturalOrder());

    @ManyToOne
    private User user;

    public Questionary() {}

    public Questionary(User user) {
        this.user = user;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }
}
