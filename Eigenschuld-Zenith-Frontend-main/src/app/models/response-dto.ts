export class ResponseDTO {
    public response_id?: string;
    public question: { id: string };
    public response: string;
    public user: { id: string };
    public firstName?: string;
    public lastName?: string;
    public responseTime?: Date;

    constructor(response_id: string | undefined, response: string, user: { id: string }, question: { id: string }, firstName?: string, lastName?: string, responseTime?: Date) {
        this.response_id = response_id;
        this.response = response;
        this.question = question;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.responseTime = responseTime;
    }
}
