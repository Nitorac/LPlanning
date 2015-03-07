package com.nitorac.lplanning;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nitorac.
 */

public class PlanningVar {
    //Semaine basique : Groupe 2 Semaine A Allemand non-latin non-DNL Groupe 2 Anglais
    public static String grClasse;
    public static String LV2;
    public static String grAng;
    public static String latin;
    public static String DNL;
    public static String semaine = getWeekLetter();

    static String [][] weekBasicGr2A = {
            {"Maths C14", "Histoire-Géo G13", "Français B2", "Français B2", "0 0", "Restauration Self", "Allemand G6", "Anglais G13", "0 0", "0 0"},
            {"PFEG C13", "PFEG C13", "Maths B2", "A.P-Maths B2", "0 0", "Restauration Self", "Français B1", "A.P-Français B1", "0 0", "0 0"},
            {"Sc.-Physiques A11", "Maths B1", "Sc.-Physiques C4", "S.V.T C1", "0 0", "0 0","0 0","0 0","0 0"},
            {"Anglais B2", "Histoire-Géo G12", "Maths G16", "Maths G16", "Restauration Self", "Sport Gymnase", "Sport Gymnase", "Histoire-Géo G12", "Allemand G11", "0 0"},
            {"0 0", "Anglais B5", "Français B1", "M.P.S-Ph C4", "M.P.S-Ph C4", "Restauration Self", "0 0", "0 0","0 0","0 0"}
    };

    static String [][] weekBasicGr2B = {
            {"0 0", "Histoire-Géo G13", "Français B2", "Français B2", "0 0", "Restauration Self", "Allemand G6", "Anglais G13", "0 0", "0 0"},
            {"PFEG C13", "PFEG C13", "Sc.-Physiques B2", "Maths B2", "A.P-Maths B2", "Restauration Self", "Français B1", "A.P-Français B1", "0 0", "0 0"},
            {"Sc.-Physiques A11", "Maths B1", "Sc.-Physiques C4", "S.V.T C1", "0 0", "0 0","0 0","0 0","0 0"},
            {"Anglais B2", "Histoire-Géo G12", "Maths G16", "ECJS G11", "Restauration Self", "Sport Gymnase", "Sport Gymnase", "Histoire-Géo G12", "Allemand G11", "Allemand G11"},
            {"0 0", "Anglais B5", "Français B1", "M.P.S-SVT C1", "M.P.S-SVT C1", "Restauration Self", "0 0", "0 0","0 0","0 0"}
    };

    static String [][] weekBasicGr1A = {
            {"0 0", "Histoire-Géo G13", "Français B2", "Français B2", "0 0", "Restauration Self", "Allemand G6", "Anglais G13", "0 0", "0 0"},
            {"PFEG C13", "PFEG C13", "Maths B2", "A.P-Maths B2", "0 0", "Restauration Self", "Français B1", "A.P-Français B1", "0 0", "0 0"},
            {"Sc.-Physiques A11", "Maths B1", "S.V.T C1", "Sc.-Physiques C4", "0 0", "0 0","0 0","0 0","0 0"},
            {"Anglais B2", "Histoire-Géo G12", "Maths G16", "ECJS G11", "Restauration Self", "Sport Gymnase", "Sport Gymnase", "Histoire-Géo G12", "Allemand G11", "Allemand G11"},
            {"0 0", "Anglais B5", "Français B1", "M.P.S-SVT C1", "M.P.S-SVT C1", "Restauration Self", "0 0", "0 0","0 0","0 0"}
    };

    static String [][] weekBasicGr1B = {
            {"Maths C14", "Histoire-Géo G13", "Français B2", "Français B2", "0 0", "Restauration Self", "Allemand G6", "Anglais G13", "0 0", "0 0"},
            {"PFEG C13", "PFEG C13", "Sc.-Physiques B2", "Maths B2", "A.P-Maths B2", "Restauration Self", "Français B1", "A.P-Français B1", "0 0", "0 0"},
            {"Sc.-Physiques A11", "Maths B1", "S.V.T C1", "Sc.-Physiques C4", "0 0", "0 0","0 0","0 0","0 0"},
            {"Anglais B2", "Histoire-Géo G12", "Maths G16", "Maths G16", "Restauration Self", "Sport Gymnase", "Sport Gymnase", "Histoire-Géo G12", "Allemand G11", "Allemand G11"},
            {"0 0", "Anglais B5", "Français B1", "M.P.S-Ph C4", "M.P.S-Ph C4", "Restauration Self", "0 0", "0 0","0 0","0 0"}
    };

