package com.example.serj.riegogoteo;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.db4o.query.Predicate;
import com.example.serj.riegogoteo.clases.Lectura;
import com.example.serj.riegogoteo.clases.Sector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Averia extends ActionBarActivity implements Serializable {
    private Spinner spSectores;
    private List<Sector> listaSectores;
    private Sector sectorActual = new Sector(null,null,0,null,null,null);
    private String URLBASE = "http://riego.pthernandez.es/rest/";
    private EditText descrip;



    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date strToDate = null;

    //{"sector":"s1","id":"19","titulo":"","estado":"nueva","fecha":"2015-06-06","usuario":"1","lg":"84546","descripcion":"Ronal Mcdonald se come una burguer","lt":"200"}]

    @SerializedName("sector")
    private String idSector;

    @SerializedName("titulo")
    private String titulo = "AVERIA";

    @SerializedName("estado")
    private String estado;

    @SerializedName("fecha")
    private Calendar fecha;

    @SerializedName("usuario")
    private String usuario = "1";

    @SerializedName("lg")
    private double longitud;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("lt")
    private double latitud;

    private TextView tvLatitud, tvLongitud;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_averia);
        descrip = (EditText)findViewById(R.id.etDescripcion);
        LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener milocListener = new MiLocationListener();
        milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
        tvLatitud = (TextView)findViewById(R.id.tvLatitud);
        tvLongitud = (TextView)findViewById(R.id.tvLongitud);
        spSectores = (Spinner)findViewById(R.id.spinner);
        listaSectores = getSectoresAveria();
        ArrayAdapter<Sector> adapter = new ArrayAdapter<Sector>(Averia.this,
                android.R.layout.simple_spinner_item, listaSectores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSectores.setAdapter(adapter);
        spSectores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectorActual = listaSectores.get(position);
                Log.v("SECTOR ACTUAL:", sectorActual.toString());
                idSector = sectorActual.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button button = (Button) findViewById(R.id.btEnviarAveria);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enviar();
            }
        });
    }
    public class MiLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            String coordenadas = "Mis coordenadas son: " + "Latitud = " + loc.getLatitude() + "Longitud = " + loc.getLongitude();
            //Toast.makeText( getApplicationContext(),coordenadas,Toast.LENGTH_SHORT).show();
            tvLatitud.setText("LAT: "+loc.getLatitude());
            tvLongitud.setText("LONG: "+loc.getLongitude());
        }
        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(),"Gps Desactivado",Toast.LENGTH_SHORT ).show();
        }
        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    public static List<Sector> getSectoresAveria(){
        List<Sector> sectores = Principal.bd.query(
                new Predicate<Sector>() {
                    @Override
                    public boolean match(Sector s) {
                        return s.getDiaDeRiego() != null;
                    }
                });
        return sectores;
    }

    private void enviar(){
        Averia averiaEnviar = new Averia();
        averiaEnviar.setDescripcion(descrip.getText().toString());
        averiaEnviar.setIdSector(sectorActual.getId());
        averiaEnviar.setLatitud(latitud);
        averiaEnviar.setLongitud(longitud);
        averiaEnviar.setUsuario(usuario);
        subirAveriaServidor(averiaEnviar);
    }
    private void subirAveriaServidor(Averia averia) {
        Gson gson = new Gson();
        Averia averiaEnviada = averia;
        //String toSend = gson.toJson(lecturafinal);
        JsonObject object = new JsonObject();
        object.addProperty("sector", averiaEnviada.getIdSector());
        object.addProperty("usuario", averiaEnviada.getUsuario());
        object.addProperty("lt",String.valueOf(averiaEnviada.getLatitud()));
        object.addProperty("lg",String.valueOf(averiaEnviada.getLongitud()));
        object.addProperty("titulo",titulo);
        object.addProperty("descripcion",averiaEnviada.getDescripcion());
        ParametrosPost p = new ParametrosPost();
        p.url = URLBASE+"reportes";

        p.object = object;
        PostRestFul pr = new PostRestFul();
        pr.execute(p);

    }
    static class ParametrosPost{
        String url;
        JsonObject object;
    }

    private class PostRestFul extends AsyncTask<ParametrosPost, Void, String> {

        @Override
        protected String doInBackground(ParametrosPost... s) {
            String r = ClientRestFul.post(s[0].url, s[0].object);
            System.out.println("JSON ENVIADO "+s[0].object.toString() + " A LA URL: "+s[0].url);
            Log.v("Añadir",r);
            return r;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                System.out.println("AVERÍA ENVIADA");
                Toast.makeText(Averia.this,"AVERÍA ENVIADA",Toast.LENGTH_LONG).show();
                //finish();
            }catch(Exception ex){}

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Averia.this,"ENVIANDO...",Toast.LENGTH_SHORT).show();
        }
    }

    public String getIdSector() {
        return idSector;
    }

    public void setIdSector(String idSector) {
        this.idSector = idSector;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TextView getTvLatitud() {
        return tvLatitud;
    }

    public void setTvLatitud(TextView tvLatitud) {
        this.tvLatitud = tvLatitud;
    }

    public TextView getTvLongitud() {
        return tvLongitud;
    }

    public void setTvLongitud(TextView tvLongitud) {
        this.tvLongitud = tvLongitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    //{"sector":"s1","id":"19","titulo":"","estado":"nueva","fecha":"2015-06-06","usuario":"1","lg":"84546","descripcion":"Ronal Mcdonald se come una burguer","lt":"200"}]

    public Averia(JSONObject object) {
        try {
            System.out.println(object.toString()+ "- RECIBIDO");
            this.idSector = object.getString("sector");
            this.titulo = object.getString("titulo");
            this.estado = object.getString("estado");
            this.descripcion = object.getString("descripcion");
            this.longitud = Double.valueOf(object.getString("lg"));
            this.latitud = Double.valueOf(object.getString("lt"));
            try {

                strToDate = format.parse(object.getString("fecha"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            this.fecha = DateToCalendar(strToDate);
            this.usuario = object.getString(usuario);




        } catch (Exception ex){}
    }
    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public Averia() {

    }
}
