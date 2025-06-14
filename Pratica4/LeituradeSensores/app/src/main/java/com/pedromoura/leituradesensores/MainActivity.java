package com.pedromoura.leituradesensores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    private float currentLight = 0;
    private float currentProximity = 0;

    private TextView tvLight, tvProximity;
    private Switch swFlash, swVibrate;
    private Button btnClassify;

    private LanternaHelper lanternaHelper;
    private MotorHelper motorHelper;

    public static final String ACTION_CLASSIFICAR = "com.pedromoura.ACTION_CLASSIFICAR_LEITURAS";
    public static final String ACTION_RESPONDER = "com.pedromoura.ACTION_RESPONDER_LEITURAS";

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ACTION_RESPONDER)) {
                boolean luzBaixa = intent.getBooleanExtra("luzBaixa", false);
                boolean obstaculoDistante = intent.getBooleanExtra("obstaculoDistante", false);

                // Controle da lanterna
                if (luzBaixa) {
                    lanternaHelper.ligar();
                    swFlash.setChecked(true);
                } else {
                    lanternaHelper.desligar();
                    swFlash.setChecked(false);
                }

                // Controle da vibração
                if (obstaculoDistante) {
                    motorHelper.iniciarVibracao();
                    swVibrate.setChecked(true);
                } else {
                    motorHelper.pararVibracao();
                    swVibrate.setChecked(false);
                }
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Este dispositivo não possui flash!", Toast.LENGTH_SHORT).show();
        }

       if (!getPackageManager().hasSystemFeature(VIBRATOR_SERVICE)) {
            Toast.makeText(this, "Este dispositivo não possui vibrador!", Toast.LENGTH_SHORT).show();
        }

        tvLight = findViewById(R.id.tv_light);
        tvProximity = findViewById(R.id.tv_proximity);
        swFlash = findViewById(R.id.sw_flash);
        swVibrate = findViewById(R.id.sw_vibrate);
        btnClassify = findViewById(R.id.btn_classify);

       swFlash.setClickable(false);
        swVibrate.setClickable(false);

        lanternaHelper = new LanternaHelper(this);
        motorHelper = new MotorHelper(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (lightSensor == null) {
            tvLight.setText("Luminosidade: Sensor não disponível");
        }
        if (proximitySensor == null) {
            tvProximity.setText("Proximidade: Sensor não disponível");
        }

        btnClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.pedromoura.ACTION_CLASSIFICAR_LEITURAS");
                intent.setPackage("com.pedromoura.classificacaoleituras"); // pacote do app B
                intent.putExtra("luminosidade", currentLight);
                intent.putExtra("proximidade", currentProximity);
                startActivityForResult(intent, 1);

            }
        });

        // Registrar receiver para resposta
        IntentFilter filter = new IntentFilter(ACTION_RESPONDER);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        lanternaHelper.desligar();
        motorHelper.pararVibracao();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLight = event.values[0];
            tvLight.setText(String.format("Luminosidade: %.1f lx", currentLight));
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            currentProximity = event.values[0];
            tvProximity.setText(String.format("Proximidade: %.1f cm", currentProximity));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}