// --- CORRECCIÓN: Faltaban todos estos imports ---
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, switchMap, catchError, EMPTY, tap } from 'rxjs';

// Componentes y Servicios
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';
import { ConfrontationService, UpdateScoreDTO } from '../../services/confrontation.service';
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model'; // <-- Importar modelo

@Component({
  selector: 'app-judge-form-confrontation',
  standalone: true,
  imports: [CommonModule, BackButtonComponent],
  templateUrl: './judge-form-confrontation.html',
  styleUrls: ['./judge-form-confrontation.css']
})
export class JudgeFormConfrontationComponent implements OnInit {
  
  // --- CORRECCIÓN: Faltaban las inyecciones de servicios ---
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confrontationService = inject(ConfrontationService);

  private matchId: string | null = null;
  
  // (Esto ya lo habíamos corregido a 'public' la vez pasada)
  public matchSubject = new BehaviorSubject<EnfrentamientoDTO | null>(null);
  public match$ = this.matchSubject.asObservable();

  public isLoading = true;
  public errorMessage: string | null = null;

  ngOnInit(): void {
    // 1. Obtener el ID del match de la URL
    this.matchId = this.route.snapshot.paramMap.get('matchId');
    if (!this.matchId) {
      this.errorMessage = "Error: No se proporcionó un ID de match.";
      this.isLoading = false;
      return;
    }

    // 2. Cargar los datos iniciales del match
    this.loadMatchData();
  }

  // Carga o recarga los datos del match desde el servicio
  loadMatchData(): void {
    if (!this.matchId) return;

    this.isLoading = true;
    this.confrontationService.getMatchById(this.matchId).subscribe({
      // --- CORRECCIÓN: Añadir tipo a 'data' ---
      next: (data: EnfrentamientoDTO) => {
        this.matchSubject.next(data); // Emite los datos
        this.isLoading = false;
      },
      // --- CORRECCIÓN: Añadir tipo a 'err' ---
      error: (err: any) => {
        this.errorMessage = "Error al cargar el enfrentamiento.";
        this.isLoading = false;
      }
    });
  }

  // Maneja las actualizaciones de puntaje
  updateScore(team: 'A' | 'B', action: 'INCREMENT_POINT' | 'DECREMENT_POINT'): void {
    if (!this.matchId || this.matchSubject.value?.estado === 'FINALIZADO') return;

    this.isLoading = true;
    const payload: UpdateScoreDTO = { team, action };
    
    this.confrontationService.updateScore(this.matchId, payload).subscribe({
      // --- CORRECCIÓN: Añadir tipo ---
      next: (updatedMatch: EnfrentamientoDTO) => {
        this.matchSubject.next(updatedMatch); // Actualiza la UI con la respuesta
        this.isLoading = false;
      },
      // --- CORRECCIÓN: Añadir tipo ---
      error: (err: any) => {
        alert('Error al actualizar puntaje. ' + (err.error?.message || ''));
        this.isLoading = false;
      }
    });
  }

  // Declara un ganador
  declareWinner(winnerId: string | null): void {
    if (!this.matchId || !winnerId || this.matchSubject.value?.estado === 'FINALIZADO') return;
    
    const winnerName = (this.matchSubject.value?.equipoA?.id === winnerId) 
      ? this.matchSubject.value.equipoA.nombre 
      : this.matchSubject.value?.equipoB?.nombre;

    if (confirm(`¿Estás seguro de declarar a [${winnerName}] como ganador? Esta acción finalizará el match.`)) {
      this.isLoading = true;
      this.confrontationService.declareWinner(this.matchId, winnerId).subscribe({
        // --- CORRECCIÓN: Añadir tipo ---
        next: (finalizedMatch: EnfrentamientoDTO) => {
          this.matchSubject.next(finalizedMatch); // Actualiza la UI
          this.isLoading = false;
          alert('Match finalizado. Ganador: ' + winnerName);
        },
        // --- CORRECCIÓN: Añadir tipo ---
        error: (err: any) => {
          alert('Error al declarar ganador. ' + (err.error?.message || ''));
          this.isLoading = false;
        }
      });
    }
  }
}