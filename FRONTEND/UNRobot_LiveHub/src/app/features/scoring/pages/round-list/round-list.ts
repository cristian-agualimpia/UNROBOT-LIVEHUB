import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { ActivatedRoute, Router } from '@angular/router';
// CORRECCIÓN: Importar 'switchMap' y 'BehaviorSubject'
import { finalize, Observable, BehaviorSubject, switchMap } from 'rxjs'; 
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

// CORRECCIÓN DE RUTA: ../../../../ (4 puntos)
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';
import { EnfrentamientoListItemDTO } from '../../../../core/models/enfrentamiento-list-item.model';
import { EquipoDTO } from '../../../../core/models/equipo.model'; 
// CORRECCIÓN: Importar 'LeaderboardEntryDTO' desde el servicio
import { IndividualRoundService, CreateRondaIndividualDTO, LeaderboardEntryDTO } from '../../services/individual-round.service'; 

// Servicios
import { ConfrontationService } from '../../services/confrontation.service';
import { TeamService } from '../../../team-registration/services/team.service'; 

// "Mini-componentes"
import { ConfrontationListItemComponent } from '../../components/confrontation-list-item/confrontation-list-item';
import { VelocistaMatchItemComponent } from '../../components/velocista-match-item/velocista-match-item'; 
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';

@Component({
  selector: 'app-round-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule, 
    ConfrontationListItemComponent,
    VelocistaMatchItemComponent, 
    BackButtonComponent
    // IndividualRoundListItemComponent ya no se usa aquí
  ],
  templateUrl: './round-list.html',
  styleUrls: ['./round-list.css']
})
export class RoundListComponent implements OnInit {
  // --- Inyecciones ---
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder); 
  private confrontationService = inject(ConfrontationService);
  private individualRoundService = inject(IndividualRoundService);
  private teamService = inject(TeamService); 

  // --- Propiedades de Estado ---
  public categoryType: 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL' | null = null;
  public categoryId: string | null = null;
  public isVelocistaCategory = false; 
  public isBusy = false; 
  public errorMessage: string | null = null;

  // --- Lógica de Enfrentamiento (Fase 1) ---
  public matches: EnfrentamientoListItemDTO[] = [];
  public dataLoaded = false; 

  // --- Lógica de Ronda Individual (Fase 1 y Simple) ---
  public createRoundForm: FormGroup;
  public teams$!: Observable<EquipoDTO[]>;
  public leaderboard$!: Observable<LeaderboardEntryDTO[]>; 
  
  // --- Lógica de Finales (Fase 3 y 5) ---
  private refreshBrackets$ = new BehaviorSubject<void>(undefined);
  public finalBrackets$!: Observable<EnfrentamientoListItemDTO[]>; 


  constructor() {
    this.createRoundForm = this.fb.group({
      idEquipo: ['', Validators.required],
      tiempoMs: [0, [Validators.required, Validators.min(0)]],
      penalizaciones: [0, [Validators.required, Validators.min(0)]],
      etiquetaRonda: ['Intento', Validators.required]
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
      if (this.categoryId.startsWith('SEGUIDOR') || this.categoryId.startsWith('DRONE')) {
        this.isVelocistaCategory = true;
      }
      
      this.teams$ = this.teamService.getTeamsByCategory(this.categoryId!);

      if (this.isVelocistaCategory) {
        // CORRECCIÓN: 'getLeaderboard' existe en 'individualRoundService'
        this.leaderboard$ = this.individualRoundService.getLeaderboard(this.categoryId!);
        this.loadFinalBrackets();
      }
    }
  }

  // --- Métodos de Enfrentamiento (Fase 1) ---
  public loadMatches(): void { 
    if (!this.categoryId || this.categoryType !== 'ENFRENTAMIENTO') return;
    this.isBusy = true; this.errorMessage = null; this.dataLoaded = true; 
    this.confrontationService.getMatchesByCategory(this.categoryId)
      .pipe(finalize(() => this.isBusy = false))
      .subscribe({
        next: (data) => { this.matches = data; },
        error: (err) => { this.errorMessage = 'Error al cargar los enfrentamientos.'; }
      });
  }
  generateBrackets(): void {
    if (!this.categoryId || this.isBusy) return;
    if (confirm('¿Estás seguro de que deseas generar las llaves? Esta acción no se puede deshacer.')) {
      this.isBusy = true; this.errorMessage = null;
      this.confrontationService.generateBrackets(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: () => { alert('Llaves generadas exitosamente.'); this.loadMatches(); },
          error: (err) => { this.errorMessage = 'Error al generar llaves. ' + (err.error?.message || 'Revisa la consola.'); }
        });
    }
  }
  navigateToMatchScoring(matchId: string): void {
    this.router.navigate(['/scoring/judge/match', matchId]);
  }

  // --- Métodos de Ronda Individual (Fase 1 y Simple) ---
  onSubmitNewRound(): void {
    if (this.createRoundForm.invalid || !this.categoryId) {
      this.errorMessage = "Formulario inválido. Revisa todos los campos."; this.createRoundForm.markAllAsTouched(); return;
    }
    this.isBusy = true; this.errorMessage = null;
    const formData = this.createRoundForm.value;
    const payload: CreateRondaIndividualDTO = {
      categoriaTipo: this.categoryId!, idEquipo: formData.idEquipo, 
      tiempoMs: Number(formData.tiempoMs), penalizaciones: Number(formData.penalizaciones),
      etiquetaRonda: formData.etiquetaRonda
    };
    this.individualRoundService.createRound(payload)
      .pipe(finalize(() => this.isBusy = false))
      .subscribe({
        next: (newRound) => {
          alert(`Ronda registrada exitosamente para ${newRound.equipo.nombre}.`);
          this.createRoundForm.reset({ idEquipo: '', tiempoMs: 0, penalizaciones: 0, etiquetaRonda: 'Intento' });
        },
        error: (err) => { this.errorMessage = 'Error al crear la ronda. ' + (err.error?.message || ''); }
      });
  }

  // --- Métodos de Velocista (Fase 2, 3, 5) ---
  
  /** (Fase 2) Llama al servicio para crear semifinales */
  generateFinals(): void {
    if (!this.categoryId || this.isBusy) return;
    if (confirm('¿Seguro que deseas cerrar la clasificación y generar las finales (Top 4)?')) {
      this.isBusy = true;
      this.confrontationService.generateVelocistaFinals(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: () => {
            alert('¡Finales generadas! Refrescando llaves...');
            this.refreshBrackets$.next(); 
          },
          error: (err) => { this.errorMessage = 'Error al generar finales.'; }
        });
    }
  }
  
  /** (Fase 3/5) Carga los matches (Semis, Final) */
  loadFinalBrackets(): void {
    if (!this.categoryId) return;
    this.finalBrackets$ = this.refreshBrackets$.pipe(
      // CORRECCIÓN: 'switchMap' debe ser importado de 'rxjs'
      switchMap(() => this.confrontationService.getMatchesByCategory(this.categoryId!))
    );
  }
  
  /** (Fase 4) Llama al servicio para avanzar de ronda (Semi -> Final) */
  advanceRound(): void {
    if (!this.categoryId || this.isBusy) return;
    if (confirm('¿Seguro que deseas avanzar a la siguiente ronda (Final)?')) {
      this.isBusy = true;
      this.confrontationService.advanceRound(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: () => {
            alert('¡Ronda avanzada! Refrescando llaves...');
            this.refreshBrackets$.next(); 
          },
          error: (err) => { this.errorMessage = 'Error al avanzar ronda.'; }
        });
    }
  }
}