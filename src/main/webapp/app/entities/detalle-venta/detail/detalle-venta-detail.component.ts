import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetalleVenta } from '../detalle-venta.model';

@Component({
  selector: 'jhi-detalle-venta-detail',
  templateUrl: './detalle-venta-detail.component.html',
})
export class DetalleVentaDetailComponent implements OnInit {
  detalleVenta: IDetalleVenta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleVenta }) => {
      this.detalleVenta = detalleVenta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
