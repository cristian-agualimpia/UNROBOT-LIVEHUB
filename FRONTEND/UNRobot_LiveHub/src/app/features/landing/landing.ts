import { Component, inject } from '@angular/core';
import { Router } from '@angular/router'; // 1. Importar el Router

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [],
  templateUrl: './landing.html',
  styleUrls: ['./landing.css']
})
export class LandingComponent {
  
  // 2. Inyectar el Router
  private router = inject(Router);

  // --- Esta es la contraseña "maestra" para los jueces ---
  // --- Cámbiala por algo que solo los jueces sepan ---
  private readonly JUDGE_PASSWORD = 'unrobot2025';

  /**
   * Método que se llama al hacer clic en los botones de acción.
   * Pide la contraseña y redirige si es correcta.
   * @param targetRoute La ruta a la que se navegará si el login es exitoso
   */
  private authenticateJudge(targetRoute: '/scoring' | '/register-team') {
    
    // 3. Pedir la contraseña al usuario
    const password = prompt('Por favor, ingresa la contraseña de Juez:');

    if (password === this.JUDGE_PASSWORD) {
      // 4. ÉXITO: Guardamos la bandera en sessionStorage
      sessionStorage.setItem('isJudge', 'true');
      
      // 5. Navegamos a la ruta deseada
      this.router.navigate([targetRoute]);

    } else if (password !== null) {
      // 6. ERROR: La contraseña fue incorrecta (y no fue "cancelar")
      alert('Contraseña incorrecta. Acceso denegado.');
    }
    // Si el usuario da "cancelar" (password === null), no hacemos nada.
  }

  /**
   * Método público para el botón de ir a Puntuaciones
   */
  goToScoring() {
    this.authenticateJudge('/scoring');
  }

  /**
   * Método público para el botón de ir a Registro de Equipos
   */
  goToRegistration() {
    this.authenticateJudge('/register-team');
  }
}