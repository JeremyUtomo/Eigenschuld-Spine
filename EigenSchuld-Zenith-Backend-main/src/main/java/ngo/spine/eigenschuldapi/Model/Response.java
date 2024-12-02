package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;
import ngo.spine.eigenschuldapi.DTO.ResponseDTO;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Response implements Comparable<Response> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public UUID id;

    @Convert(converter = AesEncrypter.class)
    public String response;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "question_id")
    public Question question;

    public Response(String response, User user) {
        this.response = response;
        this.user = user;
        this.responseTime = LocalDateTime.now();
    }

    public Response(ResponseDTO responseDTO, User user, Question question) {
        this.id = responseDTO.response_id;
        this.responseTime = responseDTO.responseTime;
        this.response = responseDTO.response;
    }

    public Response() {
        this.responseTime = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }
  
    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public int compareTo(Response o) {
        return this.responseTime.compareTo(o.responseTime);
    }
}
