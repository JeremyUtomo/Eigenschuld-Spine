package ngo.spine.eigenschuldapi.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Entity
public class Letter {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition="TEXT")
    @Convert(converter = AesEncrypter.class)
    private String letter;

    @OneToOne
    private User user;

    public Letter(User user) {
        this.user = user;
    }

    public Letter() {
    }

    @JsonIgnore
    public UUID getId() {
        return id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public User getUser() {
        return user;
    }
}
