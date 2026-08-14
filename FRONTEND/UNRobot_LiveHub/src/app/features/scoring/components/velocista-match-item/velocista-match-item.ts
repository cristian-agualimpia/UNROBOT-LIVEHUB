import { Component, Input, inject, OnInit } from '@angular/core'; // 1. Importar OnInit
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfrontationService, VelocistaTiemposPayload } from '../../services/confrontation.service';
import { EnfrentamientoListItemDTO } from '../../../../core/models/enfrentamiento-list-item.model';

// --- 2. IMPORTAR SERVICIOS Y AYUDANTES ---
import { TeamService, EquipoDTO } from '../../../team-registration/services/team.service'; 
import { forkJoin, of } from 'rxjs'; 

@Component({
  selector: 'app-velocista-match-item',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './velocista-match-item.html',
})
export class VelocistaMatchItemComponent implements OnInit { // 3. Implementar OnInit
  @Input() matchData!: EnfrentamientoListItemDTO;
  
  private fb = inject(FormBuilder);
  private confrontationService = inject(ConfrontationService);
  private teamService = inject(TeamService); // 4. Inyectar TeamService

  public timesForm: FormGroup;
  public isBusy = false;
  public errorMessage: string | null = null;
  public successMessage: string | null = null;

  // --- 5. AÑADIR PROPIEDADES PARA LOS NOMBRES ---
  public teamAName: string = 'Cargando...';
  public teamBName: string = 'Cargando...';

  constructor() {
    this.timesForm = this.fb.group({
      tiempoA: [null, [Validators.required, Validators.min(0)]],
      tiempoB: [null, [Validators.required, Validators.min(0)]],
    });
  }

  // --- 6. AÑADIR MÉTODO ngOnInit ---
  ngOnInit(): void {
    this.loadTeamNames();
    if (this.isFinalizado) {
      // Si el match ya terminó, deshabilitar el formulario
      this.timesForm.disable();
    }
  }

  // --- 7. AÑADIR MÉTODO loadTeamNames ---
  loadTeamNames(): void {
    const teamA$ = this.matchData.idEquipoA 
      ? this.teamService.getTeamById(this.matchData.idEquipoA) 
      : of(null as EquipoDTO | null); // 'of(null)' si no hay equipo (ej. BYE)
      
    const teamB$ = this.matchData.idEquipoB 
      ? this.teamService.getTeamById(this.matchData.idEquipoB)
      : of(null as EquipoDTO | null);

    // forkJoin ejecuta ambas llamadas en paralelo
    forkJoin([teamA$, teamB$]).subscribe(([teamA, teamB]) => {
      this.teamAName = teamA?.nombre || 'Equipo Pendiente';
      this.teamBName = teamB?.nombre || 'Equipo Pendiente';
    });
  }
  
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
          // Actualizamos el estado local del match
          this.matchData.idGanador = updatedMatch.idGanador; 
          this.successMessage = `Tiempos registrados.`;
          this.timesForm.disable(); // Deshabilitamos al guardar
        },
        error: (err) => {
          this.isBusy = false;
          this.errorMessage = "Error al registrar tiempos. " + (err.error?.message || '');
        }
      });
  }
}