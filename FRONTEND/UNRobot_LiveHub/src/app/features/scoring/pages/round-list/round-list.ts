import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, Observable } from 'rxjs'; 
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

// Modelos
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';
import { EnfrentamientoListItemDTO } from '../../../../core/models/enfrentamiento-list-item.model';
import { EquipoDTO } from '../../../../core/models/equipo.model'; 

// Servicios
import { ConfrontationService } from '../../services/confrontation.service';
import { IndividualRoundService, CreateRondaIndividualDTO } from '../../services/individual-round.service';
import { TeamService } from '../../../team-registration/services/team.service'; 

// Componentes
import { ConfrontationListItemComponent } from '../../components/confrontation-list-item/confrontation-list-item';
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';

@Component({
  selector: 'app-round-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule, 
    ConfrontationListItemComponent,
    BackButtonComponent
  ],
  templateUrl: './round-list.html',
  styleUrls: ['./round-list.css']
})
export class RoundListComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder); 
  private confrontationService = inject(ConfrontationService);
  private individualRoundService = inject(IndividualRoundService);
  private teamService = inject(TeamService); 

  public categoryType: 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL' | null = null;
  public categoryId: string | null = null;
  
  public matches: EnfrentamientoListItemDTO[] = [];
  public dataLoaded = false; 

  public createRoundForm: FormGroup;
  public teams$!: Observable<EquipoDTO[]>; 
  
  public isBusy = false; 
  public errorMessage: string | null = null;

  constructor() {
    this.createRoundForm = this.fb.group({
      idEquipo: ['', Validators.required],
      tiempoMs: [0, [Validators.required, Validators.min(0)]],
      penalizaciones: [0, [Validators.required, Validators.min(0)]],
      etiquetaRonda: ['Clasificatoria-1', Validators.required]
    });
  }

  ngOnInit(): void {
    this.categoryId = this.route.snapshot.paramMap.get('categoryId');
    const type = this.route.snapshot.paramMap.get('categoryType');
    
    if (!this.categoryId || !type) {
      this.errorMessage = "Error: No se encontró el ID o tipo de categoría.";
      return;
    }
    
    this.categoryType = type as 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL';

    if (this.categoryType === 'RONDA_INDIVIDUAL') {
      this.teams$ = this.teamService.getTeamsByCategory(this.categoryId!);
    }
  }

  // --- Métodos de Enfrentamiento ---
  public loadMatches(): void { 
    if (!this.categoryId || this.categoryType !== 'ENFRENTAMIENTO') return;
    this.isBusy = true;
    this.errorMessage = null;
    this.dataLoaded = true; 
    this.confrontationService.getMatchesByCategory(this.categoryId)
      .pipe(finalize(() => this.isBusy = false))
      .subscribe({
        next: (data) => { this.matches = data; },
        error: (err) => { this.errorMessage = 'Error al cargar los enfrentamientos.'; }
      });
  }
  generateBrackets(): void { /* ... (código sin cambios) ... */ }
  advanceRound(): void { /* ... (código sin cambios) ... */ }
  navigateToMatchScoring(matchId: string): void {
    this.router.navigate(['/scoring/judge/match', matchId]);
  }
  
  // --- Métodos de Ronda Individual ---
  onSubmitNewRound(): void {
    if (this.createRoundForm.invalid || !this.categoryId) {
      this.errorMessage = "Formulario inválido. Revisa todos los campos.";
      this.createRoundForm.markAllAsTouched(); 
      return;
    }
    
    this.isBusy = true;
    this.errorMessage = null;
    
    const formData = this.createRoundForm.value;
    const payload: CreateRondaIndividualDTO = {
      categoriaTipo: this.categoryId!,
      // CORRECCIÓN: 'idEquipo' es el nombre correcto para ENVIAR
      idEquipo: formData.idEquipo, 
      tiempoMs: Number(formData.tiempoMs),
      penalizaciones: Number(formData.penalizaciones),
      etiquetaRonda: formData.etiquetaRonda
    };

    this.individualRoundService.createRound(payload)
      .pipe(finalize(() => this.isBusy = false))
      .subscribe({
        next: (newRound) => {
          // CORRECCIÓN: Usamos 'newRound.equipo.nombre' (del DTO de RESPUESTA)
          alert(`Ronda registrada exitosamente para ${newRound.equipo.nombre}.`);
          
          this.createRoundForm.reset({
            idEquipo: '',
            tiempoMs: 0,
            penalizaciones: 0,
            etiquetaRonda: 'Clasificatoria-1'
          });
        },
        error: (err) => {
          this.errorMessage = 'Error al crear la ronda. ' + (err.error?.message || '');
        }
      });
  }

  // --- Implementación de los métodos de Enfrentamiento (colapsados) ---
  // (generateBrackets y advanceRound)
  generateBrackets(): void {
    if (!this.categoryId || this.isBusy) return;
    
    if (confirm('¿Estás seguro de que deseas generar las llaves? Esta acción no se puede deshacer.')) {
      this.isBusy = true; 
      this.errorMessage = null;
      
      this.confrontationService.generateBrackets(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: () => {
            alert('Llaves generadas exitosamente.');
            this.loadMatches(); 
          },
          error: (err) => {
            this.errorMessage = 'Error al generar llaves. ' + (err.error?.message || 'Revisa la consola.');
          }
        });
    }
  }

  advanceRound(): void {
    if (!this.categoryId || this.isBusy) return;
    
    if (confirm('¿Estás seguro de avanzar a la siguiente ronda? Todos los matches de la ronda actual deben estar finalizados.')) {
      this.isBusy = true;
      this.errorMessage = null;
      
      this.confrontationService.advanceRound(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: () => {
            alert('Se ha avanzado a la siguiente ronda.');
            this.loadMatches();
          },
          error: (err) => {
            this.errorMessage = 'Error al avanzar de ronda. ' + (err.error?.message || '');
          }
        });
    }
  }
}