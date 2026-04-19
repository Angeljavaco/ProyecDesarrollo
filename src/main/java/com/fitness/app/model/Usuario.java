package com.fitness.app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Usuario {

    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Min(value = 10, message = "La edad debe ser mayor a 10")
    private int edad;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    public Usuario() {}

    public Usuario(int id, String nombre, int edad, String email) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}