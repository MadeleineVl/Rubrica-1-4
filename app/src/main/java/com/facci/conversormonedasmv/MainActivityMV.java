package com.facci.conversormonedasmv;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityMV extends AppCompatActivity {

    final String [] informacion = new String[]{"Dolar", "Euro", "Peso Mexicano"};

    private Spinner monedaactual;
    private Spinner monedacambio;
    private EditText ValorCambioTXT;
    private TextView resultadoTV;
    final private double conversionDolarEuro = 0.87;
    final private double conversionPesoDolar = 0.54;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_mv);

        ArrayAdapter <String> conversor= new ArrayAdapter <String>(this,R.layout.support_simple_spinner_dropdown_item,informacion);
        monedaactual =(Spinner) findViewById(R.id.MonedaActualSP);
        monedaactual.setAdapter(conversor);
        monedacambio=(Spinner)findViewById(R.id.MonedaCambioSP);
        SharedPreferences preferencias =getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String tmpMonedaActual=preferencias.getString("monedaActual", "");
        String tmpMonedaCambio=preferencias.getString("monedaCambio","");
        if (tmpMonedaActual.equals("")){
            int indice= conversor.getPosition(tmpMonedaActual);
            monedaactual.setSelection(indice);
        }
        if (tmpMonedaCambio.equals("")){
            int indice= conversor.getPosition(tmpMonedaCambio);
            monedacambio.setSelection(indice);
        }

    }
    public void clickProcesar(View p){
        monedaactual =(Spinner) findViewById(R.id.MonedaActualSP);
        monedacambio= (Spinner) findViewById(R.id.MonedaCambioSP);
        ValorCambioTXT=(EditText) findViewById(R.id.ValorCambioET);
        resultadoTV=(TextView)findViewById(R.id.resultadoTV);

        String MONEDAACTUAL= monedaactual.getSelectedItem().toString();
        String MONEDACAMBIO= monedacambio.getSelectedItem().toString();
        double valorCambio = Double.parseDouble(ValorCambioTXT.getText().toString());
        double resultado = ProcesarTransaccion(MONEDAACTUAL, MONEDACAMBIO, valorCambio);

         if (resultado>0){
             resultadoTV.setText(String.format("Por %5.2f %s, usted recibira %5.2f %s", valorCambio,MONEDAACTUAL,resultado,MONEDACAMBIO));
             ValorCambioTXT.setText("");
             SharedPreferences preferencias= getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
             SharedPreferences.Editor editor = preferencias.edit();
             editor.putString("monedaActual",MONEDAACTUAL);
             editor.putString("monedaCambio",MONEDACAMBIO);
             editor.commit();
         }else{
            resultadoTV.setText(String.format("usted recibira"));
             Toast.makeText(MainActivityMV.this,"las opciones elegidas no tienen un factor de conversion", Toast.LENGTH_LONG).show();
        }
    }

    private double ProcesarTransaccion(String MONEDAACTUAL , String MONEDACAMBIO, double valorCambio){
       double rconversion=0;
        switch (MONEDAACTUAL){
            case "Dolar":
                if (MONEDACAMBIO.equals("Euro"))
                    rconversion= valorCambio *  conversionDolarEuro;

                if (MONEDACAMBIO.equals("Peso Mexicano"))
                    rconversion= valorCambio / conversionPesoDolar;
                break;
            case "Euro":
                if (MONEDACAMBIO.equals("Dolar"))
                    rconversion= valorCambio /  conversionDolarEuro;
                break;
            case "Peso Mexicano":
                if (MONEDACAMBIO.equals("Euro"))
                    rconversion= valorCambio *  conversionPesoDolar;
                break;
        }
        return 0;

    }

}
