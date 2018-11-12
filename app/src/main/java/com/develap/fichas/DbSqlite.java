package com.develap.fichas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbSqlite extends SQLiteOpenHelper {
    public static final int DB_Version = 1;
    public static final String TABLE_NAME = "pacientes";
    public static final String DB_Name = "fichas.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE pacientes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "apellido VARCHAR(255) NULL," +
            "nombre VARCHAR(255) NULL," +
            "fechanac DATE NULL," +
            "direccion VARCHAR(255) NULL," +
            "ficha VARCHAR(20) NULL," +
            "telefono VARCHAR(255) NULL" +
            ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS pacientes";

    // Constructor
    public DbSqlite(Context context){
        super (context, DB_Name, null, DB_Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
