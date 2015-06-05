package com.example.serj.riegogoteo;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Averia extends ActionBarActivity {

    private double latitud;


    private double longitud;


    private String descripcion;


    private TextView tvLatitud, tvLongitud;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_averia);
        LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener milocListener = new MiLocationListener();
        milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
        tvLatitud = (TextView)findViewById(R.id.tvLatitud);
        tvLongitud = (TextView)findViewById(R.id.tvLongitud);
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


}
