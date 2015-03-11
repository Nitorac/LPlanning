package com.nitorac.lplanning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class EventsBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "events.db";

    private static final String TABLE_EVENTS = "table_events";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_MATIERE = "MATIERE";
    private static final int NUM_COL_MATIERE = 1;
    private static final String COL_SALLE = "SALLE";
    private static final int NUM_COL_SALLE = 2;
    private static final String COL_JOUR = "JOUR";
    private static final int NUM_COL_JOUR = 3;
    private static final String COL_MOIS = "MOIS";
    private static final int NUM_COL_MOIS = 4;
    private static final String COL_ANNEE = "ANNEE";
    private static final int NUM_COL_ANNEE = 5;
    private static final String COL_HORAIRE = "HORAIRE";
    private static final int NUM_COL_HORAIRE = 6;
    private SQLiteDatabase bdd;

    private NitoSQLiteDatabase maBaseSQLite;

    public EventsBDD(Context context) {
        maBaseSQLite = new NitoSQLiteDatabase(context, NOM_BDD, null, VERSION_BDD);
    }

    public String getName(){
            return NOM_BDD;
    }

    public int getVersion(){
        return VERSION_BDD;
    }

    /**
     * Ouvre la BDD en écriture
     */
    public void open() {
        bdd = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Ferme l'accès à la BDD
     */
    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    public void recreateDB(){
        maBaseSQLite.onCreate(bdd);
    }

    /**
     * Insère un contact en base de données
     *
     * @param events
     *            le contact à insérer
     * @return l'identifiant de la ligne insérée
     */
    public long insertEvent(Events events) {
        ContentValues values = new ContentValues();
        // On insère les valeurs dans le ContentValues : on n'ajoute pas
        // l'identifiant car il est créé automatiquement
        values.put(COL_MATIERE, events.getMatiere());
        values.put(COL_SALLE, events.getSalle());
        values.put(COL_JOUR, events.getJour());
        values.put(COL_MOIS, events.getMois());
        values.put(COL_ANNEE, events.getAnnee());
        values.put(COL_HORAIRE, events.getTranche_horaire());

        return bdd.insert(TABLE_EVENTS, null, values);
    }

    /**
     * Met à jour le contact en base de données
     *
     * @param id
     *            l'identifiant du contact à modifier
     * @param events
     *            le nouveau contact à associer à l'identifiant
     * @return le nombre de lignes modifiées
     */
    public int updateEvent(int id, Events events) {
        ContentValues values = new ContentValues();
        values.put(COL_MATIERE, events.getMatiere());
        values.put(COL_SALLE, events.getSalle());
        values.put(COL_JOUR, events.getJour());
        values.put(COL_MOIS, events.getMois());
        values.put(COL_ANNEE, events.getAnnee());
        values.put(COL_HORAIRE, events.getTranche_horaire());
        return bdd.update(TABLE_EVENTS, values, COL_ID + " = " + id, null);
    }

    /**
     * Supprime un contact de la BDD (celui dont l'identifiant est passé en
     * paramètres)
     *
     * @param id
     *            l'identifiant du contact à supprimer
     * @return le nombre de contacts supprimés
     */
    public int removeEventWithID(int id) {
        return bdd.delete(TABLE_EVENTS, COL_ID + " = " + id, null);
    }

    public void reset(){
       bdd.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
    }

    /**
     * Retourne le premier contact dont le numéro de téléphone correspond à
     * celui en paramètre
     *
     * @param numeroTelephone
     *            le numéro de téléphone
     * @return le contact récupéré depuis la base de données
     */
    public Events getFirstEventWithJour(String numeroTelephone) {
        Cursor c = bdd.query(TABLE_EVENTS, new String[] { COL_ID, COL_MATIERE,
                COL_SALLE, COL_JOUR, COL_MOIS, COL_ANNEE, COL_HORAIRE}, COL_JOUR + " LIKE \""
                + numeroTelephone + "\"", null, null, null, null);
        return cursorToEvent(c);
    }


        public ArrayList<Events> getAllRowsInArray() {

            ArrayList<Events> list = new ArrayList<Events>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

            SQLiteDatabase db = maBaseSQLite.getReadableDatabase();
            try {

                Cursor cursor = db.rawQuery(selectQuery, null);
                try {

                    // looping through all rows and adding to list
                    if (cursor.moveToFirst()) {
                        do {
                            Events events = new Events();
                            events.setId(cursor.getInt(NUM_COL_ID));
                            events.setMatiere(cursor.getString(NUM_COL_MATIERE));
                            events.setSalle(cursor.getString(NUM_COL_SALLE));
                            events.setJour(cursor.getString(NUM_COL_JOUR));
                            events.setMois(cursor.getString(NUM_COL_MOIS));
                            events.setAnnee(cursor.getString(NUM_COL_ANNEE));
                            events.setTranche_horaire(cursor.getInt(NUM_COL_HORAIRE));
                            list.add(events);
                        } while (cursor.moveToNext());
                    }

                } finally {
                    try { cursor.close(); } catch (Exception ignore) {}
                }

            } finally {
                try { db.close(); } catch (Exception ignore) {}
            }

            return list;
    }

    public boolean isTableEventsExists(){
        return maBaseSQLite.isTableEventsExists();
    }

    /**
     * Convertit le cursor en contact
     *
     * @param c
     *            le cursor à convertir
     * @return le Contact créé à partir du Cursor
     */
    private Events cursorToEvent(Cursor c) {
        // si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        // Sinon on se place sur le premier élément
        c.moveToFirst();

        Events events = new Events();
        events.setId(c.getInt(NUM_COL_ID));
        events.setMatiere(c.getString(NUM_COL_MATIERE));
        events.setSalle(c.getString(NUM_COL_SALLE));
        events.setJour(c.getString(NUM_COL_JOUR));
        events.setMois(c.getString(NUM_COL_MOIS));
        events.setAnnee(c.getString(NUM_COL_ANNEE));
        events.setTranche_horaire(c.getInt(NUM_COL_HORAIRE));

        c.close();

        return events;
    }

}