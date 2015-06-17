package com.example.serj.riegogoteo;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.serj.riegogoteo.clases.Lectura;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class Historial extends ActionBarActivity {
    private ArrayList<Lectura> lecturasFromGet = new ArrayList<Lectura>();
    Lectura lecturasGet;
    private Adaptador adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        String[] peticiones = new String[1]; // SACAMOS PROFESORES
        peticiones[0] = "lecturas";
        System.out.println("voy a lanzar get");
        GetRestFul get = new GetRestFul();
        get.execute(peticiones);
        System.out.println("lanzado");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    lecturasGet = new Lectura(object);
                    lecturasFromGet.add(lecturasGet);
                }
                //System.out.println(lecturasFromGet.size() + " TAMAÑO");
                adapter = new Adaptador(Historial.this, R.layout.elemento, lecturasFromGet);
                final ListView lv = (ListView)findViewById(R.id.listView);
                lv.setAdapter(adapter);
                registerForContextMenu(lv);

            } catch (Exception ex) {
            }
        }
    }
}
