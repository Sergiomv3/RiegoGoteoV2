package com.example.serj.riegogoteo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.serj.riegogoteo.clases.Lectura;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class Historial_averias extends ActionBarActivity {
    private ArrayList<Averia> averiasFromGet = new ArrayList<Averia>();
    Averia averiaGet;
    private Adaptador2 adaptador2;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_averias);
         lv = (ListView)findViewById(R.id.listViewAverias);
        String[] peticiones = new String[1];
        peticiones[0] = "reportes";
        System.out.println("voy a lanzar get");
        GetRestFul get = new GetRestFul();
        get.execute(peticiones);
        System.out.println("lanzado");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Averia averiaItem =(Averia) parent.getItemAtPosition(position);
                //System.out.println(averiaItem.getLatitud());
                Intent intent = new Intent(Historial_averias.this,MapsActivity.class);
                intent.putExtra("lat",averiaItem.getLatitud());
                intent.putExtra("lon",averiaItem.getLongitud());
                intent.putExtra("descripcion",averiaItem.getDescripcion());
                startActivity(intent);
            }
        });
    }



    private class GetRestFul extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... s) {
            String[] r = new String[s.length];
            int i = 0;
            for(String a: s){
                r[i] = ClientRestFul.get(Principal.URLBASE+a);
                i++;
            }
            return r;
        }

        @Override
        protected void onPostExecute(String... s) {
            super.onPostExecute(s);
            JSONTokener token = new JSONTokener(s[0]);
            try {
                //JSONObject root = new JSONObject(tokener); no sirve
                JSONArray array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    averiaGet = new Averia(object);
                    averiasFromGet.add(averiaGet);

                }
                //System.out.println(lecturasFromGet.size() + " TAMAÑO");
                adaptador2 = new Adaptador2(Historial_averias.this, R.layout.elemento2, averiasFromGet);
                lv = (ListView)findViewById(R.id.listViewAverias);
                lv.setAdapter(adaptador2);
                registerForContextMenu(lv);


            } catch (Exception ex) {
            }
        }
    }

}
