import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'persona',
        data: { pageTitle: 'Personas' },
        loadChildren: () => import('./persona/persona.module').then(m => m.PersonaModule),
      },
      {
        path: 'genero',
        data: { pageTitle: 'Generos' },
        loadChildren: () => import('./genero/genero.module').then(m => m.GeneroModule),
      },
      {
        path: 'pelicula',
        data: { pageTitle: 'Peliculas' },
        loadChildren: () => import('./pelicula/pelicula.module').then(m => m.PeliculaModule),
      },
      {
        path: 'sala',
        data: { pageTitle: 'Salas' },
        loadChildren: () => import('./sala/sala.module').then(m => m.SalaModule),
      },
      {
        path: 'funcion',
        data: { pageTitle: 'Funcions' },
        loadChildren: () => import('./funcion/funcion.module').then(m => m.FuncionModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'Ventas' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'detalle-venta',
        data: { pageTitle: 'DetalleVentas' },
        loadChildren: () => import('./detalle-venta/detalle-venta.module').then(m => m.DetalleVentaModule),
      },
      {
        path: 'tarifa',
        data: { pageTitle: 'Tarifas' },
        loadChildren: () => import('./tarifa/tarifa.module').then(m => m.TarifaModule),
      },
      {
        path: 'promocion',
        data: { pageTitle: 'Promocions' },
        loadChildren: () => import('./promocion/promocion.module').then(m => m.PromocionModule),
      },
      {
        path: 'reporte',
        data: { pageTitle: 'Reportes' },
        loadChildren: () => import('./reporte/reporte.module').then(m => m.ReporteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
