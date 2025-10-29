import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
// 1. Importar los módulos de formularios reactivos
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

// Componentes y Servicios
import { BackButtonComponent } from '../../shared/components/back-button/back-button';
import { TeamService } from './services/team.service';
import { CreateTeamDTO } from '../../core/models/team-registration.model';

@Component({
  selector: 'app-team-registration',
  standalone: true,
  // 2. Añadir ReactiveFormsModule y BackButtonComponent
  imports: [CommonModule, ReactiveFormsModule, BackButtonComponent],
  templateUrl: './team-registration.html',
  styleUrls: ['./team-registration.css']
})
export class TeamRegistrationComponent {

  // Inyección de servicios
  private fb = inject(FormBuilder);
  private teamService = inject(TeamService);
  private router = inject(Router);

  public isLoading = false;
  public errorMessage: string | null = null;
  public successMessage: string | null = null;
  
  // 3. Definir el FormGroup
  public registrationForm: FormGroup;

  constructor() {
    // 4. Inicializar el formulario con validaciones
    this.registrationForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      institucion: ['', [Validators.required]],
      nombreCapitan: ['', [Validators.required]],
      emailCapitan: ['', [Validators.required, Validators.email]]
    });
  }

  // 5. Método para manejar el envío del formulario
  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.errorMessage = "Por favor, completa todos los campos correctamente.";
      // Marcar todos los campos como "tocados" para mostrar errores
      this.registrationForm.markAllAsTouched();
      return;
    }

    if (confirm('¿Estás seguro de registrar este equipo?')) {
      this.isLoading = true;
      this.errorMessage = null;
      this.successMessage = null;

      const payload: CreateTeamDTO = this.registrationForm.value;

      this.teamService.registerTeam(payload).subscribe({
        next: (createdTeam) => {
          this.isLoading = false;
          this.successMessage = `¡Equipo "${createdTeam.nombre}" registrado exitosamente!`;
          // Limpiar el formulario para el próximo registro
          this.registrationForm.reset();
          // Opcionalmente, navegar de vuelta al inicio
          // this.router.navigate(['/']);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al registrar el equipo. ' + (err.error?.message || 'Revisa la consola.');
          this.successMessage = null;
        }
      });
    }
  }

  // Helper para chequear la validación en la plantilla
  isInvalid(controlName: string): boolean {
    const control = this.registrationForm.get(controlName);
    return !!control && control.invalid && (control.dirty || control.touched);
  }
}