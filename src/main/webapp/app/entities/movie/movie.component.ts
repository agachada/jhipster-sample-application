import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMovie } from 'app/shared/model/movie.model';
import { AccountService } from 'app/core';
import { MovieService } from './movie.service';

@Component({
  selector: 'jhi-movie',
  templateUrl: './movie.component.html'
})
export class MovieComponent implements OnInit, OnDestroy {
  movies: IMovie[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected movieService: MovieService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.movieService
      .query()
      .pipe(
        filter((res: HttpResponse<IMovie[]>) => res.ok),
        map((res: HttpResponse<IMovie[]>) => res.body)
      )
      .subscribe(
        (res: IMovie[]) => {
          this.movies = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMovies();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMovie) {
    return item.id;
  }

  registerChangeInMovies() {
    this.eventSubscriber = this.eventManager.subscribe('movieListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
