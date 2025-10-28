import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
// CORREGIDO: Asegúrate de que importa AppComponent
import { AppComponent } from './app/app'; 

bootstrapApplication(AppComponent, appConfig) // ...y que usa AppComponent aquí
  .catch((err) => console.error(err));