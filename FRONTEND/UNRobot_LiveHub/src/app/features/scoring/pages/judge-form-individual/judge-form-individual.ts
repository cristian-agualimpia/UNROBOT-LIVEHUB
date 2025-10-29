import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, BehaviorSubject, catchError, EMPTY } from 'rxjs';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';
import { IndividualRoundService, UpdateRondaIndividualDTO } from '../../services/individual-round.service';
import { RondaIndividualDTO } from '../../../../core/models/ronda-individual.model';

@Component({
  selector: 'app-judge-form-individual',
  standalone: true,
  imports: [CommonModule, BackButtonComponent, ReactiveFormsModule],
  templateUrl: './judge-form-individual.html',
  styleUrls: ['./judge-form-individual.css']
})
export class JudgeFormIndividualComponent implements OnInit {
  
  // Inyecciones de servicios
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder); 
  private individualRoundService = inject(IndividualRoundService);

  private roundId: string | null = null;
  
  // --- CORRECCIÓN: Cambiado de 'private' a 'public' ---
  public roundSubject = new BehaviorSubject<RondaIndividualDTO | null>(null);
  public round$ = this.roundSubject.asObservable();

  public isLoading = true;
  public errorMessage: string | null = null;
  
  public scoreForm: FormGroup;

  constructor() {
    this.scoreForm = this.fb.group({
      puntos: [0, [Validators.required, Validators.min(0)]],
      tiempoMs: [0, [Validators.required, Validators.min(0)]],
      fallos: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.roundId = this.route.snapshot.paramMap.get('roundId');
    if (!this.roundId) {
      this.errorMessage = "Error: No se proporcionó un ID de ronda.";
      this.isLoading = false;
      return;
    }
    this.loadRoundData();
  }

  loadRoundData(): void {
    if (!this.roundId) return;

    this.isLoading = true;
    this.individualRoundService.getRoundById(this.roundId).subscribe({
      // --- CORRECCIÓN: Añadir tipo ---
      next: (data: RondaIndividualDTO) => {
        this.roundSubject.next(data);
        this.scoreForm.patchValue({
          puntos: data.puntos,
          tiempoMs: data.tiempoMs,
          fallos: data.fallos
        });
        
        if (data.estadoRonda === 'FINALIZADA') {
          this.scoreForm.disable();
        }
        
        this.isLoading = false;
      },
      // --- CORRECCIÓN: Añadir tipo ---
      error: (err: any) => {
        this.errorMessage = "Error al cargar la ronda.";
        this.isLoading = false;
      }
    });
  }

  saveRound(): void {
    if (!this.roundId || this.scoreForm.invalid || this.scoreForm.disabled) {
      this.errorMessage = "El formulario es inválido. Revise los campos.";
      return;
    }

    if (confirm('¿Estás seguro de guardar y finalizar esta ronda? Esta acción no se puede deshacer.')) {
      this.isLoading = true;
      this.errorMessage = null;

      const formData = this.scoreForm.value;
      const payload: UpdateRondaIndividualDTO = {
        puntos: Number(formData.puntos),
        tiempoMs: Number(formData.tiempoMs),
        fallos: Number(formData.fallos),
        estadoRonda: 'FINALIZADA'
      };
      
      this.individualRoundService.updateRound(this.roundId, payload).subscribe({
        // --- CORRECCIÓN: Añadir tipo ---
        next: (updatedRound: RondaIndividualDTO) => {
          this.roundSubject.next(updatedRound);
          this.scoreForm.disable();
          this.isLoading = false;
          alert('Ronda guardada y finalizada exitosamente.');
          this.router.navigate(['../'], { relativeTo: this.route });
        },
        // --- CORRECCIÓN: Añadir tipo ---
        error: (err: any) => {
          this.errorMessage = 'Error al guardar la ronda. ' + (err.error?.message || '');
          this.isLoading = false;
        }
      });
    }
  }
}