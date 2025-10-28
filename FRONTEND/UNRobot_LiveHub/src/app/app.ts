import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Importamos tus componentes reusables de Header y Footer
import { HeaderComponent } from './shared/components/header/header.component';
import { FooterComponent } from './shared/components/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  // 1. Añadimos los componentes y el RouterOutlet a la lista de imports
  imports: [
    RouterOutlet, 
    HeaderComponent, 
    FooterComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'UNRobot_LiveHub'; // Puedes mantener esto o quitarlo
}