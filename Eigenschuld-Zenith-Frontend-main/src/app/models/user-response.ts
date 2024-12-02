import { ExerciseDTO } from "./exercise-dto"

export interface UserResponse {
    id: string
    firstname: string
    infix: string
    lastname: string
    role: string
    lastlogin: string;
    userId: string;
    exerciseProgressIds: ExerciseDTO[]
}
