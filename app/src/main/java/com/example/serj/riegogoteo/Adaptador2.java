package com.example.serj.riegogoteo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.serj.riegogoteo.clases.Lectura;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sergio on 05/06/2015.
 */
public class Adaptador2 extends ArrayAdapter {
    private Context contexto;
    private ArrayList<Averia> lista;
    private int recurso;
    private static LayoutInflater i; // Para no crear inflater cada vez que se llame a getView()

    public static class ViewHolder{
        public TextView tvEstado;
        public TextView tvFecha2;
        public TextView tvTitulo2;
        public TextView tvUsuario2;
        public TextView tvDescripcion2;
        public TextView tvLt2;
        public TextView tvLg2;


    }

    public Adaptador2(Context context, int resource, ArrayList<Averia> objects) {
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
            vh.tvEstado = (TextView)convertView.findViewById(R.id.tvEstado2);
            vh.tvFecha2 = (TextView)convertView.findViewById(R.id.tvFecha2);
            vh.tvTitulo2 =(TextView)convertView.findViewById(R.id.tvTitulo2);
            vh.tvUsuario2 = (TextView)convertView.findViewById(R.id.tvUsuario2);
            vh.tvDescripcion2 = (TextView)convertView.findViewById(R.id.tvDrescripcion2);
            vh.tvLt2 = (TextView)convertView.findViewById(R.id.tvLt2);
            vh.tvLg2 = (TextView)convertView.findViewById(R.id.tvLg2);


            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvEstado.setText(lista.get(position).getEstado());
        vh.tvFecha2.setText(lista.get(position).getFecha().get(Calendar.YEAR)+"-"+(lista.get(position).getFecha().get(Calendar.MONTH)+1)+"-"+lista.get(position).getFecha().get(Calendar.DAY_OF_MONTH));
        vh.tvTitulo2.setText(lista.get(position).getTitulo());
        vh.tvUsuario2.setText("Reportado por: "+lista.get(position).getUsuario());
        vh.tvDescripcion2.setText(lista.get(position).getDescripcion());
        vh.tvLt2.setText("Lat: "+String.valueOf(lista.get(position).getLatitud()));
        vh.tvLg2.setText("Lon: " + String.valueOf(lista.get(position).getLongitud()));
        if(lista.get(position).getEstado().equalsIgnoreCase("nueva")){
            vh.tvEstado.setTextColor(Color.RED);
        }else{
            vh.tvEstado.setTextColor(Color.rgb(76,168,43));
        }



        return convertView;
    }
}
