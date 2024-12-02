package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;

import java.util.*;

@Entity
public class Question implements Comparable<Question> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public UUID id;

    @Convert(converter = AesEncrypter.class)
    public String question;
    public int orderNumber;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    public Set<Response> responses = new TreeSet<>();

    public Question(String question, int orderNumber) {
        this.question = question;
        this.orderNumber = orderNumber;
    }

    public Question() {}

    @Override
    public int compareTo(Question o) {
        if(o.orderNumber == this.orderNumber) {
            return 0;
        } else if (this.orderNumber > o.orderNumber) {
            return 1;
        }

        return -1;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
