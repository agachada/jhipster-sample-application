import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IKeyword, Keyword } from 'app/shared/model/keyword.model';
import { KeywordService } from './keyword.service';
import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from 'app/entities/movie';

@Component({
  selector: 'jhi-keyword-update',
  templateUrl: './keyword-update.component.html'
})
export class KeywordUpdateComponent implements OnInit {
  isSaving: boolean;

  movies: IMovie[];

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected keywordService: KeywordService,
    protected movieService: MovieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ keyword }) => {
      this.updateForm(keyword);
    });
    this.movieService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMovie[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMovie[]>) => response.body)
      )
      .subscribe((res: IMovie[]) => (this.movies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(keyword: IKeyword) {
    this.editForm.patchValue({
      id: keyword.id,
      name: keyword.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const keyword = this.createFromForm();
    if (keyword.id !== undefined) {
      this.subscribeToSaveResponse(this.keywordService.update(keyword));
    } else {
      this.subscribeToSaveResponse(this.keywordService.create(keyword));
    }
  }

  private createFromForm(): IKeyword {
    return {
      ...new Keyword(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKeyword>>) {
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

  trackMovieById(index: number, item: IMovie) {
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
