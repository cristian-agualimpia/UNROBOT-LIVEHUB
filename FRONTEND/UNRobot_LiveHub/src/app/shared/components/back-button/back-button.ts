import { Component, inject } from '@angular/core';
import { Location, CommonModule } from '@angular/common'; // 1. Importar Location y CommonModule

@Component({
  selector: 'app-back-button',
  standalone: true,
  imports: [CommonModule], // 2. Añadir CommonModule (necesario para el SVG)
  templateUrl: './back-button.html',
  styleUrls: ['./back-button.css']
})
export class BackButtonComponent {

  // 3. Inyectar el servicio Location
  private location = inject(Location);

  /**
   * Navega a la vista anterior en el historial del navegador.
   */
  goBack(): void {
    this.location.back();
  }
}