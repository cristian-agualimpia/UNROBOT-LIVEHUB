import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.html',
  styleUrls: ['./footer.css']
})
export class FooterComponent {
  // Obtenemos el año actual para mostrarlo en el copyright
  currentYear = new Date().getFullYear();
}