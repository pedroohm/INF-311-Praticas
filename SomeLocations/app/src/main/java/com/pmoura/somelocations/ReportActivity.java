package com.pmoura.somelocations;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReportActivity extends ListActivity {

    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new AppDatabase(this);

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT msg, timestamp FROM Logs", null);

        ArrayList<String> dados = new ArrayList<>();

        while (cursor.moveToNext()) {
            String msg = cursor.getString(0);
            String time = cursor.getString(1);
            dados.add(msg + " - " + time);
        }

        cursor.close();

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dados));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT Location.latitude, Location.longitude " +
                        "FROM Logs INNER JOIN Location ON Logs.id_location = Location.id " +
                        "LIMIT 1 OFFSET ?",
                new String[]{String.valueOf(position)}
        );

        if (cursor.moveToFirst()) {
            double lat = cursor.getDouble(0);
            double lon = cursor.getDouble(1);
            Toast.makeText(this, "Latitude: " + lat + "\nLongitude: " + lon, Toast.LENGTH_LONG).show();
        }

        cursor.close();
    }
}