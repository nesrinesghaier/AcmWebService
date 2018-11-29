import {ProblemsDetails} from "./ProblemsDetails";

export class Acmer {
  lastName: string;
  handle: string;
  email: string;
  firstName: string;
  country: string;
  rank: string;
  maxRank: string;
  rating: number;
  maxRating: number;
  solvedProblems: number;
  score:number;
  password: string;
  token: string;
  solvedProblemsDetails: ProblemsDetails[];
  role:Role;

  constructor() {
    this.handle = '';
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.password = '';
  }
}
