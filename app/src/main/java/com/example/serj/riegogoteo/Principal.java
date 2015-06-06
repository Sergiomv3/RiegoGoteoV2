package com.example.serj.riegogoteo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.example.serj.riegogoteo.clases.Lectura;
import com.example.serj.riegogoteo.clases.Sector;
import com.example.serj.riegogoteo.clases.Zona;
import com.example.serj.riegogoteo.util.DatosRiego;
import com.example.serj.riegogoteo.util.Dia;
import com.example.serj.riegogoteo.util.Fecha;
import com.example.serj.riegogoteo.util.Riego;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.todddavies.components.progressbar.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Principal extends ActionBarActivity {

    public static ObjectContainer bd;
    private Spinner spSectores;
    private List<Sector> listaSectores;
    private Sector sectorActual;
    private Switch swEmpezar;
    private EditText etInicial, etFinal;
    private ProgressWheel pw;
    public static String URLBASE = "http://riego.pthernandez.es/rest/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        conexionBD();
        inicializar();
        pw = (ProgressWheel) findViewById(R.id.progreso);
    }

    private void inicializar() {
        Fecha f = new Fecha();
        Log.v("FECHA", f.getDiaSemana()+"");
        listaSectores = getSectoresDiaSemana(f.getDiaSemana());
        spSectores = (Spinner)findViewById(R.id.spinnerZonas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Principal.this,
                android.R.layout.simple_spinner_item, getSectoresDiaSemana(listaSectores));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSectores.setAdapter(adapter);
        spSectores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectorActual = listaSectores.get(position);
                Log.v("SECTOR ACTUAL:", sectorActual.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etInicial = (EditText)findViewById(R.id.etLecturaI);
        etFinal = (EditText)findViewById(R.id.etLecturaF);
        // ---Switch---
        // 1.- Iniciar riego solo si esta primera lectura y no esta ultima lectura
        // 2.- Buscar una lectura incompleta y presentar si no (1)
        // 3.- Guardar si hay ultima lectura
        swEmpezar = (Switch)findViewById(R.id.swEmpezar);
        swEmpezar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // No esta bien escrito mu feo

                if(isChecked) {
                // Se va a encender. Comprobar que cumple condiciones para empezar si no volver.
                    if((etInicial.getText().toString().length() > 0 && etFinal.getText().length()==0)){
                        swEmpezar.setChecked(true);
                        insertarBD();
                        System.out.println("EMPEZAR");
                    }else{
                        swEmpezar.setChecked(false);
                        System.out.println("NO HAY LITROS INICIALES");
                    }
                } else {
                // Se va a apagar. Comprobar que cumple condiciones para apagar. SI no volver
                    if(etFinal.getText().length()>0){ // Y FALTA COMPROBAR QUE EXISTA EL OBJETO
                        swEmpezar.setChecked(false);
                        System.out.println("TERMINAR");
                        terminarInsertarBD();
                    }else{
                        swEmpezar.setChecked(true);
                        System.out.println("NO HAY LITROS FINALES O NO EXISTE EL OBJETO");
                    }
                }
                /*if(isChecked && (etInicial.getText().toString().length() == 0 ||
                        etFinal.getText().toString().length() > 0)){
                    swEmpezar.setChecked(false);
                    System.out.println("PASO1");
                    // SI NO HAY NINGUN DATO
                }

                else {
                    if(isChecked){
                        insertarBD();
                        System.out.println("PASO 2");
                        // SI HAY LITROS INICIALES EN PANTALLA
                    }
                    if (!isChecked && etFinal.getText().toString().length() == 0) {
                        swEmpezar.setChecked(true);
                        System.out.println("PASO 3");
                        // ANULA LA POSIBLIDAD DE PARAR SI FALTAN LOS LITROS FINALES
                    }

                }*/
            }
        });
    }

    private void terminarInsertarBD() {
        Lectura lecturafinal = null;
        /*ObjectContainer bd = Db4oEmbedded.openFile
                (Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) +
                        "/bd.db4o");*/

        ObjectSet<Lectura> lecturas = bd.query(
                new Predicate<Lectura>() {
                    @Override
                    public boolean match(Lectura lec) {
                        return lec.getFecha().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                                lec.getFecha().get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) && lec.getContadorFinal()==0
                                && lec.getIdsector() == sectorActual.getId();
                    }
                });
        if(!lecturas.isEmpty()){
        for(Lectura lectura: lecturas){
             lecturafinal = lectura;
            //System.out.println("LITROS INIT: " +lecturafinal.getContadorInicial()+" NOMBRE SECTOR: "+lecturafinal.getSector());
        }
            int minutosInicio = lecturafinal.getHi().get(Calendar.HOUR)*60 + lecturafinal.getHi().get(Calendar.MINUTE);
            int minutosActuales= Calendar.getInstance().get(Calendar.HOUR)*60 + Calendar.getInstance().get(Calendar.MINUTE);
            double diferenciaMinutos = minutosActuales - minutosInicio;
            double litrosMin = (diferenciaMinutos/60)*16;
            double litrosTeóricos = lecturafinal.getOlivos()*litrosMin;
            double m3Teoricos = litrosTeóricos/1000; // DATO m3 A INSERTAR
            System.out.println("DIF TEMP= " +diferenciaMinutos+ "m3 TEORICOS: "+m3Teoricos + "LITROS AL MIN"+ litrosMin);
            // RENDIMIENTO = (m3Reales/m3Teóricos) * 100
            double rendimiento = (m3Teoricos/((Double.valueOf(etFinal.getText().toString()))-Double.valueOf(etInicial.getText().toString())))*100; // DATO TAMBIÉN A INSERTAR
            System.out.println("RENDIMIENTO = "+rendimiento+" %");
            moverProgreso(rendimiento);
            if(rendimiento<70){
                Toast.makeText(this,"Alerta: rendimiento muy pobre",Toast.LENGTH_LONG).show();
            }
            lecturafinal.setHf(Calendar.getInstance());
            lecturafinal.setContadorFinal(Double.valueOf(etFinal.getText().toString()));
            lecturafinal.setMetrosCubicosTeoricos(m3Teoricos);
            /* REDONDEAR */
            double aux2=Math.rint(rendimiento*100)/100;
            rendimiento=aux2;
            lecturafinal.setRendimiento(rendimiento);
            bd.store(lecturafinal);
            bd.commit();
            subirLecturaServidor(lecturafinal);
        }else{
            swEmpezar.setChecked(true);
            Toast.makeText(this,"No hay ningúna lectura asociada (SECTOR ERRÓNEO)",Toast.LENGTH_SHORT).show();
        }




    }



    public void moverProgreso(double rend){
        Timer temporizador = new Timer();
        temporizador.schedule(new RemindTask(rend),100);
    }
    class RemindTask extends TimerTask {
        private int progresoRendimiento = 0;
        private double porcentaje;
        private double porcentaje2;
        private double rendimientoTexto;
        public RemindTask(double rend) {
         this.rendimientoTexto = rend;
         this.porcentaje = (rend*360)/100;
         this.porcentaje2 = (rend*360)/100;
        }

        public void run() {

            while (progresoRendimiento<porcentaje2) {
                progresoRendimiento++;
                porcentaje = progresoRendimiento;
                porcentaje = ((porcentaje/360)*100);
                double aux=Math.rint(porcentaje*100)/100;
                porcentaje=aux;

                pw.setText(String.valueOf(porcentaje+" %"));
                pw.setProgress(progresoRendimiento);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    }
    private void subirLecturaServidor(Lectura lecturafinal) {
        Gson gson = new Gson();
        Lectura lecturaEnviada = lecturafinal;
        //String toSend = gson.toJson(lecturafinal);
        JsonObject object = new JsonObject();
        object.addProperty("contadori", String.valueOf(lecturaEnviada.getContadorInicial()));
        object.addProperty("contadorf", String.valueOf(lecturaEnviada.getContadorFinal()));
        object.addProperty("eficiencia",String.valueOf(lecturaEnviada.getRendimiento()));
        object.addProperty("sector",lecturaEnviada.getIdsector());
        ParametrosPost p = new ParametrosPost();
        p.url = URLBASE+"lectura";

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
                System.out.println("ENVIADO");
                Toast.makeText(Principal.this,"ENVIADO AL SERVIDOR",Toast.LENGTH_LONG).show();
                //finish();
            }catch(Exception ex){}

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Principal.this,"ENVIANDO...",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        desconexionBD();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_historial) {
            Intent intent = new Intent(Principal.this, Historial.class);
            startActivity(intent);

        }
        if (id == R.id.action_averia){
            Intent intent = new Intent(Principal.this, Averia.class);
            startActivity(intent);
        }
        if (id == R.id.action_historial_averia){
            Intent intent = new Intent(Principal.this, Historial_averias.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    private void conexionBD(){
        bd = Db4oEmbedded.openFile(getConfiguracionDb4o(), getExternalFilesDir(null) + "/bd.db4o");
        Log.v("conexionBD", "bd abierta");
        if(!isInicializadaBD()){
            inicializarBD();
            Log.v("conexionBD", "bd inicializada");
            listarBD();
        }
        // Comprobar que no hay lecturas incompletas
        getOpIncompletaBD();
    }

    private void getOpIncompletaBD() {
        List<Lectura> lecturas = bd.query(
                new Predicate<Lectura>() {
                    @Override
                    public boolean match(Lectura l) {
                        return l.getHf() == null;
                    }
                });
        if(lecturas.size()>0){

        }
    }

    private void desconexionBD(){
        bd.commit();
        bd.close();
        Log.v("conexionBD", "bd cerrada");
    }

    private EmbeddedConfiguration getConfiguracionDb4o() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().activationDepth(25); // Clases anidadas
        configuration.common().objectClass(GregorianCalendar.class).storeTransientFields(true);
        configuration.common().objectClass(GregorianCalendar.class).callConstructor(true);
        configuration.common().objectClass(Sector.class).cascadeOnUpdate(true);
        configuration.common().exceptionsOnNotStorable(false);
        return configuration;
    }

    private boolean isInicializadaBD(){
        List<DatosRiego> datos = bd.query(DatosRiego.class);
        return datos.size() > 0;
    }

    private void inicializarBD(){

        Zona zona = new Zona("Solana");
        bd.store(zona);
        Sector sector;
        sector = new Sector("s1", "Estación/B.Palo bajo", 12725, Dia.miércoles, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s2", "Ventilla", 16127, Dia.jueves, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s3", "Vía",  10444, Dia.viernes, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s4", "Mitagalán/Balsa/B.Quiebras",  10200, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s5", "L. Encinillas/Quiebras altas",  7695, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s6","B. Palo alto/Chapa",  12751, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s7","Loma del Galgo",  13224, Dia.lunes, Riego.gravedad, zona);
        bd.store(sector);

        zona = new Zona("Llano");
        bd.store(zona);
        sector = new Sector("l1", "Destiladero", 6170, Dia.miércoles, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l2","Barrascales", 6100, Dia.jueves, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l3","Viñas/Canteras",  15300, Dia.viernes, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l4","Llano/Campo de Tiro",  18889, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("l5","Pedriza",  15373, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);

        zona = new Zona("Viñas");
        bd.store(zona);
        sector = new Sector("v1", "Olla del Pastor", 7917, Dia.sábado, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("v2", "Nacimiento", 4842, Dia.domingo, Riego.gravedad, zona);
        bd.store(sector);

        DatosRiego datosRiego = new DatosRiego();
        bd.store(datosRiego);

        bd.commit();
    }

    private void listarBD(){
        List<Sector> datos = bd.query(Sector.class);
        for(Sector s : datos){
            Log.v("SECTOR", s.toString());
        }
    }

    private void insertarBD(){
        Lectura l = new Lectura(Calendar.getInstance(),
                Calendar.getInstance(),
                null,
                Double.parseDouble(etInicial.getText().toString()),
                0,
                0,
                sectorActual.getZona().getNombre(),
                sectorActual.getId(),
                sectorActual.getNombre(),
                sectorActual.getOlivos(),
                0);
        bd.store(l);
        bd.commit();
    }

    private List<Sector> getSectoresDiaSemana(final Dia dia){
        List<Sector> sectores = bd.query(
                new Predicate<Sector>() {
                    @Override
                    public boolean match(Sector s) {
                        return s.getDiaDeRiego() == dia;
                    }
                });
        return sectores;
    }

    private List<String> getSectoresDiaSemana(List<Sector> sectores){
        List<String> sectoresString = new ArrayList<>();
        for(Sector s : sectores){
            //sectoresString.add(s.getId()+" "+ s.getNombre());
            sectoresString.add(s.getNombre());
        }

        return sectoresString;
    }

}
