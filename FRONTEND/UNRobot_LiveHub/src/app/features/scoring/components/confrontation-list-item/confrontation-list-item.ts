import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- Necesario para *ngIf, *ngFor, ngClass
import { EnfrentamientoDTO, EstadoEnfrentamiento } from '../../../../core/models/enfrentamiento.model';

@Component({
  selector: 'app-confrontation-list-item',
  standalone: true,
  imports: [CommonModule], // <-- Importar CommonModule
  templateUrl: './confrontation-list-item.html',
  styleUrls: ['./confrontation-list-item.css']
})
export class ConfrontationListItemComponent {

  // 1. Recibe el DTO completo del enfrentamiento
  @Input() matchData!: EnfrentamientoDTO;
  
  // 2. Emite el ID del match cuando el juez presiona "Puntuar"
  @Output() onScore = new EventEmitter<string>();

  /**
   * Emite el evento 'onScore' con el ID de este match.
   */
  scoreMatch(): void {
    if (this.matchData.estado !== 'FINALIZADO') {
      this.onScore.emit(this.matchData.id);
    }
  }

  /**
   * Helper para obtener el nombre del ganador.
   */
  getWinnerName(): string {
    if (this.matchData.ganadorId === this.matchData.equipoA?.id) {
      return this.matchData.equipoA.nombre;
    }
    if (this.matchData.ganadorId === this.matchData.equipoB?.id) {
      return this.matchData.equipoB.nombre;
    }
    return 'N/A';
  }
}