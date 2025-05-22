package com.pmoura.somelocations;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ListActivity {
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new AppDatabase(this);

        String[] menu = {
                "Minha casa na cidade natal",
                "Minha casa em Viçosa",
                "Meu departamento",
                "Fechar aplicação",
                "Relatório" // Novo item
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrayAdapter);
    }


    protected void onListItemClick(ListView l, View v, int position, long id){
        String itemSelecionado = (String) getListAdapter().getItem(position);

        Toast.makeText(this, "Você escolheu: " + itemSelecionado, Toast.LENGTH_SHORT).show();

        Intent it = new Intent(getBaseContext(), Locations.class);

        switch (position){
            case 0:
                registrarLog("Esmeraldas");
                it.putExtra("local", "Esmeraldas");
                startActivity(it);
                break;
            case 1:
                registrarLog("Viçosa");
                it.putExtra("local", "Viçosa");
                startActivity(it);
                break;
            case 2:
                registrarLog("DPI");
                it.putExtra("local", "DPI");
                startActivity(it);
                break;
            case 3:
                finish();
                break;
            case 4:
                startActivity(new Intent(this, ReportActivity.class));
                break;
        }
    }

    private void registrarLog(String nome) {
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM Location WHERE nome = ?", new String[]{nome});
        int idLocation = -1;
        if (cursor.moveToFirst()) {
            idLocation = cursor.getInt(0);
        }
        cursor.close();

        String timestamp = java.time.Instant.now().toString();

        ContentValues values = new ContentValues();
        values.put("msg", nome);
        values.put("timestamp", timestamp);
        values.put("id_location", idLocation);
        db.insert("Logs", null, values);
    }

}