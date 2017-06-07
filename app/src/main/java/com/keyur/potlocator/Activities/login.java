package com.keyur.potlocator.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button bLogin, bSignUp;
    String url, user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername= (EditText)findViewById(R.id.etUsername);
        etPassword= (EditText)findViewById(R.id.etPassword);
        bLogin = (Button)findViewById(R.id.bLogin);
        bSignUp = (Button)findViewById(R.id.bSignUp);

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,signUp.class);
                startActivity(i);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = etUsername.getText().toString();
                pass = etPassword.getText().toString();

                url = getString(R.string.server_url).concat("login");
                new GetDataTask().execute(url);

            }
        }); //onClick

    }//onCreate

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
            String[] user = new String[3];
            Log.i("json",result);
            //check Login user type
            try {
                JSONObject jsonResponse = new JSONObject(result);
                int count = jsonResponse.getInt("count");

                JSONObject data = jsonResponse.getJSONObject("data");
                String isValidUser = data.getString("isValidUser");

                if (isValidUser.equals("true")) {
                    int user_id = data.getInt("id");
                    String name = data.getString("name");
                    Boolean type = data.getBoolean("is_contractor");

                    if(!type) {
                        //Normal User
                        Intent intent = new Intent(login.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("name",name);
                        etUsername.setText("");
                        etPassword.setText("");
                        login.this.startActivity(intent);

                    }
                    else {
                        //Employee Login
                        Intent intent = new Intent(login.this, employee.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("name",name);
                        etUsername.setText("");
                        etPassword.setText("");
                        login.this.startActivity(intent);
                    }

                }
                else {
                    Toast.makeText(login.this,"Username/Password is Invalid!!",Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                Log.e("json",e.getMessage());
                e.printStackTrace();
            }
        }//onPostExecute

        private String getData(String urlPath) throws IOException, JSONException {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;

            String get_url = urlPath + "?username=" + user + "&password=" + pass;
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

}// class login
