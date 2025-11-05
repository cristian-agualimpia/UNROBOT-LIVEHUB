import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackButtonComponent } from '../../../../shared/components/back-button/back-button';
// CORRECCIÓN: Importar el DTO de UPDATE
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
  
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder); 
  private individualRoundService = inject(IndividualRoundService);

  private roundId: string | null = null;
  
  // CORRECCIÓN: Hacer PÚBLICO para que el HTML lo vea
  public roundSubject = new BehaviorSubject<RondaIndividualDTO | null>(null);
  public round$ = this.roundSubject.asObservable();

  public isLoading = true;
  public errorMessage: string | null = null;
  
  public scoreForm: FormGroup;

  constructor() {
    // CORRECCIÓN: Usar los campos del nuevo DTO
    this.scoreForm = this.fb.group({
      tiempoMs: [0, [Validators.required, Validators.min(0)]], 
      penalizaciones: [0, [Validators.required, Validators.min(0)]],
      etiquetaRonda: ['', Validators.required]
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
      next: (data) => {
        this.roundSubject.next(data);
        
        // CORRECCIÓN: Rellenar el formulario con los datos correctos
        this.scoreForm.patchValue({
          tiempoMs: data.tiempoMs,
          penalizaciones: data.penalizaciones,
          etiquetaRonda: data.etiquetaRonda
        });
        
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = "Error al cargar la ronda.";
        this.isLoading = false;
      }
    });
  }

  saveRound(): void {
    if (!this.roundId || this.scoreForm.invalid) {
      this.errorMessage = "El formulario es inválido. Revise los campos.";
      return;
    }

    if (confirm('¿Estás seguro de ACTUALIZAR esta ronda?')) {
      this.isLoading = true;
      this.errorMessage = null;

      // CORRECCIÓN: Usar el DTO de UPDATE
      const formData = this.scoreForm.value;
      const payload: UpdateRondaIndividualDTO = {
        tiempoMs: Number(formData.tiempoMs),
        penalizaciones: Number(formData.penalizaciones),
        etiquetaRonda: formData.etiquetaRonda
      };
      
      this.individualRoundService.updateRound(this.roundId, payload).subscribe({
        next: (updatedRound) => {
          this.roundSubject.next(updatedRound);
          this.isLoading = false;
          alert('Ronda actualizada exitosamente.');
          // Navegar de vuelta a la lista
          this.router.navigate(['/scoring/hub', updatedRound.categoriaTipo, updatedRound.categoriaTipo]);
        },
        error: (err) => {
          this.errorMessage = 'Error al guardar la ronda. ' + (err.error?.message || '');
          this.isLoading = false;
        }
      });
    }
  }
}