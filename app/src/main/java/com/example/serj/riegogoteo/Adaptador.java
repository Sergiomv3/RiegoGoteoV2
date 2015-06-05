package com.example.serj.riegogoteo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serj.riegogoteo.clases.Lectura;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sergio on 05/06/2015.
 */
public class Adaptador extends ArrayAdapter {
    private Context contexto;
    private ArrayList<Lectura> lista;
    private int recurso;
    private static LayoutInflater i; // Para no crear inflater cada vez que se llame a getView()

    public static class ViewHolder{
        public TextView rendimiento, idSector, fecha;

    }

    public Adaptador(Context context, int resource, ArrayList<Lectura> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.rendimiento = (TextView)convertView.findViewById(R.id.tvRendimiento);
            vh.idSector = (TextView)convertView.findViewById(R.id.tvSector);
            vh.fecha = (TextView)convertView.findViewById(R.id.tvFecha);


            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.rendimiento.setText("Rendimiento "+ String.valueOf(lista.get(position).getRendimiento())+"%");
        vh.idSector.setText("Sector "+lista.get(position).getIdsector());
        vh.fecha.setText("Fecha regado: "+lista.get(position).getFecha().get(Calendar.YEAR)+"-"+(lista.get(position).getFecha().get(Calendar.MONTH)+1)+"-"+lista.get(position).getFecha().get(Calendar.DAY_OF_MONTH));
        if(lista.get(position).getRendimiento()>70){
            vh.rendimiento.setTextColor(Color.GREEN);
        }else{
            vh.rendimiento.setTextColor(Color.RED);
        }


        return convertView;
    }
}
