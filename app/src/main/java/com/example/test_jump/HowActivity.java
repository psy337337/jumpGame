package com.example.test_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how);
    }
    public void onBackPress(){
        super.onBackPressed();
    }
}
