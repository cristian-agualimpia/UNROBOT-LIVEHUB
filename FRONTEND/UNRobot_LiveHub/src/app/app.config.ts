import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

// 1. Importar los providers de HTTP y el interceptor
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { apiInterceptor } from './core/interceptors/api.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    
    // 2. Registrar el HttpClient y el Interceptor
    // Esto habilita las llamadas HTTP en toda la app
    // y les aplica la lógica del apiInterceptor.
    provideHttpClient(withInterceptors([apiInterceptor]))
  ]
};