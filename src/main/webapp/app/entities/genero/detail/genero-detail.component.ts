import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGenero } from '../genero.model';

@Component({
  selector: 'jhi-genero-detail',
  templateUrl: './genero-detail.component.html',
})
export class GeneroDetailComponent implements OnInit {
  genero: IGenero | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ genero }) => {
      this.genero = genero;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
