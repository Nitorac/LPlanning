package com.nitorac.lplanning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddEventActivity extends ActionBarActivity {

    private static final String APP_SHARED_PREFS_COLOR = "LplanningColor";
    private SharedPreferences.Editor editor;

    private static final List<Map<String,String>> items = new ArrayList<>();
    private static final String[] keys = { "line1", "line2" };
    private static final int[] controlIds = { android.R.id.text1, android.R.id.text2 };

    private final EventsBDD eventsBdd = new EventsBDD(this);

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    private Button horairePicker;
    private Button supprBtn;
    private Button datePickerBtn;

    private String finalValuePicker = "ERROR";


    private SimpleAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getSavedActionBarColor()));
        ab.setBackgroundDrawable(colorDrawable);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        ab.setIcon(R.drawable.ic_action_event);
        ab.setTitle("   " + getString(R.string.title_activity_add_event));

        eventsBdd.open();

        if(!eventsBdd.isTableEventsExists()){
            eventsBdd.recreateDB();
        }

        final ArrayList<Events> allEvents = eventsBdd.getAllRowsInArray();

        Map<String, String> map;
        items.clear();
        for(int i = 0; i < allEvents.size(); i++){
            map = new HashMap<>();
            Events event = allEvents.get(i);
            map.put("line1", event.getMatiere() + "  " + event.getSalle());
            map.put("line2", "Le " + event.getJour() + "/" + event.getMois() + "/" + event.getAnnee() + " " +
            getHoraire(event.getTranche_horaire(), event.getAnnee(), event.getMois(), event.getJour()));
            items.add(map);
        }
        adapter = new SimpleAdapter(this,items,android.R.layout.simple_list_item_2,keys,controlIds );
        final ListView listView = (ListView) findViewById(R.id.listViewDB);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                for(int i = 0; i < allEvents.size(); i++){
                    if(position == i) {
                        String jour = allEvents.get(i).getJour();
                        if(Integer.parseInt(jour) < 10 && !jour.contains("0")){
                            jour = "0" + jour;
                        }
                        String mois = allEvents.get(i).getMois();
                        if(Integer.parseInt(mois) < 10 && !mois.contains("0")){
                            mois = "0" + mois;
                        }

                        String salle = allEvents.get(i).getSalle();
                        String matiere = allEvents.get(i).getMatiere();
                        String date = jour + "/" + mois + "/" + allEvents.get(i).getAnnee();
                        int horaire = allEvents.get(i).getTranche_horaire();
                        int thisId = allEvents.get(i).getId();
                        modifierAddEventDialog(salle, matiere, date, horaire, thisId);
                    }
                }
            }
        });

        eventsBdd.close();
        TextView any_item = (TextView) findViewById(R.id.any_item_txtView);
        if(listView.getCount() == 0){ any_item.setVisibility(View.VISIBLE); }else{ any_item.setVisibility(View.INVISIBLE);}
    }

    public boolean getMercredi(){
        String text = datePickerBtn.getText().toString();
        String[] temp = text.split("/");
        Log.i("getMercredi()","Annee et mois et jour :" + temp[2] + " " + temp[1] + " " + temp[2]);
        if(getWeekDay(temp[2], temp[1], temp[0]).equals("mercredi")){
            return true;
        }else{
            return false;
        }
    }

    public int getHoraireInt(String horaireStr){
        if(getMercredi()){
            switch (horaireStr) {
                case "De 8H à 9H":return 0;
                case "De 9H à 10H":return 1;
                case "De 10H à 11H45":return 2;
                case "De 11H45 à 13H":return 3;
            }
            return 255;
        }else{
            switch (horaireStr){
                case "De 8H à 9H":return 0;
                case "De 9H à 10H":return 1;
                case "De 10H à 11H":return 2;
                case "De 11H à 12H":return 3;
                case "De 12H à 13H":return 4;
                case "De 13H à 14H":return 5;
                case "De 14H à 15H":return 6;
                case "De 15H à 16H":return 7;
                case "De 16H à 17H":return 8;
                case "De 17H à 18H":return 9;
            }
            return 255;
        }
    }

    public void createHoraireDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Séléctionnez un horaire");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.horaire_dialog, null);
        builder.setView(dialogView);
        builder.setIcon(R.mipmap.ic_launcher);
        final String[] arrayString;
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
        numberPicker.setEnabled(true);
        if(getMercredi()){
            arrayString = new String[]{"De 8H à 9H", "De 9H à 10H", "De 10H à 11H45", "De 11H45 à 13H"};
        }else{
            arrayString = new String[]{"De 8H à 9H", "De 9H à 10H", "De 10H à 11H", "De 11H à 12H", "De 12H à 13H", "De 13H à 14H", "De 14H à 15H", "De 15H à 16H", "De 16H à 17H", "De 17H à 18H"};
        }
        numberPicker.setMinValue(0);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);
        numberPicker.setMaxValue(arrayString.length - 1);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return arrayString[value];
            }
        });

        numberPicker.setValue(1);
        numberPicker.setWrapSelectorWheel(true);
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finalValuePicker = arrayString[numberPicker.getValue()];
                horairePicker.setText(arrayString[numberPicker.getValue()]);
            }

        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void confirmSuppr(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmer la suppression");
        builder.setIcon(R.drawable.ic_delete);
        builder.setCancelable(true);
        builder.setMessage("Etes-vous sûr de vouloir supprimer cette exception ?");

        builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventsBdd.open();
                eventsBdd.removeEventWithID(id);
                eventsBdd.close();
                dialog.dismiss();
                Intent intent = new Intent(AddEventActivity.this, AddEventActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        Dialog finalDialog = builder.create();
        finalDialog.show();
    }

    public void modifierAddEventDialog(final String salle,final String matiere,final String date,final int horaire,final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.modify_event_dialog, null);
        builder.setView(dialogView);
        builder.setIcon(R.drawable.ic_modify);
        builder.setTitle("Modifier un événement");
        builder.setCancelable(false);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);

        datePickerBtn = (Button) dialogView.findViewById(R.id.dateBtn);
        final EditText matiereEditText = (EditText) dialogView.findViewById(R.id.matiereEditText);
        final EditText salleEditText = (EditText) dialogView.findViewById(R.id.salleEditText);
        horairePicker = (Button) dialogView.findViewById(R.id.horaireBtn);
        supprBtn = (Button) dialogView.findViewById(R.id.supprBtn);

        String[] dateSplit = date.split("/");

        salleEditText.setText(salle);
        matiereEditText.setText(matiere);
        datePickerBtn.setText(date);
        horairePicker.setText(getHoraireMaj(horaire,dateSplit[2], dateSplit[1], dateSplit[0]));

        horairePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(datePickerBtn.getText().equals("Entrez une date")) {
                    Toast.makeText(AddEventActivity.this, "Veuillez entrez d'abord une date", Toast.LENGTH_LONG).show();
                }else{
                    createHoraireDialog();
                }
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        String dayNb = String.valueOf(newDate.get(Calendar.DAY_OF_MONTH));
                        String monthNb = String.valueOf(newDate.get(Calendar.MONTH)+1);
                        if(newDate.get(Calendar.DAY_OF_MONTH) < 10){
                            dayNb = "0" + String.valueOf(newDate.get(Calendar.DAY_OF_MONTH));
                        }
                        if(newDate.get(Calendar.MONTH)+1 < 10){
                            monthNb = "0" + String.valueOf(newDate.get(Calendar.MONTH)+1);
                        }
                        datePickerBtn.setText(dayNb + "/" + monthNb + "/" + newDate.get(Calendar.YEAR));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                horairePicker.setText("Entrez un horaire");
            }
        });
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();

        alert.findViewById(R.id.supprBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                confirmSuppr(id);
            }
        });

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(salleEditText.getText().toString().isEmpty()){
                    Toast.makeText(AddEventActivity.this, "Veuillez entrer une salle", Toast.LENGTH_SHORT).show();
                }else if(matiereEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Veuillez entrer une matière", Toast.LENGTH_SHORT).show();
                }else if(datePickerBtn.getText().equals("Entrez une date")){
                    Toast.makeText(AddEventActivity.this, "Veuillez sélectionnez une date", Toast.LENGTH_SHORT).show();
                }else if(horairePicker.getText().equals("Entrez un horaire")){
                    Toast.makeText(AddEventActivity.this, "Veuillez sélectionnez un horaire", Toast.LENGTH_SHORT).show();
                }else{
                    String[] temp = datePickerBtn.getText().toString().split("/");
                    Events event = new Events(matiereEditText.getText().toString(), salleEditText.getText().toString(), temp[0], temp[1], temp[2], getHoraireInt(horairePicker.getText().toString()));
                    eventsBdd.open();
                    eventsBdd.updateEvent(id, event);
                    eventsBdd.close();
                    alert.dismiss();
                    Intent intent = new Intent(AddEventActivity.this, AddEventActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    public void createAddEventDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_event_dialog, null);
        builder.setView(dialogView);
        builder.setIcon(R.drawable.ic_add);
        builder.setTitle("Ajouter un événement");
        builder.setCancelable(false);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);

        datePickerBtn = (Button) dialogView.findViewById(R.id.dateBtn);
        final EditText matiereEditText = (EditText) dialogView.findViewById(R.id.matiereEditText);
        final EditText salleEditText = (EditText) dialogView.findViewById(R.id.salleEditText);
        horairePicker = (Button) dialogView.findViewById(R.id.horaireBtn);


        horairePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(datePickerBtn.getText().equals("Entrez une date")) {
                    Toast.makeText(AddEventActivity.this, "Veuillez entrez d'abord une date", Toast.LENGTH_LONG).show();
                }else{
                    createHoraireDialog();
                }
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        String dayNb = String.valueOf(newDate.get(Calendar.DAY_OF_MONTH));
                        String monthNb = String.valueOf(newDate.get(Calendar.MONTH)+1);
                        if(newDate.get(Calendar.DAY_OF_MONTH) < 10){
                            dayNb = "0" + String.valueOf(newDate.get(Calendar.DAY_OF_MONTH));
                        }
                        if(newDate.get(Calendar.MONTH)+1 < 10){
                            monthNb = "0" + String.valueOf(newDate.get(Calendar.MONTH)+1);
                        }
                        datePickerBtn.setText(dayNb + "/" + monthNb + "/" + newDate.get(Calendar.YEAR));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                horairePicker.setText("Entrez un horaire");
            }
        });
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

       final AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(salleEditText.getText().toString().isEmpty()){
                    Toast.makeText(AddEventActivity.this, "Veuillez entrer une salle", Toast.LENGTH_SHORT).show();
                }else if(matiereEditText.getText().toString().isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Veuillez entrer une matière", Toast.LENGTH_SHORT).show();
                }else if(datePickerBtn.getText().equals("Entrez une date")){
                    Toast.makeText(AddEventActivity.this, "Veuillez sélectionnez une date", Toast.LENGTH_SHORT).show();
                }else if(horairePicker.getText().equals("Entrez un horaire")){
                    Toast.makeText(AddEventActivity.this, "Veuillez sélectionnez un horaire", Toast.LENGTH_SHORT).show();
                }else{
                    String[] temp = datePickerBtn.getText().toString().split("/");
                    Events event = new Events(matiereEditText.getText().toString(), salleEditText.getText().toString(), temp[0], temp[1], temp[2], getHoraireInt(horairePicker.getText().toString()));
                    eventsBdd.open();
                    eventsBdd.insertEvent(event);
                    eventsBdd.close();
                    alert.dismiss();
                    Intent intent = new Intent(AddEventActivity.this, AddEventActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }


    public String getSavedActionBarColor(){
        SharedPreferences color = getSharedPreferences(APP_SHARED_PREFS_COLOR, Activity.MODE_PRIVATE);
        editor = color.edit();
        if(color.getString("actionBarColor", "#428AC9").equals("#428AC9")){
            editor.putString("actionBarColor", "#428AC9");
            editor.commit();
            return "#428AC9";
        }else{
            return color.getString("actionBarColor", "#428AC9");
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getWeekDay(String annee, String mois, String jour){
        SimpleDateFormat inFormat = new SimpleDateFormat("ddMMyyyy");
        try {
        Date date = inFormat.parse(jour + mois + annee);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            return outFormat.format(date);
        }catch (Exception e){e.printStackTrace(); return "null";}
    }

    public String getHoraire(int tranche_horaire, String annee, String mois, String jour){
            if(getWeekDay(annee,mois,jour).equals("mercredi")){
                if(tranche_horaire == 0){
                    return "de 8H à 9H";
                }else if(tranche_horaire == 1){
                    return "de 9H à 10H";
                }else if(tranche_horaire == 2){
                    return "de 10H à 11H45";
                }else if(tranche_horaire == 3){
                    return "de 11H45 à 13H";
                }
            }else {
                if (tranche_horaire == 0) {
                    return "de 8H à 9H";
                } else if (tranche_horaire == 1) {
                    return "de 9H à 10H";
                } else if (tranche_horaire == 2) {
                    return "de 10H à 11H";
                } else if (tranche_horaire == 3) {
                    return "de 11H à 12H";
                } else if (tranche_horaire == 4) {
                    return "de 12H à 13H";
                } else if (tranche_horaire == 5) {
                    return "de 13H à 14H";
                } else if (tranche_horaire == 6) {
                    return "de 14H à 15H";
                } else if (tranche_horaire == 7) {
                    return "de 15H à 16H";
                } else if (tranche_horaire == 8) {
                    return "de 16H à 17H";
                } else if (tranche_horaire == 9) {
                    return "de 17H à 18H";
                }
            }
        return "ERROR";
    }

    public String getHoraireMaj(int tranche_horaire, String annee, String mois, String jour){
        if(getWeekDay(annee,mois,jour).equals("mercredi")){
            if(tranche_horaire == 0){
                return "De 8H à 9H";
            }else if(tranche_horaire == 1){
                return "De 9H à 10H";
            }else if(tranche_horaire == 2){
                return "De 10H à 11H45";
            }else if(tranche_horaire == 3){
                return "De 11H45 à 13H";
            }
        }else {
            if (tranche_horaire == 0) {
                return "De 8H à 9H";
            } else if (tranche_horaire == 1) {
                return "De 9H à 10H";
            } else if (tranche_horaire == 2) {
                return "De 10H à 11H";
            } else if (tranche_horaire == 3) {
                return "De 11H à 12H";
            } else if (tranche_horaire == 4) {
                return "De 12H à 13H";
            } else if (tranche_horaire == 5) {
                return "De 13H à 14H";
            } else if (tranche_horaire == 6) {
                return "De 14H à 15H";
            } else if (tranche_horaire == 7) {
                return "De 15H à 16H";
            } else if (tranche_horaire == 8) {
                return "De 16H à 17H";
            } else if (tranche_horaire == 9) {
                return "De 17H à 18H";
            }
        }
        return "ERROR";
    }

    public void addEvent(MenuItem mi){
        createAddEventDialog();
    }

    public void deleteEvent(MenuItem mi){
        EventsBDD eventsBdd = new EventsBDD(this);
        eventsBdd.open();
        eventsBdd.reset();
        eventsBdd.recreateDB();
        eventsBdd.close();
        Intent intent = new Intent(this, this.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

}
