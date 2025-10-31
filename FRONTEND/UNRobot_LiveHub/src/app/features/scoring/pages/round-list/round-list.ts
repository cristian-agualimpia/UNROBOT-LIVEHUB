import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs'; // Importamos finalize

// Modelos
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model';
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';

// Servicios
import { ConfrontationService } from '../../services/confrontation.service';
import { IndividualRoundService } from '../../services/individual-round.service';

// "Mini-componentes"
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
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confrontationService = inject(ConfrontationService);
  private individualRoundService = inject(IndividualRoundService);

  public categoryType: 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL' | null = null;
  public categoryId: string | null = null;
  
  // --- CAMBIO: Ya no usamos Observables (matches$) ---
  // Usamos arreglos locales para guardar los datos.
  public matches: EnfrentamientoDTO[] = [];
  public rounds: RondaIndividualDTO[] = [];
  
  // --- CAMBIO: 'isLoading' ahora es 'isBusy' para evitar conflictos ---
  // y lo usaremos solo para los botones
  public isBusy = false; 
  public errorMessage: string | null = null;
  // Para saber si ya cargamos datos por primera vez
  public dataLoaded = false; 

  ngOnInit(): void {
    this.categoryId = this.route.snapshot.paramMap.get('categoryId');
    const type = this.route.snapshot.paramMap.get('categoryType');

    if (!this.categoryId || !type) {
      this.errorMessage = "Error: No se encontró el ID o tipo de categoría.";
      return;
    }
    
    this.categoryType = type as 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL';
    
    // --- CAMBIO: Ya no cargamos datos automáticamente ---
    // El usuario debe presionar el botón
  }

  // --- NUEVO MÉTODO: Se llama desde el botón "Cargar" ---
  public loadData(): void {
    if (!this.categoryId) return;

    this.isBusy = true;
    this.errorMessage = null;
    this.dataLoaded = true; // Marcamos que el intento de carga se hizo

    if (this.categoryType === 'ENFRENTAMIENTO') {
      this.confrontationService.getMatchesByCategory(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: (data) => {
            this.matches = data; // Guardamos los datos en el arreglo local
          },
          error: (err) => {
            this.errorMessage = 'Error al cargar los enfrentamientos.';
            this.matches = []; // Vaciamos en caso de error
          }
        });
    } else if (this.categoryType === 'RONDA_INDIVIDUAL') {
      this.individualRoundService.getRoundsByCategory(this.categoryId)
        .pipe(finalize(() => this.isBusy = false))
        .subscribe({
          next: (data) => {
            this.rounds = data; // Guardamos los datos en el arreglo local
          },
          error: (err) => {
            this.errorMessage = 'Error al cargar las rondas.';
            this.rounds = []; // Vaciamos en caso de error
          }
        });
    }
  }


  // --- MÉTODOS DE ACCIÓN (Actualizados) ---

  generateBrackets(): void {
    if (!this.categoryId || this.isBusy) return;
    
    if (confirm('¿Estás seguro de que deseas generar las llaves? Esta acción no se puede deshacer.')) {
      this.isBusy = true; 
      this.errorMessage = null;
      
      this.confrontationService.generateBrackets(this.categoryId)
        .pipe(finalize(() => this.isBusy = false)) // Nos aseguramos de que 'isBusy' se apague
        .subscribe({
          next: () => {
            alert('Llaves generadas exitosamente.');
            this.loadData(); // Refrescamos la lista automáticamente
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
            this.loadData(); // Refrescamos la lista automáticamente
          },
          error: (err) => {
            this.errorMessage = 'Error al avanzar de ronda. ' + (err.error?.message || '');
          }
        });
    }
  }
  
  // --- Métodos de Navegación (Sin cambios) ---
  navigateToMatchScoring(matchId: string): void {
    this.router.navigate(['/scoring/judge/match', matchId]);
  }

  navigateToRoundScoring(roundId: string): void {
    this.router.navigate(['/scoring/judge/individual', roundId]);
  }
}