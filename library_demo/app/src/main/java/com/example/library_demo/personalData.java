package com.example.library_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class personalData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
    }
    public void back(View v){
        Intent intent = new Intent(personalData.this, personal.class);
        startActivity(intent);
    }
}
