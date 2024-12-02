package ngo.spine.eigenschuldapi.DTO;

import ngo.spine.eigenschuldapi.Model.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class QuestionDTO {
    public UUID id;
    public String question;
    public int orderNumber;
    public ArrayList<ResponseDTO> responses;

    public QuestionDTO() {}

    public QuestionDTO(UUID id, String question, int orderNumber) {
        this.id = id;
        this.question = question;
        this.orderNumber = orderNumber;
        this.responses = new ArrayList<>();
    }

    public QuestionDTO(UUID id, String question, int orderNumber, ArrayList<ResponseDTO> responses) {
        this.id = id;
        this.question = question;
        this.orderNumber = orderNumber;
        this.responses = responses;
    }
}
