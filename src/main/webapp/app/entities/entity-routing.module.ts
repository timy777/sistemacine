import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'persona',
        data: { pageTitle: 'sistemacineApp.persona.home.title' },
        loadChildren: () => import('./persona/persona.module').then(m => m.PersonaModule),
      },
      {
        path: 'genero',
        data: { pageTitle: 'sistemacineApp.genero.home.title' },
        loadChildren: () => import('./genero/genero.module').then(m => m.GeneroModule),
      },
      {
        path: 'pelicula',
        data: { pageTitle: 'sistemacineApp.pelicula.home.title' },
        loadChildren: () => import('./pelicula/pelicula.module').then(m => m.PeliculaModule),
      },
      {
        path: 'sala',
        data: { pageTitle: 'sistemacineApp.sala.home.title' },
        loadChildren: () => import('./sala/sala.module').then(m => m.SalaModule),
      },
      {
        path: 'funcion',
        data: { pageTitle: 'sistemacineApp.funcion.home.title' },
        loadChildren: () => import('./funcion/funcion.module').then(m => m.FuncionModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'sistemacineApp.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'detalle-venta',
        data: { pageTitle: 'sistemacineApp.detalleVenta.home.title' },
        loadChildren: () => import('./detalle-venta/detalle-venta.module').then(m => m.DetalleVentaModule),
      },
      {
        path: 'tarifa',
        data: { pageTitle: 'sistemacineApp.tarifa.home.title' },
        loadChildren: () => import('./tarifa/tarifa.module').then(m => m.TarifaModule),
      },
      {
        path: 'promocion',
        data: { pageTitle: 'sistemacineApp.promocion.home.title' },
        loadChildren: () => import('./promocion/promocion.module').then(m => m.PromocionModule),
      },
      {
        path: 'reporte',
        data: { pageTitle: 'sistemacineApp.reporte.home.title' },
        loadChildren: () => import('./reporte/reporte.module').then(m => m.ReporteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
