package com.example.serj.riegogoteo.clases;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lectura implements Serializable{

    private Calendar fecha, hi, hf;

    private double metrosCubicosTeoricos;
    private int olivos;
    private String zona, sector;

    @SerializedName("eficiencia")
    private double  rendimiento;

    @SerializedName("contadori")
    private double contadorInicial;

    @SerializedName("contadorf")
    private double  contadorFinal;

    @SerializedName("sector")
    private String idsector;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date strToDate = null;



    public Lectura(Calendar fecha, Calendar hi, Calendar hf, double contadorInicial, double contadorFinal, double metrosCubicosTeoricos, String zona, String idsector, String sector, int olivos, double rendimiento) {
        this.fecha = fecha;
        this.hi = hi;
        this.hf = hf;
        this.contadorInicial = contadorInicial;
        this.contadorFinal = contadorFinal;
        this.metrosCubicosTeoricos = metrosCubicosTeoricos;
        this.zona = zona;
        this.idsector = idsector;
        this.sector = sector;
        this.olivos = olivos;
        this.rendimiento = rendimiento;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public Calendar getHi() {
        return hi;
    }

    public void setHi(Calendar hi) {
        this.hi = hi;
    }

    public Calendar getHf() {
        return hf;
    }

    public void setHf(Calendar hf) {
        this.hf = hf;
    }

    public double getContadorInicial() {
        return contadorInicial;
    }

    public void setContadorInicial(double contadorInicial) {
        this.contadorInicial = contadorInicial;
    }

    public double getContadorFinal() {
        return contadorFinal;
    }

    public void setContadorFinal(double contadorFinal) {
        this.contadorFinal = contadorFinal;
    }

    public double getMetrosCubicosTeoricos() {
        return metrosCubicosTeoricos;
    }

    public void setMetrosCubicosTeoricos(double metrosCubicosTeoricos) {
        this.metrosCubicosTeoricos = metrosCubicosTeoricos;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getIdsector() {
        return idsector;
    }

    public void setIdsector(String idsector) {
        this.idsector = idsector;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getOlivos() {
        return olivos;
    }

    public void setOlivos(int olivos) {
        this.olivos = olivos;
    }

    public double getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(double rendimiento) {
        this.rendimiento = rendimiento;
    }

    public Lectura(JSONObject object) {
        try {
            System.out.println(object.toString()+ "- RECIBIDO");
            this.contadorInicial = object.getDouble("contadorI");
            this.contadorFinal = object.getDouble("contadorF");
            this.rendimiento = object.getDouble("eficiencia");
            try {

                strToDate = format.parse(object.getString("fecha"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            this.fecha = DateToCalendar(strToDate);
            this.idsector = object.getString("sector");
        } catch (Exception ex){}
    }
    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public String toString() {
        return "Lectura{" +
                "fecha=" + fecha +
                ", hi=" + hi +
                ", hf=" + hf +
                ", metrosCubicosTeoricos=" + metrosCubicosTeoricos +
                ", olivos=" + olivos +
                ", zona='" + zona + '\'' +
                ", sector='" + sector + '\'' +
                ", rendimiento=" + rendimiento +
                ", contadorInicial=" + contadorInicial +
                ", contadorFinal=" + contadorFinal +
                ", idsector='" + idsector + '\'' +
                ", format=" + format +
                ", strToDate=" + strToDate +
                '}';
    }
}
