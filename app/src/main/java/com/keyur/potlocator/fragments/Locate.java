package com.keyur.potlocator.fragments;

import android.Manifest;

import com.keyur.potlocator.Activities.login;
import com.keyur.potlocator.Activities.signUp;
import com.keyur.potlocator.R;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keyur.potlocator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class Locate extends Fragment {
    private static final int LOCATION_REQUEST_CODE = 121;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private Button btnSearch, btnLocatePot, btnStartTracking;
    private TextView txtShowLocation;
    private EditText edtLocation;
    private Activity activity;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;
    private List<Location> locationsList;
    private static GpsProviderLocationListener gpsProviderLocationListener;
    private final float zoomLevel = 16;
    Double latitude, longitude;
    int user_id;
    Context context;



    public  static Locate getInstance(Bundle bundle) {
        Locate locate = new Locate();
        locate.setArguments(bundle);
        return locate;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_locate, container, false);
    }//onCreate

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();


        Bundle bundle =getArguments();
        user_id = bundle.getInt("userid",0);
        Log.d("@@@@ uer_id", "" + user_id );

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapCallback());

        setupViews(view);
    }

    private void setupViews(View view) {
        if (view == null)
            return;
        activity = getActivity();
        gpsProviderLocationListener = new GpsProviderLocationListener();

        locationsList = new ArrayList<>();
        edtLocation = (EditText) view.findViewById(R.id.t_location);
        txtShowLocation = (TextView) view.findViewById(R.id.showLocation);
        btnSearch = (Button) view.findViewById(R.id.search);
        btnLocatePot = (Button) view.findViewById(R.id.btn_locate_pot);

        ViewListener viewListener = new ViewListener();

        locationManager = (LocationManager) view.getContext().getSystemService(LOCATION_SERVICE);

        //Setting common listener to button views
        btnLocatePot.setOnClickListener(viewListener);
        btnSearch.setOnClickListener(viewListener);
    }

    private class MapCallback implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (isLocationPermissionGiven()) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
    }//class MapCallback

    private void buildGoogleApiClient() {
        //TODO
    }

    private class ViewListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search: {
                    onSearchClicked(v);
                }
                break;

                case R.id.btn_locate_pot: {
                    if (locationManager == null)
                        return;
                    if (isLocationPermissionGiven()) {
                        Log.d("@@@@ Pot detected", "Get location manually");
                        mMap.setMyLocationEnabled(true);
                        locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsProviderLocationListener);

                    } else {
                        Toast.makeText(activity, "Need location permission", Toast.LENGTH_LONG).show();
                        askForLocationPermission();
                    }
                }
                break;

            } //switch
        } //onClick
    } //class Listener

    private void markLocationOnMap(double latitude, double longitude, String add) {
        LatLng getLatLng = new LatLng(latitude, longitude);

        //save location to server
        String url = getString(R.string.server_url).concat("potlocation");
        new PostDataTask().execute(url);

        //Show location on map
        mMap.addMarker(new MarkerOptions().position(getLatLng).title(add));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng, zoomLevel));

    } //markLocationOnMap

    private void onSearchClicked(View view) {
        String strLocation = edtLocation.getText().toString();

        Geocoder geo_coder = new Geocoder(view.getContext(), Locale.getDefault());
        Address address = null;
        try {
            List<Address> addressList = geo_coder.getFromLocationName(strLocation, 1);
            address = addressList.get(0);
        } catch (IOException e) {
            Log.e("Error", "onSearchClicked ", e);

        }
        if (address == null) {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("Address is ", "" + address.toString());
        latitude = address.getLatitude();
        longitude = address.getLongitude();
        String add = address.getLocality() + ", " + address.getCountryName();
        markLocationOnMap(latitude, longitude, add);

    } //onSearchClicked

    private void markLocationOnMap(android.location.Location _location, String type) {
        latitude = _location.getLatitude();
        longitude = _location.getLongitude();
        markLocationOnMap(latitude, longitude, type);
        Log.d("manual location is ", "" + latitude + ", " + longitude);;
    }

    class PostDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return postData(params[0]);
            } catch (IOException e) {
                Log.e("Error","IOE",e);
                return "Network Error";
            } catch (JSONException e) {
                return "Data Error";
            }
        } //doInBackground

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonResponse = new JSONObject(result);
                Boolean flag = jsonResponse.getBoolean("data");

                if (flag) {
                    String temp = "Pot Hole Detected at: " + latitude + ", " + longitude + "\n";
                    txtShowLocation.setText(temp);

                    locationManager.removeUpdates(gpsProviderLocationListener);
                    Toast.makeText(getActivity(), "PotHole Located!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),R.string.error,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } //onPostExecute

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            JSONObject data =new JSONObject();
            data.put("latitude",latitude);
            data.put("longitude",longitude);
            data.put("user_id",user_id);
            Log.d("@@@@ Location",""+ data);

            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(data.toString());
            bufferedWriter.flush();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");

            }
            Log.d("@@@@ result ",""+ result);
            return result.toString();
        }
    } //class PostData


    private class GpsProviderLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(android.location.Location _location) {
            //mark the current location on map
            markLocationOnMap(_location, "GPS Location");
            Log.d("@@@@ result ","inside location change");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    } //GPSProvider

    private boolean isLocationPermissionGiven() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermissionRevoked = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return permissionGranted && coarseLocationPermissionRevoked;
    }


    private void askForLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Need location permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            mMap.setMyLocationEnabled(true);
        }
    }

} //class Locate
