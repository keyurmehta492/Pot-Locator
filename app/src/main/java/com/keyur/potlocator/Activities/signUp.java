package com.keyur.potlocator.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static android.app.PendingIntent.getActivity;

public class signUp extends AppCompatActivity {

    EditText etFName, etLName, etEmail, etUname, etPass1,etPass2;
    String fname,lname,username,email,password1,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFName= (EditText)findViewById(R.id.etFName);
        etLName= (EditText)findViewById(R.id.etLName);
        etEmail= (EditText)findViewById(R.id.etEmail);
        etUname= (EditText)findViewById(R.id.etUname);
        etPass1= (EditText)findViewById(R.id.etPass1);
        etPass2= (EditText)findViewById(R.id.etPass2);
        Button bSignup = (Button)findViewById(R.id.bSignup);

        bSignup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fname = etFName.getText().toString();
                lname = etLName.getText().toString();
                username = etUname.getText().toString();
                email = etEmail.getText().toString();
                password1 = etPass1.getText().toString();
                password2 = etPass2.getText().toString();

                if(password1.equals(password2)) {
                String url = getString(R.string.server_url).concat("register");
                    new PostDataTask().execute(url);

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(signUp.this);
                    builder.setMessage("Password mismatch")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }

            }
        }); //signUp Button onclick

    } //onCreate

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
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonResponse = new JSONObject(result);
                String data = jsonResponse.getString("data");

                if (data.equals("true")) {
                    Toast.makeText(signUp.this,"User Registered Successfully!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(signUp.this, login.class);
                    signUp.this.startActivity(intent);
                }
                else {
                    Toast.makeText(signUp.this,R.string.error,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            JSONObject data =new JSONObject();
            data.put("first_name",fname);
            data.put("last_name",lname);
            data.put("email",email);
            data.put("username",username);
            data.put("password",password1);

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
            return result.toString();
        }
    } //class PostData

} //class signUp
