package com.pmoura.completecalc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class MainActivity extends Activity {

    private Double num1, num2;
    private EditText visor;
    private String input = "";
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visor = findViewById(R.id.visor);
    }

    public void typeNum(View view) {
        Button btn = (Button) view;

        if (Objects.equals(input, "") && !result.isEmpty()) {
            input = result;
        }
        input += btn.getText().toString();

        visor.setText(input);
    }

    public void typeOp(View view) {
        if (!input.isEmpty() || !result.isEmpty()) {
            if (!result.isEmpty()) {
                input = result;
            }

            Button btn = (Button) view;
            input += " " + btn.getText().toString() + " ";

            visor.setText(input);
        }
    }

    public void typeBsp(View view) {
        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);
            visor.setText(input);
        }
    }

    @SuppressLint("SetTextI18n")
    public void typeEqual(View view) {
        if (!input.isEmpty()) {
            String[] parts = input.split(" ");
            if (parts.length == 3) {
                num1 = Double.parseDouble(parts[0]);
                num2 = Double.parseDouble(parts[2]);
                char operator = parts[1].charAt(0);

                double resultValue = 0;
                switch (operator) {
                    case '+':
                        resultValue = num1 + num2;
                        break;
                    case '-':
                        resultValue = num1 - num2;
                        break;
                    case '*':
                        resultValue = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0) resultValue = num1 / num2;
                        else resultValue = Double.NaN;
                        break;
                }

                if (!Double.isNaN(resultValue)) {
                    result = String.valueOf(resultValue);
                    visor.setText(result);
                } else {
                    visor.setText("ERROR");
                    result = "";
                }
            }
        }
        input = "";
        }

    public void typeClear(View view) {
        input = "";
        result = "";
        visor.setText("");
    }

    public void typeDot(View view) {
        if (!input.isEmpty()) {
            String[] parts = input.split(" ");
            String lastPart = parts[parts.length - 1];

            if (!lastPart.contains(".")) {
                input += ".";
                visor.setText(input);
            }
        }
    }

}
