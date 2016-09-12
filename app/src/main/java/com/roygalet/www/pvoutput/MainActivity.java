package com.roygalet.www.pvoutput;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PVDataDownloader dl = new PVDataDownloader();

        dl.execute();

    }

    private class PVDataDownloader extends AsyncTask<String, Integer, String>{
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            data = "";
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Processing Request...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String urlStr =
                    "http://pvoutput.org/service/r2/getoutput.jsp?sid=47892&key=d2ac813ded2662a3156cc88dd86c92a1050af217";
            URL url = null;
            String data = "";
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn =
                    null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                data = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();

            try
            {
                data=result;
                String[] records = data.split(";");
                String[][] pvoutput = new String[records.length][];
                for(int index=0; index<records.length; index++){
                    pvoutput[index] = records[index].split(",");
                }
                TextView txtData = (TextView)findViewById(R.id.txtData);
                txtData.setText(records.length + " records found");
            }
            catch (Exception e)
            {

            }
            super.onPostExecute(result);
        }
    }
}
