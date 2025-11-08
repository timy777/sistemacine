import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromocion } from '../promocion.model';

@Component({
  selector: 'jhi-promocion-detail',
  templateUrl: './promocion-detail.component.html',
})
export class PromocionDetailComponent implements OnInit {
  promocion: IPromocion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promocion }) => {
      this.promocion = promocion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
