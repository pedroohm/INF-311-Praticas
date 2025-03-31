package com.pmoura.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    private EditText edt, edt2;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        edt = (EditText) findViewById(R.id.firstInput);
        edt2 = (EditText) findViewById(R.id.secInput);
        result = (TextView) findViewById(R.id.resultado);
    }

    private boolean validarEntrada() {
        String input1 = edt.getText().toString().trim();
        String input2 = edt2.getText().toString().trim();

        if (input1.isEmpty() || input2.isEmpty()) {
            result.setText("Erro: Preencha ambos os campos!");
            return false;
        }

        return true;
    }
    @SuppressLint("DefaultLocale")
    public void clickSum(View view){
        if (!validarEntrada()) return;

        Double num1 = Double.parseDouble(edt.getText().toString());
        Double num2 = Double.parseDouble(edt2.getText().toString());

        Double sum = num1+num2;

        result.setText(String.format("O resultado e: %.2f", sum));
    }

    @SuppressLint("DefaultLocale")
    public void clickSub(View view){
        if (!validarEntrada()) return;

        Double num1 = Double.parseDouble(edt.getText().toString());
        Double num2 = Double.parseDouble(edt2.getText().toString());

        Double sum = num1-num2;

        result.setText(String.format("O resultado e: %.2f", sum));
    }


    @SuppressLint("DefaultLocale")
    public void clickDiv(View view){
        if (!validarEntrada()) return;

        Double num1 = Double.parseDouble(edt.getText().toString());
        Double num2 = Double.parseDouble(edt2.getText().toString());

        TextView txt = (TextView) findViewById(R.id.resultado);
        if (num2 != 0) {

            Double sum = num1 / num2;

            result.setText(String.format("O resultado e: %.2f", sum));
        } else {
            txt.setText("Erro: Divisão por zero!");
        }
    }

    @SuppressLint("DefaultLocale")
    public void clickMulti(View view){
        if (!validarEntrada()) return;

        Double num1 = Double.parseDouble(edt.getText().toString());
        Double num2 = Double.parseDouble(edt2.getText().toString());

        Double sum = num1*num2;

        result.setText(String.format("O resultado e: %.2f", sum));
    }
}