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

public class genericmedActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    TextView textView;
    private ProgressDialog pDialog;
    private ListView lv;
    TextView actual;
    Button details;

    // URL to get contacts JSON
    private static String url = "https://api.myjson.com/bins/18hsn0";

    ArrayList<HashMap<String, String>> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genericmed);
        textView = (TextView) findViewById(R.id.tv);
        final String medName = getIntent().getStringExtra("Medicine Name");
        actual=(TextView)findViewById(R.id.actualPrc) ;
        textView.setText(medName);
        medicineList = new ArrayList<>();
        details=(Button)findViewById(R.id.btndtls);
        lv = (ListView) findViewById(R.id.listView);
        new GetMedicine().execute();


        details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //take picture here


                Intent i=new Intent(genericmedActivity.this,detailsActivity.class);
                i.putExtra("Medicine Name",medName);

                startActivity(i);


            }
        });
    }



    private class GetMedicine extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(genericmedActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.myjson.com/bins/18hsn0";
            String jsonStr = sh.makeServiceCall(url);
            String medName=getIntent().getStringExtra("Medicine Name");

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
                   String actualp= medicine.getString("originalprice");
                    actual.setText(actualp);

                   JSONArray alternatives= medicine.getJSONArray("alternatives");
                    JSONArray price= medicine.getJSONArray("price");
//                    JSONArray usage= medicine.getJSONArray("usage");
//                    JSONArray sideeffects= medicine.getJSONArray("sideEffects");
//                    JSONArray salt= medicine.getJSONArray("salt");

                    ArrayList<String> subsList = new ArrayList<String>();
                    JSONArray jsonArray = alternatives;
                    if (jsonArray != null) {
                        int len = jsonArray.length();
                        for (int i=0;i<len;i++){
                            subsList.add(jsonArray.get(i).toString());
                        }
                    }

                    ArrayList<String> priceList = new ArrayList<String>();
                    JSONArray jsonArray1 = price;
                    if (jsonArray1 != null) {
                        int len = jsonArray1.length();
                        for (int i=0;i<len;i++){
                            priceList.add(jsonArray1.get(i).toString());
                        }
                    }

                    // looping through All Alternatives
                    for (int i = 0; i < subsList.size(); i++) {
                        String subs=subsList.get(i);
                        String price1=priceList.get(i);




                        // tmp hash map for single contact
                        HashMap<String, String> medicines = new HashMap<>();

                        // adding each child node to HashMap key => value
                        medicines.put("alternatives", subs);
                        medicines.put("price", price1);


                        // adding contact to contact list
                        medicineList.add(medicines);
                    }
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
            ListAdapter adapter = new SimpleAdapter(genericmedActivity.this, medicineList,
                    R.layout.list_components, new String[]{ "alternatives","price"},
                    new int[]{R.id.name, R.id.price});
            lv.setAdapter(adapter);
        }
    }
}
