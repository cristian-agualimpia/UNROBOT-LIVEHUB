import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
// CORRECCIÓN DE RUTA: ../../../../
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';

@Component({
  selector: 'app-individual-round-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './individual-round-list-item.html',
  styleUrls: ['./individual-round-list-item.css']
})
export class IndividualRoundListItemComponent {
  
  @Input() roundData!: RondaIndividualDTO;
  @Output() onScore = new EventEmitter<string>();

  scoreRound(): void {
    // Ya no revisamos el estado, siempre se puede "ver/editar"
    this.onScore.emit(this.roundData.id);
  }
}