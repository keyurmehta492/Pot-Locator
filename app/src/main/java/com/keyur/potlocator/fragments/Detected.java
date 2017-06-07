package com.keyur.potlocator.fragments;

import com.keyur.potlocator.Activities.MainActivity;
import com.keyur.potlocator.Activities.login;
import com.keyur.potlocator.R;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Detected extends Fragment {
    Context context;
    String url;
    int userid;
    ListView lvPotHole;
    potAdaptor potAdaptor;

    public  static Detected getInstance(Bundle bundle) {
        Detected detected = new Detected();
        detected.setArguments(bundle);
        return detected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detected, container, false);

    }// onCreateView

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        lvPotHole = (ListView)view.findViewById(R.id.lvPothole);
        potAdaptor = new potAdaptor(getActivity(),R.layout.rowlayout);
        lvPotHole.setAdapter(potAdaptor);

        if(isVisible ()) {

            Bundle bundle =getArguments();
            userid = bundle.getInt("userid",0);
            url = getString(R.string.server_url).concat("potlocation");
            new GetDataTask().execute(url);
        }


    }

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
            Log.e("#### error", String.valueOf(e));
            return "Network Error";
        } catch (JSONException e) {

            e.printStackTrace();
            return "IOerror";
        }
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        Log.i("json",result);
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
            Log.e("#### json",e.getMessage());
            e.printStackTrace();
        }
    }//onPostExecute


    private String getData(String urlPath) throws IOException, JSONException {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;

        String get_url = urlPath + "?userid=" + userid;
        Log.d("@@@@ url is ",""+get_url);
        URL url = new URL(get_url);
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
}// class Detected
