package com.example.gen_med;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class detailsActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    TextView salt;

    TextView uses;
    TextView sideeff;
    Button maps;
    TextView name;

    // URL to get contacts JSON
    private static String url = "https://api.myjson.com/bins/18hsn0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        uses = (TextView) findViewById(R.id.usage);

        salt=(TextView)findViewById(R.id.salts) ;
        name=(TextView)findViewById(R.id.odtls);
       final String medName = getIntent().getStringExtra("Medicine Name");

        sideeff=(TextView)findViewById(R.id.side);

        maps=(Button)findViewById(R.id.btnmaps);
        new GetDetails().execute();


        maps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //take picture here


                Intent i=new Intent(detailsActivity.this,mapActivity.class);

                startActivity(i);


            }
        });
    }



    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(detailsActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.myjson.com/bins/18hsn0";
            String jsonStr = sh.makeServiceCall(url);
            final String medName = getIntent().getStringExtra("Medicine Name");
            name.setText(medName);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject medicine = jsonObj.getJSONObject(medName);
                    if (medicine==null)
                    {
                        Toast.makeText(getApplicationContext(),
                                "No Record Found" ,
                                Toast.LENGTH_LONG).show();
                    }


                    JSONArray salts= medicine.getJSONArray("salts");
                    JSONArray usage= medicine.getJSONArray("usage");
//                    JSONArray usage= medicine.getJSONArray("usage");
                    JSONArray sideeffects= medicine.getJSONArray("sideEffects");
//                    JSONArray salt= medicine.getJSONArray("salt");

                    //ArrayList<String> saltsList = new ArrayList<String>();
                    JSONArray jsonArray = salts;
                    String fsalts=jsonArray.get(0).toString();
                    /*if (jsonArray != null) {
                        int len = jsonArray.length();
                        for (int i=1;i<len;i++){
                            //fsalts+= fsalts.concat((jsonArray.get(i).toString()).concat("\n"));
                           // String t = jsonArray.get(i).toString();
                            fsalts = fsalts + jsonArray.get(1).toString();

                        }
                    }*/

                    //ArrayList<String> usageList = new ArrayList<String>();
                    JSONArray jsonArray1 = usage;
                    String fusage=(jsonArray1.get(0).toString());
//                    if (jsonArray1 != null) {
//                        int len = jsonArray1.length();
//                        for (int i=1;i<len;i++){
//                            fusage+="\n"+jsonArray1.get(i).toString();
//                        }
//                    }

                    //ArrayList<String> usageList = new ArrayList<String>();
                    JSONArray jsonArray2 = sideeffects;
                    String fsideeffects=(jsonArray2.get(0).toString());
//                    if (jsonArray2 != null) {
//                        int len = jsonArray2.length();
//                        for (int i=1;i<len;i++){
//                            fsideeffects+="\n"+jsonArray.get(i).toString();
//                        }
//                    }

                    salt.setText(fsalts);
                    uses.setText(fusage);
                    sideeff.setText(fsideeffects);


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
