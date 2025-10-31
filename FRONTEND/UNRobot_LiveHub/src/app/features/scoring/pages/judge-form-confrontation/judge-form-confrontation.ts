import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs'; 
// CORRECCIÓN: Importar módulos de formularios
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

// Componentes y Servicios
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';
import { ConfrontationService, UpdateScoreDTO } from '../../services/confrontation.service';
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model';

// Definimos el tipo de estado calculado
type EstadoCalculado = 'PENDIENTE' | 'EN_CURSO' | 'FINALIZADO';

@Component({
  selector: 'app-judge-form-confrontation',
  standalone: true,
  // CORRECCIÓN: Añadir ReactiveFormsModule
  imports: [CommonModule, BackButtonComponent, ReactiveFormsModule], 
  templateUrl: './judge-form-confrontation.html',
  styleUrls: ['./judge-form-confrontation.css']
})
export class JudgeFormConfrontationComponent implements OnInit {
  
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private confrontationService = inject(ConfrontationService);
  private fb = inject(FormBuilder); // CORRECCIÓN: Inyectar FormBuilder

  private matchId: string | null = null;
  
  public matchSubject = new BehaviorSubject<EnfrentamientoDTO | null>(null);
  public match$ = this.matchSubject.asObservable();

  // Propiedades locales
  public teamAName: string = 'Cargando...';
  public teamBName: string = 'Cargando...';
  public estadoCalculado: EstadoCalculado = 'PENDIENTE'; 
  public isLoading = true;
  public errorMessage: string | null = null;

  // CORRECCIÓN: Formulario para las notas
  public noteForm: FormGroup;

  constructor() {
    this.noteForm = this.fb.group({
      noteText: [''] // El control para el <textarea>
    });
  }

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
      next: (match: EnfrentamientoDTO) => {
        this.matchSubject.next(match);
        this.calculateAndUpdateState(match); 
        this.teamAName = match.nombreEquipoA || 'N/A';
        this.teamBName = match.nombreEquipoB || 'N/A';
        
        // CORRECCIÓN: Cargar las notas existentes en el formulario
        this.noteForm.patchValue({ noteText: match.faltasNotas || '' });
        
        this.isLoading = false;
      },
      error: (err: any) => {
        this.errorMessage = "Error al cargar los datos del enfrentamiento.";
        this.isLoading = false;
      }
    });
  }

  private calculateAndUpdateState(match: EnfrentamientoDTO | null): void {
    if (!match) return;
    
    if (match.idGanador) {
      this.estadoCalculado = 'FINALIZADO';
    } else if (match.puntosA > 0 || match.puntosB > 0) {
      this.estadoCalculado = 'EN_CURSO';
    } else {
      this.estadoCalculado = 'PENDIENTE';
    }
  }

  updateScore(team: 'A' | 'B', action: 'INCREMENT_POINT' | 'DECREMENT_POINT'): void {
    if (!this.matchId || this.estadoCalculado === 'FINALIZADO') return;

    this.isLoading = true;
    const payload: UpdateScoreDTO = { team, action, note: null }; // Nota es null para puntos
    
    this.confrontationService.updateScore(this.matchId, payload).subscribe({
      next: (updatedMatch: EnfrentamientoDTO) => {
        this.matchSubject.next(updatedMatch); 
        this.calculateAndUpdateState(updatedMatch);
        this.isLoading = false;
      },
      error: (err: any) => {
        alert('Error al actualizar puntaje. ' + (err.error?.message || ''));
        this.isLoading = false;
      }
    });
  }

  // --- ¡NUEVO MÉTODO! ---
  /**
   * Envía una nota/falta al backend usando el endpoint /score
   */
  addNote(team: 'A' | 'B'): void {
    if (!this.matchId || this.estadoCalculado === 'FINALIZADO') return;

    const noteContent = this.noteForm.value.noteText;
    if (!noteContent || noteContent.trim() === '') {
      alert('Por favor, escribe una nota antes de reportarla.');
      return;
    }

    this.isLoading = true;
    const payload: UpdateScoreDTO = {
      team: team,
      action: 'ADD_NOTE',
      note: noteContent
    };

    this.confrontationService.updateScore(this.matchId, payload).subscribe({
      next: (updatedMatch) => {
        this.matchSubject.next(updatedMatch); 
        this.calculateAndUpdateState(updatedMatch);
        // Actualizamos el formulario con la nota que guardó el backend
        this.noteForm.patchValue({ noteText: updatedMatch.faltasNotas || '' });
        this.isLoading = false;
        alert('Nota/Falta guardada exitosamente.');
      },
      error: (err) => {
        alert('Error al guardar la nota. ' + (err.error?.message || ''));
        this.isLoading = false;
      }
    });
  }

  declareWinner(winnerId: string | null): void {
    if (!this.matchId || !winnerId || this.estadoCalculado === 'FINALIZADO') return;
    
    const winnerName = (this.matchSubject.value?.idEquipoA === winnerId) 
      ? this.teamAName 
      : this.teamBName;

    if (confirm(`¿Estás seguro de declarar a [${winnerName}] como ganador? ...`)) {
      this.isLoading = true;
      this.confrontationService.declareWinner(this.matchId, winnerId).subscribe({
        next: (finalizedMatch: EnfrentamientoDTO) => {
          this.matchSubject.next(finalizedMatch);
          this.calculateAndUpdateState(finalizedMatch);
          this.isLoading = false;
          alert('Match finalizado. Ganador: ' + winnerName);
        },
        error: (err: any) => {
          alert('Error al declarar ganador. ' + (err.error?.message || ''));
          this.isLoading = false;
        }
      });
    }
  }
}