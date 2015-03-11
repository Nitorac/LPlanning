package com.nitorac.lplanning;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddEventActivity extends ActionBarActivity {

    private static final String APP_SHARED_PREFS_COLOR = "LplanningColor";
    private SharedPreferences.Editor editor;

    private static final List<Map<String,String>> items = new ArrayList<>();
    private static final String[] keys = { "line1", "line2" };
    private static final int[] controlIds = { android.R.id.text1, android.R.id.text2 };

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

        final EventsBDD eventsBdd = new EventsBDD(this);

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
            map.put("line2", "Le " + event.getJour() + "/" + event.getMois() + "/" + event.getAnnee());
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
                        eventsBdd.open();
                        Log.i("Position", String.valueOf(position));
                        Log.i("Item ID", String.valueOf(allEvents.get(i).getId()));
                        eventsBdd.removeEventWithID(allEvents.get(i).getId());
                        eventsBdd.close();
                        Intent intent = new Intent(AddEventActivity.this, AddEventActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        });

        eventsBdd.close();
        TextView any_item = (TextView) findViewById(R.id.any_item_txtView);
        if(listView.getCount() == 0){ any_item.setVisibility(View.VISIBLE); }else{ any_item.setVisibility(View.INVISIBLE);}
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

    public void addEvent(MenuItem mi){
        final EventsBDD eventsBdd = new EventsBDD(this);
        eventsBdd.open();
        Events events = new Events("Maths", "G16", "24", "10", "2014", 5);
        Events events2 = new Events("Francais", "G15", "10", "15", "2015", 8);
        Events events3 = new Events("Physiques", "G15", "10", "15", "2015", 8);
        Events events4 = new Events("Test", "G15", "10", "15", "2015", 8);
        eventsBdd.insertEvent(events);
        eventsBdd.insertEvent(events2);
        eventsBdd.insertEvent(events3);
        eventsBdd.insertEvent(events4);
        eventsBdd.close();
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
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
