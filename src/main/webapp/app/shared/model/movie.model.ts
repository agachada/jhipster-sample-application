import { IKeyword } from 'app/shared/model/keyword.model';

export interface IMovie {
  id?: string;
  title?: string;
  ids?: IKeyword[];
}

export class Movie implements IMovie {
  constructor(public id?: string, public title?: string, public ids?: IKeyword[]) {}
}
