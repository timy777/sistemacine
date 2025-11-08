import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPelicula } from '../pelicula.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-pelicula-detail',
  templateUrl: './pelicula-detail.component.html',
})
export class PeliculaDetailComponent implements OnInit {
  pelicula: IPelicula | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      this.pelicula = pelicula;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
