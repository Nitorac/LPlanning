package com.nitorac.lplanning;

/**
 * Created by Nitorac.
 */
public class Events {

    private int id;

    private String matiere;

    private String salle;

    private String jour;

    private String mois;

    private String annee;

    private int tranche_horaire;

    public Events() {

    }

    public Events(String matiere, String salle, String jour, String mois, String annee, int tranche_horaire) {
        super();
        this.matiere = matiere;
        this.salle = salle;
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
        this.tranche_horaire = tranche_horaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public int getTranche_horaire() {
        return tranche_horaire;
    }

    public void setTranche_horaire(int tranche_horaire) {
        this.tranche_horaire = tranche_horaire;
    }

    @Override
    public String toString() {
        return "[Contact] id : " + id + " - matiere , salle : " + matiere + ", "
                + salle + " - jour : " + jour + "  " + mois + "  " + annee + "/" + String.valueOf(tranche_horaire);
    }

}