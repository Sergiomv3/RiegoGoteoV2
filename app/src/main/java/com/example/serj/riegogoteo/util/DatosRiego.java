package com.example.serj.riegogoteo.util;

public class DatosRiego {
    private int goteros;
    private int litrosPorGoteroHora;

    public DatosRiego(){
        this.goteros = 4;
        this.litrosPorGoteroHora = 16;
    }

    public int getGoteros() {
        return goteros;
    }

    public void setGoteros(int goteros) {
        this.goteros = goteros;
    }

    public int getLitrosPorGoteroHora() {
        return litrosPorGoteroHora;
    }

    public void setLitrosPorGoteroHora(int litrosPorGoteroHora) {
        this.litrosPorGoteroHora = litrosPorGoteroHora;
    }
}
