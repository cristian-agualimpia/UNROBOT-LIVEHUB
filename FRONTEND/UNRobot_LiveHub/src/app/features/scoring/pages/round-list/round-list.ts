import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- Necesario para *ngIf, *ngFor, async
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, BehaviorSubject, switchMap, EMPTY, tap, catchError } from 'rxjs';

// Modelos
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model';
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';

// Servicios
import { ConfrontationService } from '../../services/confrontation.service';
import { IndividualRoundService } from '../../services/individual-round.service';

// "Mini-componentes" que creamos
import { ConfrontationListItemComponent } from '../../components/confrontation-list-item/confrontation-list-item';
import { IndividualRoundListItemComponent } from '../../components/individual-round-list-item/individual-round-list-item';

// Componente de botón de volver
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';

@Component({
  selector: 'app-round-list',
  standalone: true,
  imports: [
    CommonModule,
    ConfrontationListItemComponent,
    IndividualRoundListItemComponent,
    BackButtonComponent
  ],
  templateUrl: './round-list.html',
  styleUrls: ['./round-list.css']
})
export class RoundListComponent implements OnInit {
  // Inyección de servicios
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confrontationService = inject(ConfrontationService);
  private individualRoundService = inject(IndividualRoundService);

  // Almacena los parámetros de la URL
  public categoryType: 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL' | null = null;
  public categoryId: string | null = null;
  
  // Sujeto para refrescar los datos (ej. después de generar llaves)
  private refreshData$ = new BehaviorSubject<void>(undefined);

  // Observables para las listas de datos
  public matches$!: Observable<EnfrentamientoDTO[]>;
  public rounds$!: Observable<RondaIndividualDTO[]>;
  
  public isLoading = false;
  public errorMessage: string | null = null;

  ngOnInit(): void {
    // 1. Leer los parámetros de la URL
    this.categoryId = this.route.snapshot.paramMap.get('categoryId');
    const type = this.route.snapshot.paramMap.get('categoryType');

    if (!this.categoryId || !type) {
      this.errorMessage = "Error: No se encontró el ID o tipo de categoría.";
      return;
    }
    
    this.categoryType = type as 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL';

    // 2. Cargar los datos basado en el tipo de lógica
    if (this.categoryType === 'ENFRENTAMIENTO') {
      this.loadMatches();
    } else if (this.categoryType === 'RONDA_INDIVIDUAL') {
      this.loadIndividualRounds();
    }
  }

  // Carga la lista de enfrentamientos
  private loadMatches(): void {
    if (!this.categoryId) return;
    
    this.matches$ = this.refreshData$.pipe(
      tap(() => this.isLoading = true),
      switchMap(() => this.confrontationService.getMatchesByCategory(this.categoryId!)),
      tap(() => this.isLoading = false),
      catchError(err => {
        this.errorMessage = 'Error al cargar los enfrentamientos.';
        this.isLoading = false;
        return EMPTY; // No emite nada si hay error
      })
    );
  }

  // Carga la lista de rondas individuales
  private loadIndividualRounds(): void {
    if (!this.categoryId) return;

    this.rounds$ = this.refreshData$.pipe(
      tap(() => this.isLoading = true),
      switchMap(() => this.individualRoundService.getRoundsByCategory(this.categoryId!)),
      tap(() => this.isLoading = false),
      catchError(err => {
        this.errorMessage = 'Error al cargar las rondas individuales.';
        this.isLoading = false;
        return EMPTY;
      })
    );
  }

  // --- Métodos de Acción para los Botones ---

  // Llama al servicio para generar llaves (solo enfrentamientos)
  generateBrackets(): void {
    if (!this.categoryId) return;
    
    if (confirm('¿Estás seguro de que deseas generar las llaves? Esta acción no se puede deshacer.')) {
      this.isLoading = true;
      this.confrontationService.generateBrackets(this.categoryId).subscribe({
        next: () => {
          alert('Llaves generadas exitosamente.');
          this.refreshData$.next(); // Refresca la lista
        },
        error: (err) => {
          this.errorMessage = 'Error al generar llaves. ' + (err.error?.message || '');
          this.isLoading = false;
        }
      });
    }
  }

  // Llama al servicio para avanzar de ronda (solo enfrentamientos)
  advanceRound(): void {
    if (!this.categoryId) return;
    
    if (confirm('¿Estás seguro de avanzar a la siguiente ronda? Todos los matches de la ronda actual deben estar finalizados.')) {
      this.isLoading = true;
      this.confrontationService.advanceRound(this.categoryId).subscribe({
        next: () => {
          alert('Se ha avanzado a la siguiente ronda.');
          this.refreshData$.next(); // Refresca la lista
        },
        error: (err) => {
          this.errorMessage = 'Error al avanzar de ronda. ' + (err.error?.message || '');
          this.isLoading = false;
        }
      });
    }
  }
  
  // --- Métodos de Navegación (llamados desde los mini-componentes) ---

  // Navega al formulario de puntuación de Enfrentamiento
  navigateToMatchScoring(matchId: string): void {
    this.router.navigate(['/scoring/judge/match', matchId]);
  }

  // Navega al formulario de puntuación Individual
  navigateToRoundScoring(roundId: string): void {
    this.router.navigate(['/scoring/judge/individual', roundId]);
  }
}