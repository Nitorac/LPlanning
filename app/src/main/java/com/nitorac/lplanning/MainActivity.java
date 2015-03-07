package com.nitorac.lplanning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    public static String APP_VERSION;

    public static Activity thisActivity;

    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    static String dwnload_file_path = "http://nitorac.bugs3.com/LPlanning.apk";

    private static final String APP_SHARED_PREFS = "Lplanning";
    private SharedPreferences.Editor editor;

    final File SDCardRoot = Environment.getExternalStorageDirectory();
    final File file = new File(SDCardRoot,"LPlanning.apk");

    public int hourSlotBack = PlanningVar.currentSlotHour();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APP_VERSION = getString(R.string.versionName);
        thisActivity = this;
        Log.i("REZA", String.valueOf(hourSlotBack));
        if(file.exists()){
              file.delete();
        }
        new Thread(new Runnable() {
            public void run(){
                while(true){
                        if(hourSlotBack != PlanningVar.currentSlotHour()){
                            Intent intent = getIntent();
                            finish();
                            hourSlotBack = PlanningVar.currentSlotHour();
                            startActivity(intent);
                        }
                    try {
                        Thread.sleep(5000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        onCreateStuff();
    }

    public void refreshClick(View v){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
            dialog.setContentView(R.layout.maj_dialog);
            dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.mipmap.ic_launcher);
            dialog.setTitle("Mise à jour");
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

    public void checkUpdate() {
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
                        if (!stringText.equals(APP_VERSION)) {
                            handler.sendEmptyMessage(1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
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

    public void onCreateStuff() {
        boolean setupOk = quest();
        checkUpdate();
            if (setupOk) {
                TextView salle = (TextView) findViewById(R.id.salle);
                TextView matiere = (TextView) findViewById(R.id.matiere);

                String all = PlanningVar.getFinalPlanning();

                String[] arrayStr = all.split(" ", 2);
                if (arrayStr[0].equals("0") || arrayStr[1].equals("Pas_de_cours")) {
                    matiere.setText("!!!!!!");
                    salle.setText("Pas de cours");
                } else {
                    matiere.setText(arrayStr[0]);
                    salle.setText(arrayStr[1]);
                }
            }
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

    public boolean quest(){
        boolean setupOk = true;
        SharedPreferences questSpinner = getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        editor = questSpinner.edit();
        if(questSpinner.getString("latin", "RIEN").equals("RIEN")){
            latinSpinner();
            setupOk = false;
        }else if(questSpinner.getString("latin", "RIEN").equals("oui")){
            PlanningVar.latin = "oui";
        }else if(questSpinner.getString("latin", "RIEN").equals("non")){
            PlanningVar.latin = "non";
        }

        if(questSpinner.getString("DNL", "RIEN").equals("RIEN")){
            DNLSpinner();
            setupOk = false;
        }else if(questSpinner.getString("DNL", "RIEN").equals("oui")){
            PlanningVar.DNL = "oui";
        }else if(questSpinner.getString("DNL", "RIEN").equals("non")){
            PlanningVar.DNL = "non";
        }

        if(questSpinner.getString("LV2", "RIEN").equals("RIEN")){
            LV2Spinner();
            setupOk = false;
        }else if(questSpinner.getString("LV2", "RIEN").equals("all")){
            PlanningVar.LV2 = "all";
        }else if(questSpinner.getString("LV2", "RIEN").equals("esp")){
            PlanningVar.LV2 = "esp";
        }

        if(questSpinner.getString("grAng", "RIEN").equals("RIEN")){
            grAngSpinner();
            setupOk = false;
        }else if(questSpinner.getString("grAng", "RIEN").equals("ang1")){
            PlanningVar.grAng = "ang1";
        }else if(questSpinner.getString("grAng", "RIEN").equals("ang2")){
            PlanningVar.grAng = "ang2";
        }

        if(questSpinner.getString("grClasse", "RIEN").equals("RIEN")){
            grClasseSpinner();
            setupOk = false;
        }else if(questSpinner.getString("grClasse", "RIEN").equals("1")){
            PlanningVar.grClasse = "1";
        }else if(questSpinner.getString("grClasse", "RIEN").equals("2")){
            PlanningVar.grClasse = "2";
        }
        return setupOk;
    }

    public void DNLSpinner() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.DNLTxt));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.DNLoui));
        arrayAdapter.add(getString(R.string.DNLnon));
        builderSingle.setCancelable(false);

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editor.putString("DNL", "oui");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else if (which == 1) {
                            editor.putString("DNL", "non");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });
        builderSingle.show();
    }

    public void latinSpinner() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.latinTxt));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.latinoui));
        arrayAdapter.add(getString(R.string.latinnon));
        builderSingle.setCancelable(false);

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editor.putString("latin", "oui");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else if (which == 1) {
                            editor.putString("latin", "non");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });
        builderSingle.show();
    }


    public void grClasseSpinner() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.grClassSpinner));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.gr1));
        arrayAdapter.add(getString(R.string.gr2));
        builderSingle.setCancelable(false);

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editor.putString("grClasse", "1");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else if (which == 1) {
                            editor.putString("grClasse", "2");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });
        builderSingle.show();
    }

    public void LV2Spinner() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.lv2Txt));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.all));
        arrayAdapter.add(getString(R.string.esp));
        builderSingle.setCancelable(false);

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editor.putString("LV2", "all");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else if (which == 1) {
                            editor.putString("LV2", "esp");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });
        builderSingle.show();
    }

    public void grAngSpinner() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle(getString(R.string.grAng));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.ang1));
        arrayAdapter.add(getString(R.string.ang2));
        builderSingle.setCancelable(false);

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editor.putString("grAng", "ang1");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else if (which == 1) {
                            editor.putString("grAng", "ang2");
                            editor.commit();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                });
        builderSingle.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void settingsActivity(MenuItem mi){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

  /*      showProgress(dwnload_file_path);

        new Thread(new Runnable() {
            public void run() {
                downloadFile();
            }
        }).start();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path){
        dialog = new Dialog(MainActivity.this);
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
