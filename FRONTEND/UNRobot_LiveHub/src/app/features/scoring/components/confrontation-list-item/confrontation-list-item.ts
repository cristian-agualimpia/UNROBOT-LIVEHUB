import { Component, Input, Output, EventEmitter, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin, of } from 'rxjs'; 

// --- CORRECCIÓN DE RUTA: ../../../../ (4 puntos) ---
import { EnfrentamientoListItemDTO } from '../../../../core/models/enfrentamiento-list-item.model';
import { EquipoDTO } from '../../../../core/models/equipo.model'; 
import { TeamService } from '../../../team-registration/services/team.service';

type EstadoCalculado = 'PENDIENTE' | 'EN_CURSO' | 'FINALIZADO';

@Component({
  selector: 'app-confrontation-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confrontation-list-item.html',
  styleUrls: ['./confrontation-list-item.css']
})
export class ConfrontationListItemComponent implements OnInit {

  @Input() matchData!: EnfrentamientoListItemDTO;
  @Output() onScore = new EventEmitter<string>();

  private teamService = inject(TeamService);

  public estadoCalculado!: EstadoCalculado;
  public teamAName: string = '...';
  public teamBName: string = '...';
  private winnerName: string = 'N/A';

  ngOnInit(): void {
    if (this.matchData.idGanador) {
      this.estadoCalculado = 'FINALIZADO';
    } else if (this.matchData.puntosA > 0 || this.matchData.puntosB > 0) {
      this.estadoCalculado = 'EN_CURSO';
    } else {
      this.estadoCalculado = 'PENDIENTE';
    }
    this.loadTeamNames();
  }

  loadTeamNames(): void {
    const teamA$ = this.matchData.idEquipoA 
      ? this.teamService.getTeamById(this.matchData.idEquipoA) 
      : of(null as EquipoDTO | null); 
      
    const teamB$ = this.matchData.idEquipoB 
      ? this.teamService.getTeamById(this.matchData.idEquipoB)
      : of(null as EquipoDTO | null);

    forkJoin([teamA$, teamB$]).subscribe(([teamA, teamB]) => {
      this.teamAName = teamA?.nombre || 'Equipo Pendiente';
      this.teamBName = teamB?.nombre || 'Equipo Pendiente';
      
      if (this.matchData.idGanador === teamA?.id) {
        this.winnerName = teamA.nombre;
      } else if (this.matchData.idGanador === teamB?.id) {
        this.winnerName = teamB.nombre;
      }
    });
  }

  scoreMatch(): void {
    if (this.estadoCalculado !== 'FINALIZADO') {
      this.onScore.emit(this.matchData.id);
    }
  }

  getWinnerName(): string {
    return this.winnerName;
  }
}