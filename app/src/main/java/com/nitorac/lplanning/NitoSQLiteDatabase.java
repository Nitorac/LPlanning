package com.nitorac.lplanning;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NitoSQLiteDatabase extends SQLiteOpenHelper {

    private static final String TABLE_EVENTS = "table_events";
    private static final String COL_ID = "ID";
    private static final String COL_MATIERE = "MATIERE";
    private static final String COL_SALLE = "SALLE";
    private static final String COL_JOUR = "JOUR";
    private static final String COL_MOIS = "MOIS";
    private static final String COL_ANNEE = "ANNEE";
    private static final String COL_HORAIRE = "HORAIRE";


    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE "
            + TABLE_EVENTS + " (" + COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_MATIERE
            + " TEXT NOT NULL, " + COL_SALLE + " TEXT NOT NULL, "
            + COL_JOUR + " TEXT NOT NULL, " + COL_MOIS + " TEXT NOT NULL, "
            + COL_ANNEE + " TEXT NOT NULL, " + COL_HORAIRE + " INTEGER NOT NULL);";

    public NitoSQLiteDatabase(Context context, String name, CursorFactory factory,
                              int version) {
        super(context, name, factory, version);
    }

    /**
     * Cette méthode est appelée lors de la toute première création de la base
     * de données. Ici, on doit créer les tables et éventuellement les populer.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on crée la table table_contacts dans la BDD
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on supprime la table table_contacts de la BDD et on recrée la BDD
        db.execSQL("DROP TABLE " + TABLE_EVENTS + ";");
        onCreate(db);
    }


    public boolean isTableEventsExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ TABLE_EVENTS +"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }


    public int getRowsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

}