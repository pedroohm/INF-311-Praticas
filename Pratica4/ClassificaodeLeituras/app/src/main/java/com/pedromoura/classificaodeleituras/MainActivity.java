package com.pedromoura.classificaodeleituras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView tvReceived;
    private Button btnRespond;

    private float lightValue = 0;
    private float proximityValue = 0;

    public static final String ACTION_CLASSIFICAR = "com.seuapp.ACTION_CLASSIFICAR";
    public static final String ACTION_RESPONDER = "com.seuapp.ACTION_RESPONDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvReceived = findViewById(R.id.tv_received);
        btnRespond = findViewById(R.id.btn_respond);

        Intent intent = getIntent();
        lightValue = intent.getFloatExtra("light", 0);
        proximityValue = intent.getFloatExtra("proximity", 0);

        tvReceived.setText(String.format("Luminosidade: %.2f lx\nProximidade: %.2f cm", lightValue, proximityValue));

        btnRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean lanterna = lightValue < 20.0;
                boolean vibracao = proximityValue > 3.0;

                Intent resposta = new Intent();
                resposta.setAction(ACTION_RESPONDER);
                resposta.putExtra("lanterna", lanterna);
                resposta.putExtra("vibracao", vibracao);
                sendBroadcast(resposta);

                finish();
            }
        });
    }
}