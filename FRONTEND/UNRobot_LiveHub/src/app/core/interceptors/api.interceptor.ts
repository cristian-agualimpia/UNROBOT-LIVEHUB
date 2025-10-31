import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
// Ya no necesitamos 'environment' para la URL

/**
 * Interceptor funcional que construye dinámicamente la URL base de la API
 * y maneja errores HTTP de forma centralizada.
 */
export const apiInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  // --- LÓGICA DINÁMICA ---
  // 1. Define el puerto del backend
  const BACKEND_PORT = 8081; 
  
  // 2. Obtiene el hostname de la barra de direcciones del navegador
  //    - Si estás en la PC, esto será "localhost"
  //    - Si estás en el celular, esto será "192.168.1.10" (o la IP que sea)
  const backendHost = window.location.hostname; 

  // 3. Construye la URL base de la API dinámicamente
  const baseUrl = `http://${backendHost}:${BACKEND_PORT}/api/v1`;
  // --- FIN LÓGICA DINÁMICA ---


  // 4. Clona la petición y le añade la URL base
  const apiReq = req.clone({
    url: `${baseUrl}${req.url}`
  });

  // 5. Envía la petición y maneja errores (sin cambios)
  return next(apiReq).pipe(
    catchError((err: any) => {
      if (err instanceof HttpErrorResponse) {
        console.error(`Error ${err.status}: ${err.message}`);
      } else {
        console.error('Un error inesperado ha ocurrido:', err);
      }
      return throwError(() => err);
    })
  );
};