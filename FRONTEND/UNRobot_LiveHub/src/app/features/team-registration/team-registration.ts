import { Component, inject, OnInit } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs'; 
import { BackButtonComponent } from '../../shared/components/back-button/back-button';
import { TeamService, CreateTeamDTO } from './services/team.service'; // Asume que team.service está en la misma carpeta
import { CategoryService } from '../../core/services/category.service';
import { CategoriaInfoDTO } from '../../core/models/category.model';


@Component({
  selector: 'app-team-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, BackButtonComponent],
  templateUrl: './team-registration.html',
  styleUrls: ['./team-registration.css']
})
// CORRECCIÓN: Faltaba 'export'
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
    this.registrationForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      institucion: [''], 
      categoriaTipo: ['', [Validators.required]],
      nombreCapitan: ['', [Validators.required]],
      emailCapitan: ['', [Validators.required, Validators.email]],
      telefonoCapitan: [''], 
      miembros: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.getCategories();
  }

  get miembros(): FormArray {
    return this.registrationForm.get('miembros') as FormArray;
  }

  addMember(): void {
    const memberControl = this.fb.control('', Validators.required);
    this.miembros.push(memberControl);
  }

  removeMember(index: number): void {
    this.miembros.removeAt(index);
  }

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
      const payload: CreateTeamDTO = this.registrationForm.value;

      this.teamService.registerTeam(payload).subscribe({
        next: (createdTeam) => {
          this.isLoading = false;
          this.successMessage = `¡Equipo "${createdTeam.nombre}" registrado exitosamente!`;
          this.registrationForm.reset();
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