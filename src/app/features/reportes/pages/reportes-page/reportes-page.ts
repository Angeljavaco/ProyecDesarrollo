import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { Clase } from '../../../clases/models/clase';
import { ClaseService } from '../../../clases/services/clase';
import { ReporteClase } from '../../models/reporte';
import { ReporteService } from '../../services/reporte';

@Component({
  selector: 'app-reportes-page',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './reportes-page.html'
})
export class ReportesPage implements OnInit {
  private readonly claseService = inject(ClaseService);
  private readonly reporteService = inject(ReporteService);
  private readonly cdr = inject(ChangeDetectorRef);

  clases: Clase[] = [];
  reporte: ReporteClase | null = null;

  isLoadingClases = false;
  isLoadingReporte = false;

  errorMessage = '';

  ngOnInit(): void {
    this.cargarMisClases();
  }

  cargarMisClases(): void {
    this.isLoadingClases = true;
    this.errorMessage = '';

    this.claseService.listarMisClases().subscribe({
      next: (clases) => {
        this.clases = clases;
        this.isLoadingClases = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(
          'ERROR AL CARGAR LAS CLASES DEL TRAINER:',
          error
        );

        this.errorMessage =
          error?.error?.error ??
          'No se pudieron cargar tus clases.';

        this.isLoadingClases = false;
        this.cdr.detectChanges();
      }
    });
  }

  verReporte(claseId: number): void {
    this.isLoadingReporte = true;
    this.errorMessage = '';
    this.reporte = null;

    this.reporteService
      .obtenerInscritosPorClase(claseId)
      .subscribe({
        next: (reporte) => {
          this.reporte = reporte;
          this.isLoadingReporte = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error(
            'ERROR AL GENERAR EL REPORTE:',
            error
          );

          this.errorMessage =
            error?.error?.error ??
            'No se pudo generar el reporte de inscritos.';

          this.isLoadingReporte = false;
          this.cdr.detectChanges();
        }
      });
  }

  limpiarReporte(): void {
    this.reporte = null;
    this.errorMessage = '';
  }
}