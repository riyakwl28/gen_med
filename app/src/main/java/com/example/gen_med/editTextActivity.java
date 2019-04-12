package com.example.gen_med;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editTextActivity extends AppCompatActivity {
    EditText editText;
    Button proceed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        editText=(EditText) findViewById(R.id.editname);
        proceed=(Button)findViewById(R.id.editbutton);

        String medName= getIntent().getStringExtra("Medicine Name");

        editText.setText(medName);



        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(editTextActivity.this, genericmedActivity.class);
                startActivity(i);
            }
        });

    }


}