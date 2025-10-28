import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- Necesario para *ngIf, *ngFor, async pipe
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CategoriaInfoDTO } from '../../../../core/models/category.model';
import { CategoryService } from '../../../../core/services/category.service';

@Component({
  selector: 'app-scoring-hub',
  standalone: true,
  // 1. Importamos CommonModule
  imports: [CommonModule],
  templateUrl: './scoring-hub.html',
  styleUrls: ['./scoring-hub.css']
})
export class ScoringHubComponent implements OnInit {
  
  // 2. Inyectamos los servicios
  private categoryService = inject(CategoryService);
  private router = inject(Router);

  // 3. Creamos un Observable para la lista de categorías
  public categories$!: Observable<CategoriaInfoDTO[]>;

  ngOnInit(): void {
    // 4. En cuanto el componente carga, pedimos las categorías
    this.categories$ = this.categoryService.getCategories();
  }

  /**
   * Navega a la lista de rondas de la categoría seleccionada
   */
  selectCategory(category: CategoriaInfoDTO): void {
    // 5. Usamos los datos para navegar a la ruta dinámica que definimos
    this.router.navigate([
      '/scoring/list',
      category.tipoLogica, // :categoryType (ENFRENTAMIENTO o RONDA_INDIVIDUAL)
      category.tipo         // :categoryId (ej: BOLABOT)
    ]);
  }
}