package com.pmoura.somelocations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class Locations extends FragmentActivity {

    private GoogleMap map;
    private Marker currentLocationMarker;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    LatLng destino = null;
    String titulo = "";

    AppDatabase database;

    private LatLng buscarLocal(String nome) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT latitude, longitude FROM Location WHERE nome = ?", new String[]{nome});
        LatLng resultado = null;
        if (cursor.moveToFirst()) {
            resultado = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
        }
        cursor.close();
        return resultado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new AppDatabase(this);
        setContentView(R.layout.activity_locations);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }

        Button btnLocalAtual = findViewById(R.id.btnLocalizacao);
        btnLocalAtual.setOnClickListener(v -> obterLocalizacaoAtual());


    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Intent it = getIntent();
        String localizacao = it.getStringExtra("local");

        destino = buscarLocal(localizacao);
        titulo = localizacao;

        if (destino != null) {
            map.addMarker(new MarkerOptions().position(destino).title(titulo));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 15));
        }
    }

    public void onClickVicosa(View v) {
        destino = buscarLocal("Viçosa");
        titulo = "Viçosa";
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (destino != null) {
            map.addMarker(new MarkerOptions().position(destino).title(titulo));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 18));
        }
    }

    public void onClickEsmeraldas(View v) {
        destino = buscarLocal("Esmeraldas");
        titulo = "Esmeraldas";
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (destino != null) {
            map.addMarker(new MarkerOptions().position(destino).title(titulo));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 12));
        }
    }

    public void onClickDpi(View v) {
        destino = buscarLocal("DPI");
        titulo = "DPI/UFV";
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (destino != null) {
            map.addMarker(new MarkerOptions().position(destino).title(titulo));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, 17));
        }
    }

    private void obterLocalizacaoAtual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            atualizarMarcadorLocalizacao(location);
                        } else {
                            Toast.makeText(this, "Localização indisponível", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void atualizarMarcadorLocalizacao(Location location) {
        LatLng posicaoAtual = new LatLng(location.getLatitude(), location.getLongitude());

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        currentLocationMarker = map.addMarker(new MarkerOptions()
                .position(posicaoAtual)
                .title("Minha localização atual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(posicaoAtual, 16));

        // Buscar Viçosa do banco e calcular distância
        LatLng vicosaLatLng = buscarLocal("Viçosa");
        if (vicosaLatLng != null) {
            float[] resultado = new float[1];
            Location.distanceBetween(
                    posicaoAtual.latitude, posicaoAtual.longitude,
                    vicosaLatLng.latitude, vicosaLatLng.longitude,
                    resultado
            );
            //String texto = String.format("Distância até Viçosa: %.2f metros", resultado[0]);
            Toast.makeText(this, "Minha localização", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obterLocalizacaoAtual();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}