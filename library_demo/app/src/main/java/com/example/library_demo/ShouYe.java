package com.example.library_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShouYe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_ye);
    }
    public void xianshang(View v){
        Intent intent = new Intent(ShouYe.this, zixishi.class);
        startActivity(intent);
    }
    public void dingweidaka(View v){
        Intent intent = new Intent(ShouYe.this, dingweidaka.class);
        startActivity(intent);
    }
    public void geren(View v){
        Intent intent = new Intent(ShouYe.this, personal.class);
        startActivity(intent);
    }
}
