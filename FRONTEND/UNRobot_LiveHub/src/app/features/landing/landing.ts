import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [],
  templateUrl: './landing.html',
  styleUrls: ['./landing.css']
})
export class LandingComponent {
  
  private router = inject(Router);

  // Asegúrate de que esta es la contraseña que estás usando
  private readonly JUDGE_PASSWORD = 'unrobot2025';

  /**
   * Método que se llama al hacer clic en los botones de acción.
   */
  private authenticateJudge(targetRoute: '/scoring' | '/register-team') {
    
    const password = prompt('Por favor, ingresa la contraseña de Juez:');

    // --- INICIO DE DEBUGGING ---
    // Estas líneas nos dirán qué está pasando en la consola (F12)
    console.log('Contraseña ingresada:', password);
   // console.log('Contraseña esperada:', this.JUDGE_PASSWORD);
    console.log('¿Coinciden? (debe ser true):', password === this.JUDGE_PASSWORD);
    // --- FIN DE DEBUGGING ---

    if (password === this.JUDGE_PASSWORD) {
      console.log('¡Contraseña correcta! Navegando a', targetRoute);
      sessionStorage.setItem('isJudge', 'true');
      this.router.navigate([targetRoute]);

    } else if (password !== null) {
      // Si la contraseña no es nula (no fue "Cancelar"), pero no coincidió
      console.error('La contraseña no coincidió.');
      // CORRECCIÓN: Había un error de tipeo, decía "denificado"
      alert('Contraseña incorrecta. Acceso denegado.');
    } else {
      // El usuario presionó "Cancelar"
      console.log('Autenticación cancelada por el usuario.');
    }
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