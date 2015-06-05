package com.example.serj.riegogoteo.util;

import java.util.Calendar;

public class Fecha {

    private Calendar c;
    private int dia, mes, anio, hora, min, diaSemana;

    public Fecha(){
        c = Calendar.getInstance();
        c.set(2015, 2, 7);  //cambiar dia...
        this.dia = c.get(Calendar.DAY_OF_MONTH);
        this.mes = c.get(Calendar.MONTH)+1;
        this.anio = c.get(Calendar.YEAR);
        this.hora = c.get(Calendar.HOUR_OF_DAY);
        this.min = c.get(Calendar.MINUTE);
        this.diaSemana = c.get(Calendar.DAY_OF_WEEK);
    }

    public Dia getDiaSemana(){
        return Dia.values()[diaSemana-1];
    }
}
