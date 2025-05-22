package com.pmoura.somelocations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "locations.db";
    private static final int DB_VERSION = 1;

    public AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Location
        db.execSQL("CREATE TABLE Location (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "latitude REAL," +
                "longitude REAL)");

        // Tabela Logs
        db.execSQL("CREATE TABLE Logs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "msg TEXT," +
                "timestamp TEXT," +
                "id_location INTEGER," +
                "FOREIGN KEY(id_location) REFERENCES Location(id))");

        // Inserção inicial
        db.execSQL("INSERT INTO Location (nome, latitude, longitude) VALUES " +
                "('Viçosa', -20.75907591006864, -42.87345073406712)," +
                "('Esmeraldas', -19.718160053488578, -44.179021073631944)," +
                "('DPI', -20.764992719866058, -42.868406565327184)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Logs");
        db.execSQL("DROP TABLE IF EXISTS Location");
        onCreate(db);
    }
}