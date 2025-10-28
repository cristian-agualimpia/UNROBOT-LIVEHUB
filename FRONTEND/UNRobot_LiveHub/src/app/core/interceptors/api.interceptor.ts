import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment.development';

/**
 * Interceptor funcional que añade la URL base de la API
 * y maneja errores HTTP de forma centralizada.
 */
export const apiInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  
  // 1. Clona la petición y le añade la URL base
  const apiReq = req.clone({
    url: `${environment.apiUrl}${req.url}`
  });

  // 2. Envía la petición y maneja errores
  return next(apiReq).pipe(
    catchError((err: any) => {
      if (err instanceof HttpErrorResponse) {
        // Loguear errores en la consola
        console.error(`Error ${err.status}: ${err.message}`);
        // Aquí podrías añadir un servicio de Notificaciones (toast)
        // ej: this.notificationService.showError('Error de conexión');
      } else {
        console.error('Un error inesperado ha ocurrido:', err);
      }
      // Propaga el error
      return throwError(() => err);
    })
  );
};