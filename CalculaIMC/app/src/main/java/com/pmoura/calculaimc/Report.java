package com.pmoura.calculaimc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Report extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent it = getIntent();

        Pair<Float, String> IMC = calcIMC(it.getFloatExtra("weight", 0), it.getFloatExtra("height", 0));

        TextView name = (TextView) findViewById(R.id.nameR);
        String nameView = name.getText().toString() + " " + it.getStringExtra("name");
        name.setText(nameView);

        TextView age = (TextView) findViewById(R.id.ageR);
        String ageView = age.getText().toString() + " " + it.getIntExtra("age", 0) + " anos";
        age.setText(ageView);

        TextView weight = (TextView) findViewById(R.id.weightR);
        String weightView = weight.getText().toString() + " " + it.getFloatExtra("weight",0) + " Kg";
        weight.setText(weightView);

        TextView height = (TextView) findViewById(R.id.heightR);
        String heightView = height.getText().toString() + " " + it.getFloatExtra("height",0) + " m";
        height.setText(heightView);

        TextView imc = (TextView) findViewById(R.id.imcR);
        String imcView = imc.getText().toString() + " " + String.format("%.1f", IMC.first) + " Kg/m²";
        imc.setText(imcView);

        TextView report = (TextView) findViewById(R.id.classR);
        String reportView = report.getText().toString() + " " + IMC.second;
        report.setText(reportView);    }

    public Pair<Float, String> calcIMC(Float weight, Float height){
        float imc = weight/(height*height);
        String report;

        if (imc < 18.5) report = "Abaixo do peso";
        else if (18.5 <= imc && imc <= 24.9) report = "Saudável";
        else if (25 <= imc && imc <= 29.9) report = "Sobrepeso";
        else if (30<= imc && imc <= 34.9) report = "Obesidade Grau I";
        else if (35<= imc && imc <= 39.9) report = "Obesidade Grau II (severa)";
        else report = "Obesidade Grau III (mórbida)";

        return new Pair<>(imc, report);
    }


    public void newCalc(View v){
        Intent it = new Intent(getApplicationContext(), MainActivity.class);
        it.putExtra("fromReport", true); // flag para identificar a origem
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // remove a Report do topo
        startActivity(it);
        finish();
    }

    private String getClassName(){
        return getClass().getName();
    }

    protected void onStart(){
        super.onStart();
        Log.i("Ciclo de vida", getClassName()+ ".onStart() chamado.");
    }

    protected void onResume(){
        super.onResume();
        Log.i("Ciclo de vida", getClassName()+ ".onResume() chamado.");
    }

    protected void onPause(){
        super.onPause();
        Log.i("Ciclo de vida", getClassName()+ ".onPause() chamado.");
    }

    protected void onStop(){
        super.onStop();
        Log.i("Ciclo de vida", getClassName()+ ".onStop() chamado.");
    }

    protected void onRestart() {
        super.onRestart();
        Log.i("Ciclo de vida", getClassName()+ ".onRestart() chamado.");
    }
}