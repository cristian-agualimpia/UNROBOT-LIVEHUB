import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- Necesario
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';

@Component({
  selector: 'app-individual-round-list-item',
  standalone: true,
  imports: [CommonModule], // <-- Importar
  templateUrl: './individual-round-list-item.html',
  styleUrls: ['./individual-round-list-item.css']
})
export class IndividualRoundListItemComponent {

  @Input() roundData!: RondaIndividualDTO;
  @Output() onScore = new EventEmitter<string>();

  /**
   * Emite el evento 'onScore' con el ID de esta ronda.
   */
  scoreRound(): void {
    if (this.roundData.estadoRonda !== 'FINALIZADA') {
      this.onScore.emit(this.roundData.id);
    }
  }

  /**
   * Formatea el tiempo de milisegundos a MM:SS.sss
   */
  formatTime(ms: number): string {
    if (!ms || ms === 0) return '00:00.000';
    
    const totalSeconds = ms / 1000;
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = Math.floor(totalSeconds % 60);
    const milliseconds = ms % 1000;

    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}.${milliseconds.toString().padStart(3, '0')}`;
  }
}