    static String [][] weekEmpty;


@SuppressLint("SimpleDateFormat")
public static int currentSlotHour(){
    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int minutes = Calendar.getInstance().get(Calendar.MINUTE);
    try {
        Date time1 = new SimpleDateFormat("HH:mm").parse("03:00");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time1);
        cal1.add(Calendar.DATE, 1);

        Date time2 = new SimpleDateFormat("HH:mm").parse("08:55");
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(time2);
        cal2.add(Calendar.DATE, 1);

        Date time3 = new SimpleDateFormat("HH:mm").parse("09:45");
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(time3);
        cal3.add(Calendar.DATE, 1);

        Date time4 = new SimpleDateFormat("HH:mm").parse("10:50");
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(time4);
        cal4.add(Calendar.DATE, 1);

        Date time5 = new SimpleDateFormat("HH:mm").parse("11:50");
        Calendar cal5 = Calendar.getInstance();
        cal5.setTime(time5);
        cal5.add(Calendar.DATE, 1);

        Date time6 = new SimpleDateFormat("HH:mm").parse("12:40");
        Calendar cal6 = Calendar.getInstance();
        cal6.setTime(time6);
        cal6.add(Calendar.DATE, 1);

        Date time7 = new SimpleDateFormat("HH:mm").parse("13:40");
        Calendar cal7 = Calendar.getInstance();
        cal7.setTime(time7);
        cal7.add(Calendar.DATE, 1);

        Date time8 = new SimpleDateFormat("HH:mm").parse("14:55");
        Calendar cal8 = Calendar.getInstance();
        cal8.setTime(time8);
        cal8.add(Calendar.DATE, 1);

        Date time9 = new SimpleDateFormat("HH:mm").parse("15:55");
        Calendar cal9 = Calendar.getInstance();
        cal9.setTime(time9);
        cal9.add(Calendar.DATE, 1);

        Date time10 = new SimpleDateFormat("HH:mm").parse("16:55");
        Calendar cal10 = Calendar.getInstance();
        cal10.setTime(time10);
        cal10.add(Calendar.DATE, 1);

        Date time11 = new SimpleDateFormat("HH:mm").parse("18:15");
        Calendar cal11 = Calendar.getInstance();
        cal11.setTime(time11);
        cal11.add(Calendar.DATE, 1);

        Date customTime1 = new SimpleDateFormat("HH:mm").parse("11:40");
        Calendar cusCal1 = Calendar.getInstance();
        cusCal1.setTime(customTime1);
        cusCal1.add(Calendar.DATE, 1);

        Date currentTime = new SimpleDateFormat("HH:mm").parse(String.valueOf(hour) + ":" + String.valueOf(minutes));
        Calendar current = Calendar.getInstance();
        current.setTime(currentTime);
        current.add(Calendar.DATE, 1);


        if (currentDay() == 2) {
            Date cur = current.getTime();
            if ((cur.after(cal1.getTime()) && cur.before(cal2.getTime())) || cur.equals(cal2.getTime())) {
                return 0;
            }else if((cur.after(cal2.getTime()) && cur.before(cal3.getTime())) || cur.equals(cal3.getTime())) {
                return 1;
            }else if((cur.after(cal3.getTime()) && cur.before(cusCal1.getTime())) || cur.equals(cusCal1.getTime())){
                return 2;
            }else if((cur.after(cusCal1.getTime()) && cur.before(cal6.getTime())) || cur.equals(cal6.getTime())){
                return 3;
            }
            return 255;
        } else {
            Date cur = current.getTime();
            if ((cur.after(cal1.getTime()) && cur.before(cal2.getTime())) || cur.equals(cal2.getTime())) {
                return 0;
            }else if((cur.after(cal2.getTime()) && cur.before(cal3.getTime())) || cur.equals(cal3.getTime())){
                return 1;
            }else if((cur.after(cal3.getTime()) && cur.before(cal4.getTime())) || cur.equals(cal4.getTime())){
                return 2;
            }else if((cur.after(cal4.getTime()) && cur.before(cal5.getTime())) || cur.equals(cal5.getTime())){
                return 3;
            }else if((cur.after(cal5.getTime()) && cur.before(cal6.getTime())) || cur.equals(cal6.getTime())){
                return 4;
            }else if((cur.after(cal6.getTime()) && cur.before(cal7.getTime())) || cur.equals(cal7.getTime())){
                return 5;
            }else if((cur.after(cal7.getTime()) && cur.before(cal8.getTime())) || cur.equals(cal8.getTime())){
                return 6;
            }else if((cur.after(cal8.getTime()) && cur.before(cal9.getTime())) || cur.equals(cal9.getTime())){
                return 7;
            }else if((cur.after(cal9.getTime()) && cur.before(cal10.getTime())) || cur.equals(cal10.getTime())){
                return 8;
            }else if((cur.after(cal10.getTime()) && cur.before(cal11.getTime())) || cur.equals(cal11.getTime())){
                return 9;
            }
            return 255;
        }
    }catch (Exception e){
        e.printStackTrace();
        return 255;
    }
}

