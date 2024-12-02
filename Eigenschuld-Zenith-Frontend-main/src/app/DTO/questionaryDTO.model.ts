import { QuestionDTO } from "./questionDTO.model";

export interface QuestionaryDTO {
  id: string
  questions: QuestionDTO[]
}