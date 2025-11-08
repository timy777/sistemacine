import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITarifa } from '../tarifa.model';

@Component({
  selector: 'jhi-tarifa-detail',
  templateUrl: './tarifa-detail.component.html',
})
export class TarifaDetailComponent implements OnInit {
  tarifa: ITarifa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarifa }) => {
      this.tarifa = tarifa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
