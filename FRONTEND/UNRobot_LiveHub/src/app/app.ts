import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// CORREGIDO: Se quitó .component de las rutas
import { HeaderComponent } from './shared/components/header/header';
import { FooterComponent } from './shared/components/footer/footer';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, 
    HeaderComponent, 
    FooterComponent
  ],
  // CORREGIDO: Se cambió a .html y .css (como mencionaste)
  templateUrl: './app.html',
  styleUrls: ['./app.css'], 
})
export class AppComponent {
  title = 'UNRobot_LiveHub';
}