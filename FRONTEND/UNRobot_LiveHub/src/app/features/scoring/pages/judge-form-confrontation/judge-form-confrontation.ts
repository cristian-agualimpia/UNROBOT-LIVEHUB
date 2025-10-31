import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, switchMap, catchError, EMPTY, tap } from 'rxjs';

// Componentes y Servicios
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';
import { ConfrontationService, UpdateScoreDTO } from '../../services/confrontation.service';
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model';

@Component({
  selector: 'app-judge-form-confrontation',
  standalone: true,
  imports: [CommonModule, BackButtonComponent],
  templateUrl: './judge-form-confrontation.html',
  styleUrls: ['./judge-form-confrontation.css']
})
export class JudgeFormConfrontationComponent implements OnInit {
  
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confrontationService = inject(ConfrontationService);

  private matchId: string | null = null;
  
  public matchSubject = new BehaviorSubject<EnfrentamientoDTO | null>(null);
  public match$ = this.matchSubject.asObservable();

  public isLoading = true;
  public errorMessage: string | null = null;

  ngOnInit(): void {
    this.matchId = this.route.snapshot.paramMap.get('matchId');
    if (!this.matchId) {
      this.errorMessage = "Error: No se proporcionó un ID de match.";
      this.isLoading = false;
      return;
    }
    this.loadMatchData();
  }

  loadMatchData(): void {
    if (!this.matchId) return;

    this.isLoading = true;
    this.confrontationService.getMatchById(this.matchId).subscribe({
      next: (data: EnfrentamientoDTO) => {
        this.matchSubject.next(data);
        this.isLoading = false;
      },
      error: (err: any) => {
        this.errorMessage = "Error al cargar el enfrentamiento.";
        this.isLoading = false;
      }
    });
  }

  updateScore(team: 'A' | 'B', action: 'INCREMENT_POINT' | 'DECREMENT_POINT'): void {
    // Asumimos que el DTO del 'estado' no existe, así que lo calculamos
    const estado = this.matchSubject.value?.idGanador ? 'FINALIZADO' : 'EN_CURSO';
    if (!this.matchId || estado === 'FINALIZADO') return;

    this.isLoading = true;
    const payload: UpdateScoreDTO = { team, action };
    
    this.confrontationService.updateScore(this.matchId, payload).subscribe({
      next: (updatedMatch: EnfrentamientoDTO) => {
        this.matchSubject.next(updatedMatch); 
        this.isLoading = false;
      },
      error: (err: any) => {
        alert('Error al actualizar puntaje. ' + (err.error?.message || ''));
        this.isLoading = false;
      }
    });
  }

  // Declara un ganador
  declareWinner(winnerId: string | null): void {
    const estado = this.matchSubject.value?.idGanador ? 'FINALIZADO' : 'EN_CURSO';
    if (!this.matchId || !winnerId || estado === 'FINALIZADO') return;
    
    // --- CORRECCIÓN ---
    // Ya no tenemos el 'nombre', así que mostramos el ID en la alerta.
    // Esto es temporal. Si el endpoint 'getMatchById' SÍ devuelve los nombres,
    // tendremos que crear un DTO diferente.
    const winnerIdentifier = (this.matchSubject.value?.idEquipoA === winnerId) 
      ? `Equipo (ID: ${winnerId})`
      : `Equipo (ID: ${winnerId})`;

    if (confirm(`¿Estás seguro de declarar a [${winnerIdentifier}] como ganador? Esta acción finalizará el match.`)) {
      this.isLoading = true;
      this.confrontationService.declareWinner(this.matchId, winnerId).subscribe({
        next: (finalizedMatch: EnfrentamientoDTO) => {
          this.matchSubject.next(finalizedMatch); 
          this.isLoading = false;
          alert('Match finalizado. Ganador: ' + winnerIdentifier);
        },
        error: (err: any) => {
          alert('Error al declarar ganador. ' + (err.error?.message || ''));
          this.isLoading = false;
        }
      });
    }
  }
}