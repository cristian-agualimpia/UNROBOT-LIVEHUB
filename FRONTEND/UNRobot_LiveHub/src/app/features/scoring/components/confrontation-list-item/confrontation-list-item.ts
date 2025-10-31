// 1. Importar OnInit
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EnfrentamientoDTO } from '../../../../core/models/enfrentamiento.model';

type EstadoCalculado = 'PENDIENTE' | 'EN_CURSO' | 'FINALIZADO';

@Component({
  selector: 'app-confrontation-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confrontation-list-item.html',
  styleUrls: ['./confrontation-list-item.css']
})
// 2. Implementar OnInit
export class ConfrontationListItemComponent implements OnInit {

  @Input() matchData!: EnfrentamientoDTO;
  @Output() onScore = new EventEmitter<string>();

  // 3. Crear una propiedad para guardar el estado
  public estadoCalculado!: EstadoCalculado;

  // 4. Calcular el estado UNA SOLA VEZ cuando el componente se inicia
  ngOnInit(): void {
    if (this.matchData.idGanador) {
      this.estadoCalculado = 'FINALIZADO';
    } else if (this.matchData.puntosA > 0 || this.matchData.puntosB > 0) {
      this.estadoCalculado = 'EN_CURSO';
    } else {
      this.estadoCalculado = 'PENDIENTE';
    }
  }

  scoreMatch(): void {
    // 5. Usar la propiedad en lugar de la función
    if (this.estadoCalculado !== 'FINALIZADO') {
      this.onScore.emit(this.matchData.id);
    }
  }

  // (El resto de los métodos 'get...Name()' están bien como estaban)
  getEquipoAName(): string {
    return this.matchData.idEquipoA || 'Equipo Pendiente';
  }

  getEquipoBName(): string {
    return this.matchData.idEquipoB || 'Equipo Pendiente';
  }

  getWinnerName(): string {
    if (this.matchData.idGanador === this.matchData.idEquipoA) {
      return this.getEquipoAName();
    }
    if (this.matchData.idGanador === this.matchData.idEquipoB) {
      return this.getEquipoBName();
    }
    return 'N/A';
  }
}