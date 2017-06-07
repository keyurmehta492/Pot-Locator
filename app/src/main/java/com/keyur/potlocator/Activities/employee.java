package com.keyur.potlocator.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.keyur.potlocator.R;
import com.keyur.potlocator.fragments.potAdaptor;
import com.keyur.potlocator.fragments.potHole;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class employee extends AppCompatActivity {

    ListView lvTotalPothole;
    potAdaptor potAdaptor;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        lvTotalPothole = (ListView)findViewById(R.id.lvTotalPothole);
        potAdaptor = new potAdaptor(this,R.layout.rowlayout);
        lvTotalPothole.setAdapter(potAdaptor);

        url = getString(R.string.server_url).concat("allpotlocation");
        new GetDataTask().execute(url);

    }// onCreate

    class GetDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                return getData(params[0]);
            } catch (IOException e) {
                Log.e("error", String.valueOf(e));
                return "Network Error";
            } catch (JSONException e) {

                e.printStackTrace();
                return "IOerror";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("@@@@ json",result);
            //get user marked locations
            try {
                JSONObject jsonResponse = new JSONObject(result);
                int count = jsonResponse.getInt("count");

                JSONArray data = jsonResponse.getJSONArray("data");
                JSONObject pothole = null;
                for (int i = 0; i < count; i++) {
                    pothole = data.getJSONObject(i);

                    int potid = pothole.getInt("id");
                    Double latitude = pothole.getDouble("latitude");
                    Double longitude = pothole.getDouble("longitude");
                    Boolean isrepaired = pothole.getBoolean("isrepaired");
                    String time = pothole.getString("reportedon");
                    potHole pothole1 = new potHole(potid,latitude,longitude, isrepaired, time);
                    potAdaptor.add(pothole1);

                }


            } catch (JSONException e) {
                Log.e("json",e.getMessage());
                e.printStackTrace();
            }
        }//onPostExecute


        private String getData(String urlPath) throws IOException, JSONException {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;

            Log.d("@@@@ url is ",""+urlPath);
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            Log.d("@@@@ buffer",""+bufferedReader.toString());

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
                Log.i("@@@@ line", ""+line);
            }
            Log.i("@@@@ result", String.valueOf(result));
            return result.toString();
        }
    } //class GetData
}
