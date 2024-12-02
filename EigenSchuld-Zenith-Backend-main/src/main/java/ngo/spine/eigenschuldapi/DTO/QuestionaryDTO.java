package ngo.spine.eigenschuldapi.DTO;

import java.util.ArrayList;
import java.util.Set;

public class QuestionaryDTO {
    public ArrayList<QuestionDTO> questions;

    public QuestionaryDTO(ArrayList<QuestionDTO> questions) {
        this.questions = questions;
    }

    public QuestionaryDTO() { }
}
