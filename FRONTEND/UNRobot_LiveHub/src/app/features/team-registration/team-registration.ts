import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
// 1. Importar FormArray
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

// Componentes y Servicios
import { BackButtonComponent } from '../../shared/components/back-button/back-button';
import { TeamService } from './services/team.service';
import { CreateTeamDTO } from '../../core/models/team-registration.model';
import { CategoryService } from '../../core/services/category.service';
import { CategoriaInfoDTO } from '../../core/models/category.model';


@Component({
  selector: 'app-team-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, BackButtonComponent],
  templateUrl: './team-registration.html',
  styleUrls: ['./team-registration.css']
})
export class TeamRegistrationComponent implements OnInit {

  private fb = inject(FormBuilder);
  private teamService = inject(TeamService);
  private router = inject(Router);
  private categoryService = inject(CategoryService); 

  public isLoading = false;
  public errorMessage: string | null = null;
  public successMessage: string | null = null;
  
  public registrationForm: FormGroup;
  public categories$!: Observable<CategoriaInfoDTO[]>;

  constructor() {
    // 2. Actualizar el FormGroup para que coincida con el DTO/JSON
    this.registrationForm = this.fb.group({
      // --- Campos de Equipo ---
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      institucion: [''], // Opcional
      categoriaTipo: ['', [Validators.required]],

      // --- Campos de Capitán ---
      nombreCapitan: ['', [Validators.required]],
      emailCapitan: ['', [Validators.required, Validators.email]],
      telefonoCapitan: [''], // Opcional

      // --- Lista de Miembros ---
      miembros: this.fb.array([]) // 3. Inicializar como FormArray vacío
    });
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.getCategories();
  }

  // 4. Helper para obtener el FormArray de 'miembros' en el template
  get miembros(): FormArray {
    return this.registrationForm.get('miembros') as FormArray;
  }

  // 5. Método para AÑADIR un nuevo campo de miembro
  addMember(): void {
    // Añadimos un nuevo FormControl al FormArray
    const memberControl = this.fb.control('', Validators.required);
    this.miembros.push(memberControl);
  }

  // 6. Método para QUITAR un miembro en un índice específico
  removeMember(index: number): void {
    this.miembros.removeAt(index);
  }

  // 7. onSubmit (Ahora enviará el JSON completo)
  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.errorMessage = "Por favor, completa todos los campos correctamente.";
      this.registrationForm.markAllAsTouched();
      return;
    }
    
    if (confirm('¿Estás seguro de registrar este equipo?')) {
      this.isLoading = true;
      this.errorMessage = null;
      this.successMessage = null;

      // El .value del form ahora coincide perfectamente con tu JSON de prueba
      const payload: CreateTeamDTO = this.registrationForm.value;

      this.teamService.registerTeam(payload).subscribe({
        next: (createdTeam) => {
          this.isLoading = false;
          this.successMessage = `¡Equipo "${createdTeam.nombre}" registrado exitosamente!`;
          
          // Limpiar el formulario
          this.registrationForm.reset();
          // 8. Vaciar el FormArray manualmente
          this.miembros.clear(); 
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Error al registrar el equipo. ' + (err.error?.message || 'Revisa la consola.');
          this.successMessage = null;
        }
      });
    }
  }

  isInvalid(controlName: string): boolean {
    const control = this.registrationForm.get(controlName);
    return !!control && control.invalid && (control.dirty || control.touched);
  }
}