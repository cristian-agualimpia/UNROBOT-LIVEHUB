import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaInfoDTO } from '../models/category.model';

@Injectable({
  providedIn: 'root' // Disponible globalmente
})
export class CategoryService {
  
  private http = inject(HttpClient);

  /**
   * Obtiene la lista completa de todas las categorías.
   * Llama a GET /api/v1/categorias-info
   */
  public getCategories(): Observable<CategoriaInfoDTO[]> {
    
    // Nota: Solo ponemos la ruta relativa.
    // El interceptor se encargará de añadir "http://.../api/v1"
    return this.http.get<CategoriaInfoDTO[]>('/categorias-info');
  }
}