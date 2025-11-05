import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfrontationService, VelocistaTiemposPayload } from '../../services/confrontation.service';
import { EnfrentamientoListItemDTO } from '../../../../core/models/enfrentamiento-list-item.model';
import { TeamService } from '../../../team-registration/services/team.service';

@Component({
  selector: 'app-velocista-match-item',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './velocista-match-item.html',
})
export class VelocistaMatchItemComponent {
  @Input() matchData!: EnfrentamientoListItemDTO;
  // TODO: Podríamos pasar los nombres como @Input() para evitar llamarlos de nuevo
  
  private fb = inject(FormBuilder);
  private confrontationService = inject(ConfrontationService);

  public timesForm: FormGroup;
  public isBusy = false;
  public errorMessage: string | null = null;
  public successMessage: string | null = null;

  constructor() {
    this.timesForm = this.fb.group({
      tiempoA: [null, [Validators.required, Validators.min(0)]],
      tiempoB: [null, [Validators.required, Validators.min(0)]],
    });
  }
  
  // Lógica para saber si el match ya se corrió
  get isFinalizado(): boolean {
    return !!this.matchData.idGanador;
  }

  submitTimes(): void {
    if (this.timesForm.invalid || this.isFinalizado) return;

    this.isBusy = true;
    this.errorMessage = null;
    this.successMessage = null;
    
    const payload: VelocistaTiemposPayload = {
      tiempoEquipoA_ms: Number(this.timesForm.value.tiempoA),
      tiempoEquipoB_ms: Number(this.timesForm.value.tiempoB),
    };

    this.confrontationService.registerVelocistaTimes(this.matchData.id, payload)
      .subscribe({
        next: (updatedMatch) => {
          this.isBusy = false;
          // Actualizamos el DTO local para que muestre el ganador
          this.matchData = updatedMatch as any; // (Hacemos un cast, ya que el DTO es 'rico')
          this.successMessage = `Tiempos registrados. Ganador: ${this.matchData.idGanador}`;
          this.timesForm.disable();
        },
        error: (err) => {
          this.isBusy = false;
          this.errorMessage = "Error al registrar tiempos. " + (err.error?.message || '');
        }
      });
  }
}