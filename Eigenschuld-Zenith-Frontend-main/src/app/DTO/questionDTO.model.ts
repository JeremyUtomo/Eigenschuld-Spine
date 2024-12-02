import {ResponseDTO} from "../models/response-dto";

export class QuestionDTO {
    public id: string;
    public question: string;
    public orderNumber: number;
    public responses: ResponseDTO[];

    constructor(id: string, question: string, orderNumber: number, responses: ResponseDTO[]) {
        this.id = id;
        this.question = question;
        this.orderNumber = orderNumber;
        this.responses = responses;
    }
}
