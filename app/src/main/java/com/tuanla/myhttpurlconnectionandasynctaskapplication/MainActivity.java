package com.tuanla.myhttpurlconnectionandasynctaskapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuanla.myhttpurlconnectionandasynctaskapplication.model.Address;
import com.tuanla.myhttpurlconnectionandasynctaskapplication.model.Company;
import com.tuanla.myhttpurlconnectionandasynctaskapplication.model.Geo;
import com.tuanla.myhttpurlconnectionandasynctaskapplication.model.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnRun;
    ListView listView;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRun = (Button) findViewById(R.id.btn_run);
        listView = (ListView) findViewById(R.id.list_item_main);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HttpTask httpTask = new HttpTask();
                        httpTask.execute("https://jsonplaceholder.typicode.com/users?fbclid=IwAR2farXAGVzed0kilDtHnbT5HSq03y3rI1mrm6V7nsy33BVDqNLKPA0H2U0");
                        for (User user : httpTask.getUsers()) {
                            names.add(user.getName());
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, names);

                        listView.setAdapter(adapter);
                    }
                });

            }
        });
    }


    private class HttpTask extends AsyncTask<String, Void, String> {

        private List<User> users = new ArrayList<User>();

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                //parsing json:
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        User user = new User();
                        Address address = new Address();
                        Geo geo = new Geo();
                        Company company = new Company();

                        geo.setLat(jsonArray.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lat"));
                        geo.setLng(jsonArray.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lng"));

                        address.setStreet(jsonArray.getJSONObject(i).getJSONObject("address").getString("street"));
                        address.setSuite(jsonArray.getJSONObject(i).getJSONObject("address").getString("suite"));
                        address.setCity(jsonArray.getJSONObject(i).getJSONObject("address").getString("city"));
                        address.setZipcode(jsonArray.getJSONObject(i).getJSONObject("address").getString("zipcode"));
                        address.setGeo(geo);

                        company.setName(jsonArray.getJSONObject(i).getJSONObject("company").getString("name"));
                        company.setCatchPhrase(jsonArray.getJSONObject(i).getJSONObject("company").getString("catchPhrase"));
                        company.setBs(jsonArray.getJSONObject(i).getJSONObject("company").getString("bs"));

                        user.setId(jsonArray.getJSONObject(i).getInt("id"));
                        user.setName(jsonArray.getJSONObject(i).getString("name"));
                        user.setUsername(jsonArray.getJSONObject(i).getString("username"));
                        user.setEmail(jsonArray.getJSONObject(i).getString("email"));
                        user.setAddress(address);
                        user.setPhone(jsonArray.getJSONObject(i).getString("phone"));
                        user.setWebsite(jsonArray.getJSONObject(i).getString("website"));
                        user.setCompany(company);

                        this.users.add(user);
                    }
                }

                //test list user:
                for (User user : this.users) {
                    System.out.println(user.getId());
                    System.out.println(user.getName());
                    System.out.println(user.getUsername());
                    System.out.println(user.getEmail());
                    System.out.println("Address:");
                    System.out.println("\t" + user.getAddress().getStreet());
                    System.out.println("\t" + user.getAddress().getSuite());
                    System.out.println("\t" + user.getAddress().getCity());
                    System.out.println("\t" + user.getAddress().getZipcode());
                    System.out.println("\tGeo:");
                    System.out.println("\t\t" + user.getAddress().getGeo().getLat());
                    System.out.println("\t\t" + user.getAddress().getGeo().getLng());
                    System.out.println(user.getPhone());
                    System.out.println(user.getWebsite());
                    System.out.println("Company:");
                    System.out.println("\t" + user.getCompany().getName());
                    System.out.println("\t" + user.getCompany().getCatchPhrase());
                    System.out.println("\t" + user.getCompany().getBs());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
