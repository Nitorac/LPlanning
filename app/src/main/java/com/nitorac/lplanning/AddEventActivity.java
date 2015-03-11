package com.nitorac.lplanning;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getSavedActionBarColor()));
        ab.setBackgroundDrawable(colorDrawable);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        ab.setIcon(R.drawable.ic_action_event);
        ab.setTitle("   " + getString(R.string.title_activity_add_event));

        ContactsBDD contactBdd = new ContactsBDD(this);
        MySQLiteDatabase sql = new MySQLiteDatabase(this, contactBdd.getName(), null, contactBdd.getVersion());

        contactBdd.open();

        Events events = new Events("Maths", "G16", "24", "10", "2014", 5);
        Events events2 = new Events("Francais", "G15", "10", "15", "2015", 8);
        contactBdd.insertContact(events);
        contactBdd.insertContact(events2);

        ArrayList<Events> allEvents = contactBdd.getAllRowsInArray();

        Map<String, String> map;
        items.clear();
        map = new HashMap<>();
        map.put("line1", "Test");
        map.put("line2", "test" + " " + getSavedActionBarColor());
        items.add(map);
        for(int i = 0; i < allEvents.size(); i++){
            map = new HashMap<>();
            map.put("line1", allEvents.get(i).getMatiere());
            map.put("line2", "LOL");
            items.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(this,items,android.R.layout.simple_list_item_2,keys,controlIds );
        ListView listView = (ListView) findViewById(R.id.listViewDB);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(position == 0){
                }
                else if(position == 1){
                }
                else if(position == 2){
                }
                else if(position == 3){
                }
                else if(position == 4){
                }
                else if(position == 5){
                }
                else if(position == 6){
                }
                else if(position == 7){
                }
            }
        });

        contactBdd.close();
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
