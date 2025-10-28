import { Component } from '@angular/core';
import { RouterLink } from '@angular/router'; // Importamos RouterLink para la navegación

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterLink // Añadimos RouterLink a los imports
  ],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class HeaderComponent {
  // No necesitamos lógica aquí por ahora, solo es presentación
}