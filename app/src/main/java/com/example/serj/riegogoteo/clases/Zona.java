package com.example.serj.riegogoteo.clases;

public class Zona implements Comparable<Zona> {

    private String nombre;
    private double lectura;

    public Zona(String nombre){
        this(nombre, 0);
    }

    public Zona(String nombre, double lectura) {
        this.nombre = nombre;
        this.lectura = lectura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLectura() {
        return lectura;
    }

    public void setLectura(double lectura) {
        this.lectura = lectura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Zona zona = (Zona) o;

        if (!nombre.equals(zona.nombre)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public int compareTo(Zona another) {
        return this.nombre.compareTo(another.nombre);
    }
}
