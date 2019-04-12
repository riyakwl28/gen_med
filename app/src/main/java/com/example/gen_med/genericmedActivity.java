package com.example.gen_med;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class genericmedActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    TextView textView;
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "https://api.myjson.com/bins/woj84";

    ArrayList<HashMap<String, String>> medicineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genericmed);
        textView=(TextView) findViewById(R.id.tv);
        String medName= getIntent().getStringExtra("Medicine Name");
        textView.setText(medName);
    }
}
