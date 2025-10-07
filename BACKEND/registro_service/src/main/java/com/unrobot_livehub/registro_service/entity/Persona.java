package com.unrobot_livehub.registro_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    // ... otros datos básicos

    // GETTERS MANUALES (temporalmente)
    public String getNombres() {
        return this.nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public String getIdentificacion() {
        return this.identificacion;
    }

    public String getEmail() {
        return this.apellidos;
    }


}
