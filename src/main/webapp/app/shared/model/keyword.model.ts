import { IMovie } from 'app/shared/model/movie.model';

export interface IKeyword {
  id?: string;
  name?: string;
  ids?: IMovie[];
}

export class Keyword implements IKeyword {
  constructor(public id?: string, public name?: string, public ids?: IMovie[]) {}
}
