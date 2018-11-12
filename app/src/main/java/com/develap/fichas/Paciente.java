package com.develap.fichas;

public class Paciente {

    public String id, apellido, nombre, direccion, ficha, telefono, fechanac;


    public Paciente(){}

    public Paciente(String id, String apellido, String nombre, String fechanac, String ficha, String telefono)
    {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechanac = fechanac;
        this.ficha = ficha;
        this.telefono = telefono;
    }

    public Paciente(String id, String apellido, String nombre, String fechanac, String direccion, String ficha, String telefono)
    {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechanac = fechanac;
        this.direccion = direccion;
        this.ficha = ficha;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechanac() {
        return fechanac;
    }

    public void setFechanac(String fechanac) {
        this.fechanac = fechanac;
    }
}
