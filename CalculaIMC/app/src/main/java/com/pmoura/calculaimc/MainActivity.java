package com.pmoura.calculaimc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Intent intent = getIntent();
        boolean fromReport = intent.getBooleanExtra("fromReport", false);

        if (fromReport) {
            clearFields();


            intent.removeExtra("fromReport"); // Limpa o extra
        }
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

    public void clickButton(View v) {
        EditText name = (EditText) findViewById(R.id.inputName);
        EditText age = (EditText) findViewById(R.id.ageInput);
        EditText weight = (EditText) findViewById(R.id.weightInput);
        EditText height = (EditText) findViewById(R.id.heightInput);

        String nameStr = name.getText().toString();
        String ageStr = age.getText().toString();
        String weightStr = weight.getText().toString();
        String heightStr = height.getText().toString();

        if (nameStr.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(getBaseContext(), Report.class);
        Bundle params = new Bundle();

        params.putString("name", nameStr);
        params.putInt("age", Integer.parseInt(ageStr));
        params.putFloat("weight", Float.parseFloat(weightStr));
        params.putFloat("height", Float.parseFloat(heightStr));

        it.putExtras(params);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);
    }

    private void clearFields() {
        ((EditText) findViewById(R.id.inputName)).setText("");
        ((EditText) findViewById(R.id.ageInput)).setText("");
        ((EditText) findViewById(R.id.weightInput)).setText("");
        ((EditText) findViewById(R.id.heightInput)).setText("");
    }

}