import {Observable} from 'rxjs';

export interface User {
  name: string;
  picture: string;
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
  score: number;
  password: string;
  token: string;
  solvedProblemsDetails: ProblemsDetails[];
  role: Role;
}

export interface Contacts {
  user: User;
  type: string;
}

export interface RecentUsers extends Contacts {
  time: number;
}

export interface ProblemsDetails {
  id: number;
  problemIndex: string;
  solvedCount: number;
}

export abstract class UserData {
  abstract getUsers(): Observable<User[]>;

  abstract getContacts(): Observable<Contacts[]>;

  abstract getRecentUsers(): Observable<RecentUsers[]>;
}
