package com.example.serj.riegogoteo.clases;

import com.example.serj.riegogoteo.util.Dia;
import com.example.serj.riegogoteo.util.Riego;

public class Sector implements Comparable<Sector>{

    private String id, nombre;
    private int olivos;
    private Dia diaDeRiego;
    private Riego metodoDeRiego;
    private Zona zona;

    public Sector(String id, String nombre, int olivos, Dia diaDeRiego, Riego metodoDeRiego, Zona zona) {
        this.id = id;
        this.nombre = nombre;
        this.olivos = olivos;
        this.diaDeRiego = diaDeRiego;
        this.metodoDeRiego = metodoDeRiego;
        this.zona = zona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOlivos() {
        return olivos;
    }

    public void setOlivos(int olivos) {
        this.olivos = olivos;
    }

    public Dia getDiaDeRiego() {
        return diaDeRiego;
    }

    public void setDiaDeRiego(Dia diaDeRiego) {
        this.diaDeRiego = diaDeRiego;
    }

    public Riego getMetodoDeRiego() {
        return metodoDeRiego;
    }

    public void setMetodoDeRiego(Riego metodoDeRiego) {
        this.metodoDeRiego = metodoDeRiego;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sector sector = (Sector) o;

        if (!id.equals(sector.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Sector another) {
        return this.id.compareTo(another.id);
    }

    @Override
    public String toString() {
        return "Sector{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", olivos=" + olivos +
                ", diaDeRiego=" + diaDeRiego +
                ", metodoDeRiego=" + metodoDeRiego +
                ", zona=" + zona +
                '}';
    }
}
