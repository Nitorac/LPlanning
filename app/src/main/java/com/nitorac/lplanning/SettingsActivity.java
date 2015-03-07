package com.nitorac.lplanning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nitorac.lplanning.colorpicker.ColorPicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SettingsActivity extends ActionBarActivity {

    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    static String dwnload_file_path = MainActivity.dwnload_file_path;
    final File SDCardRoot = Environment.getExternalStorageDirectory();
    final File file = new File(SDCardRoot,"LPlanning.apk");

    private static final String APP_SHARED_PREFS = "Lplanning";
    private static final String APP_SHARED_PREFS_COLOR = "LplanningColor";

    private SharedPreferences.Editor editor;

    private static final List<Map<String,String>> items =
            new ArrayList<>();
    private static final String[] keys =
            { "line1", "line2" };
    private static final int[] controlIds =
            { android.R.id.text1,
                    android.R.id.text2 };

    @Override
    @Deprecated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.support.v7.app.ActionBar ab =  getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getSavedActionBarColor()));
        ab.setBackgroundDrawable(colorDrawable);

        String credits = getString(R.string.credits);
        String app_name = getString(R.string.app_name);
        String version = MainActivity.APP_VERSION;
        String copyCredits = getString(R.string.copyRight);
        String reset = getString(R.string.reset);
        String grClasseSet = getString(R.string.grClasseSet);
        String grAngSet = getString(R.string.grAngSet);
        String LV2Set = getString(R.string.LV2Set);
        String optLatinSet = getString(R.string.optLatinSet);
        String optDNLSet = getString(R.string.optDNLSet);
        String checkUpdatesTxt = getString(R.string.checkUpdatesTxt);
        String currentVerTxt = getString(R.string.currentVerTxt);
        String cooMobileTxt = getString(R.string.cooMobile);
        String changeColorActionBarTxt = getString(R.string.changeColorTxt);
        String currentColor = getString(R.string.currentColor);
        String changeColorBackgroundTxt = getString(R.string.changeColorBackgroundTxt);
        String changeColorTextSalleTxt = getString(R.string.changeColorTextSalleTxt);
        String resetColor = getString(R.string.resetColorTxt);
        String resetColorSub = getString(R.string.resetColorTxtSub);
        String changeColorTextMatiereTxt = getString(R.string.changeColorMatiereSalleTxt);

        String allemand = "allemande";
        String espagnol = "espagnol";
        String LV2 = PlanningVar.LV2.equals("all") ? allemand : espagnol;

        String active = "activée";
        String desactive = "désactivée";
        String latin = PlanningVar.latin.equals("oui") ? active : desactive;
        String DNL = PlanningVar.DNL.equals("oui") ? active : desactive;
        String mobileCoo = getMobileActivated() ? getString(R.string.mobileOn) : getString(R.string.mobileOff);

        Map<String, String> map;
        items.clear();
        map = new HashMap<>();
        map.put("line1", changeColorTextSalleTxt);
        map.put("line2", currentColor + " " + getSavedTextSalleColor());
        items.add(map);
        map = new HashMap<>();
        map.put("line1", changeColorTextMatiereTxt);
        map.put("line2", currentColor + " " + getSavedTextMatiereColor());
        items.add(map);
        map = new HashMap<>();
        map.put("line1", changeColorBackgroundTxt);
        map.put("line2", currentColor + " " + getSavedBackgroundColor());
        items.add(map);
        map = new HashMap<>();
        map.put("line1", changeColorActionBarTxt);
        map.put("line2", currentColor + " " + getSavedActionBarColor());
        items.add(map);
        map = new HashMap<>();
        map.put("line1", cooMobileTxt);
        map.put("line2", mobileCoo);
        items.add(map);
        map = new HashMap<>();
        map.put("line1", checkUpdatesTxt);
        map.put("line2", currentVerTxt + " v" + version);
        items.add(map);
        map = new HashMap<>();
        map.put("line1", resetColor);
        map.put("line2", resetColorSub);
        items.add(map);
        map = new HashMap<>();
        map.put("line1", reset);
        map.put("line2", grClasseSet + " " + PlanningVar.grClasse + "\n" +
                grAngSet + " " + PlanningVar.grAng.substring(PlanningVar.grAng.length() - 1, PlanningVar.grAng.length()) + "\n" +
                LV2Set + " " + LV2 + "\n" +
                optLatinSet + " " + latin + "\n" +
                optDNLSet + " " + DNL + "\n");
        items.add(map);
        map = new HashMap<>();
        map.put("line1", credits);
        map.put("line2", app_name + " v" + version + "\n"
                + copyCredits);
        items.add(map);

        ListAdapter adapter = new SimpleAdapter(
                this,
                items,
                android.R.layout.simple_list_item_2,
                keys,
                controlIds );
        ListView listView = (ListView) findViewById(R.id.listViewSettings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(position == 0){
                    handlerPickerTextSalle.sendEmptyMessage(1);
                }
                else if(position == 1){
                    handlerPickerTextMatiere.sendEmptyMessage(1);
                }
                else if(position == 2){
                    handlerPickerBackground.sendEmptyMessage(1);
                }
                else if(position == 3){
                    handlerPicker.sendEmptyMessage(1);
                }
                else if(position == 4){
                    mobileSpinner();
                }
                else if(position == 5){
                    manuallyUpdates();
                }
                else if(position == 6){
                    SettingsActivity.this.getSharedPreferences("LplanningColor", 0).edit().clear().commit();
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                }
                else if(position == 7){
                    SettingsActivity.this.getSharedPreferences("Lplanning", 0).edit().clear().commit();
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
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

    public String getSavedBackgroundColor(){
        SharedPreferences color = getSharedPreferences(APP_SHARED_PREFS_COLOR, Activity.MODE_PRIVATE);
        editor = color.edit();
        if(color.getString("backgroundColor", "#E6E6E6").equals("#E6E6E6")){
            editor.putString("backgroundColor", "#E6E6E6");
            editor.commit();
            return "#E6E6E6";
        }else{
            return color.getString("backgroundColor", "#E6E6E6");
        }
    }

    public String getSavedTextSalleColor(){
        SharedPreferences color = getSharedPreferences(APP_SHARED_PREFS_COLOR, Activity.MODE_PRIVATE);
        editor = color.edit();
        if(color.getString("textSalleColor", "#3443EB").equals("#3443EB")){
            editor.putString("textSalleColor", "#3443EB");
            editor.commit();
            return "#3443EB";
        }else{
            return color.getString("textSalleColor", "#3443EB");
        }
    }

    public String getSavedTextMatiereColor(){
        SharedPreferences color = getSharedPreferences(APP_SHARED_PREFS_COLOR, Activity.MODE_PRIVATE);
        editor = color.edit();
        if(color.getString("textMatiereColor", "#2c2d36").equals("#2c2d36")){
            editor.putString("textMatiereColor", "#2c2d36");
            editor.commit();
            return "#2c2d36";
        }else{
            return color.getString("textMatiereColor", "#2c2d36");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return false;
    }
    public boolean haveNetworkConnection(boolean mobileActived) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if(mobileActived) {
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public boolean getMobileActivated(){
        boolean isActivated = true;
        SharedPreferences mobileActivated = getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        editor = mobileActivated.edit();
        if(mobileActivated.getString("mobile", "RIEN").equals("RIEN")){
            isActivated = true;
        }else if(mobileActivated.getString("mobile", "RIEN").equals("oui")){
            isActivated = true;
        }else if(mobileActivated.getString("mobile", "RIEN").equals("non")){
            isActivated = false;
        }
        return isActivated;
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            final Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
            dialog.setContentView(R.layout.maj_dialog);
            dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);
            dialog.setTitle("Mise à jour");
            dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);
            WebView changelogs = (WebView) dialog.findViewById(R.id.webView1);
            changelogs.loadUrl("http://nitorac.url.ph/Changelogs.html");
            Button non = (Button) dialog.findViewById(R.id.nonBtn);
            Button maj = (Button) dialog.findViewById(R.id.btnMaj);

            non.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            maj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress(dwnload_file_path);
                    new Thread(new Runnable() {
                        public void run() {
                            downloadFile();
                        }
                    }).start();
                }
            });

            dialog.show();
        }
    };

    public void colorDialog(final String prefKey, String hexInput){
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.color_picker);
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);
        dialog.setTitle("Choisissez la couleur");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);

        final ColorPicker colorPicker = (ColorPicker) dialog.findViewById(R.id.colorPicker);
        int setupColor = Color.parseColor(hexInput);
        colorPicker.setColor(setupColor);
        Button validBtn = (Button) dialog.findViewById(R.id.validBtn);
        validBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences colorPick = getSharedPreferences(APP_SHARED_PREFS_COLOR, Activity.MODE_PRIVATE);
                editor = colorPick.edit();
                int color = colorPicker.getColor();
                String hex = String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
                editor.putString(prefKey, hex);
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    final Handler handlerPicker = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            colorDialog("actionBarColor", getSavedActionBarColor());
        }
    };

    final Handler handlerPickerBackground = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            colorDialog("backgroundColor", getSavedBackgroundColor());
        }
    };

    final Handler handlerPickerTextSalle = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            colorDialog("textSalleColor", getSavedTextSalleColor());
        }
    };

    final Handler handlerPickerTextMatiere = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            colorDialog("textMatiereColor", getSavedTextMatiereColor());
        }
    };

    final Handler handlerNoUp = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("Mise à jour");
            builder.setMessage("Aucune mise à jour disponible");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    @Override
    public void onBackPressed(){
            super.onBackPressed();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
    }

    public void manuallyUpdates(){
        if (haveNetworkConnection(getMobileActivated())) {
            new Thread() {
                @Override
                public void run() {
                    String path = "http://nitorac.bugs3.com/LPlanningVersion.txt";
                    try {
                        URL textUrl = new URL(path);
                        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));
                        String StringBuffer;
                        String stringText = "";
                        while ((StringBuffer = bufferReader.readLine()) != null) {
                            stringText += StringBuffer;
                        }
                        bufferReader.close();
                        if (!stringText.equals(MainActivity.APP_VERSION)) {
                            handler.sendEmptyMessage(1);
                        }else{
                            handlerNoUp.sendEmptyMessage(1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

    public void mobileSpinner(){
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(SettingsActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.cooMobile));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                SettingsActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.mobileCooActived));
        arrayAdapter.add(getString(R.string.mobileCooDesactived));
        builderSingle.setNegativeButton(getString(R.string.cancel_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if(getMobileActivated()){
                                Toast.makeText(SettingsActivity.this, getString(R.string.alreadyOpt), Toast.LENGTH_SHORT).show();
                            } else {
                                editor.putString("mobile", "oui");
                                editor.commit();
                                Intent intent = new Intent (SettingsActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        } else if (which == 1) {
                            if(!getMobileActivated()){
                                Toast.makeText(SettingsActivity.this, getString(R.string.alreadyOpt), Toast.LENGTH_SHORT).show();
                            }else{
                                editor.putString("mobile", "non");
                                editor.commit();
                                Intent intent = new Intent (SettingsActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                    }
                });
        builderSingle.show();
    }


    void downloadFile(){

        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            //create a new file, to save the downloaded file

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Téléchargement : " + downloadedSize + "Ko / " + totalSize + "Ko (" + (int)per + "%)" );
                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    openApk();
                }
            });

        } catch (final MalformedURLException e) {
            showError("Erreur : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Erreur : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Erreur : Pas de connexion Internet " + e);
        }
    }

    private void openApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        finish();
        startActivity(intent);
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SettingsActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path){
        dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progr_bar_dialog_update);
        dialog.setCancelable(false);
        dialog.setTitle("Progression du téléchargement");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Téléchargement depuis " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Démarrage du téléchargement ...");
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progr_bar));
    }
}