public static int currentDay(){
        int jour = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int jourReturn;
        switch(jour){
            case Calendar.MONDAY: jourReturn = 0;
                break;
            case Calendar.TUESDAY: jourReturn = 1;
                break;
            case Calendar.WEDNESDAY: jourReturn = 2;
                break;
            case Calendar.THURSDAY: jourReturn = 3;
                break;
            case Calendar.FRIDAY: jourReturn = 4;
                break;
            default:  jourReturn = 5;
                break;
        }
    return jourReturn;
}

public static String getFinalPlanning(){
    int hour = currentSlotHour();
    int day = currentDay();
   // Log.i("TRY", temp[0][8]);
    try {
    if(grClasse.equals("1")){
        if(semaine.equals("A")){
            String [][] weekLatin = optLatin(weekBasicGr1A);
            Log.i("LATIN", weekLatin[0][4]);
            String [] [] weekAfterDNL = optDNL(weekLatin);
            Log.i("DNL", weekAfterDNL[0][8]);
            String weekAfterLV2A [][] = LV2SA(weekAfterDNL);
            Log.i("LV2", weekAfterLV2A[0][6]);
            String [][] finalTab = grAng(weekAfterLV2A);
            return finalTab[day][hour];
        }else if(semaine.equals("B")){
            String [][] weekLatin = optLatin(weekBasicGr1B);
            Log.i("LATIN", weekLatin[0][4]);
            String [] [] weekAfterDNL = optDNL(weekLatin);
            Log.i("DNL", weekAfterDNL[0][8]);
            String weekAfterLV2B [][] = LV2SB(weekAfterDNL);
            Log.i("LV2", weekAfterLV2B[0][6]);
            String [][] finalTab = grAng(weekAfterLV2B);
            return finalTab[day][hour];
        }
    }else if(grClasse.equals("2")){
        if(semaine.equals("A")){
            String [][] weekLatin = optLatin(weekBasicGr2A);
            String [] [] weekAfterDNL = optDNL(weekLatin);
            String weekAfterLV2A [][] = LV2SA(weekAfterDNL);
            String [][] finalTab = grAng(weekAfterLV2A);
            return finalTab[day][hour];
        }else if(semaine.equals("B")) {
            String[][] weekLatin = optLatin(weekBasicGr2B);
            String[][] weekAfterDNL = optDNL(weekLatin);
            String weekAfterLV2B[][] = LV2SB(weekAfterDNL);
            String [][] finalTab = grAng(weekAfterLV2B);
            return finalTab[day][hour];
        }
    }
        Log.i("FinalPlanningRt", "Day:" + String.valueOf(currentDay() + "Hour:" + currentSlotHour()));
    }catch (Exception e){
        e.printStackTrace();
        return "!!!!!!! Pas_de_cours";
    }
    return "ERROR ERROR";
}

    public static String [][] optDNL(String [][] week){
        switch (DNL) {
            case "oui":
                week[0][8] = "DNL G12";
                week[4][0] = "DNL B2";
                return week;
            case "non":
                return week;
            default:
                return weekEmpty;
        }
    }

    public static String [][] LV2SA(String [][] week){
        switch (LV2) {
            case "esp":
                week[0][6] = "Espagnol G11";
                week[1][4] = "Espagnol G12";
                week[3][8] = "Espagnol G14";
                return week;
            case "all":
                return week;
            default:
                return weekEmpty;
        }
    }

    public static String [][] grAng(String [][] week){
        switch (grAng) {
            case "ang1":
                week[0][7] = "Anglais G12";
                week[3][0] = "Anglais G15";
                week[4][1] = "Anglais A11";
                return week;
            case "ang2":
                return week;
            default:
                return weekEmpty;
        }
    }

    public static String [][] LV2SB(String [][] week){
        switch (LV2) {
            case "esp":
                week[0][6] = "Espagnol G11";
                week[3][8] = "Espagnol G14";
                week[3][9] = "0 0";
                return week;
            case "all":
                return week;
            default:
                return weekEmpty;
        }
    }

    public static String [][] optLatin(String [][] week){
        switch (latin) {
            case "oui":
                week[0][4] = "Latin B2";
                week[4][6] = "Latin G15";
                week[4][7] = "Latin G15";
                return week;
            case "non":
                return week;
            default:
                return weekEmpty;
        }
    }

    public static String getWeekLetter() {
        int weekNum = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        if(weekNum % 2 == 0){
            return "A";
        }else{
            return "B";
        }
    }

}
