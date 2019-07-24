import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMovie, Movie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';
import { IKeyword } from 'app/shared/model/keyword.model';
import { KeywordService } from 'app/entities/keyword';

@Component({
  selector: 'jhi-movie-update',
  templateUrl: './movie-update.component.html'
})
export class MovieUpdateComponent implements OnInit {
  isSaving: boolean;

  keywords: IKeyword[];

  editForm = this.fb.group({
    id: [],
    title: [],
    ids: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected movieService: MovieService,
    protected keywordService: KeywordService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ movie }) => {
      this.updateForm(movie);
    });
    this.keywordService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IKeyword[]>) => mayBeOk.ok),
        map((response: HttpResponse<IKeyword[]>) => response.body)
      )
      .subscribe((res: IKeyword[]) => (this.keywords = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(movie: IMovie) {
    this.editForm.patchValue({
      id: movie.id,
      title: movie.title,
      ids: movie.ids
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const movie = this.createFromForm();
    if (movie.id !== undefined) {
      this.subscribeToSaveResponse(this.movieService.update(movie));
    } else {
      this.subscribeToSaveResponse(this.movieService.create(movie));
    }
  }

  private createFromForm(): IMovie {
    return {
      ...new Movie(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      ids: this.editForm.get(['ids']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovie>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackKeywordById(index: number, item: IKeyword) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
