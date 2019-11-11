package com.example.test_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        Button conti,quits,howto;

        conti = findViewById(R.id.button1);
        quits = findViewById(R.id.button2);
        howto = findViewById(R.id.button3);

        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PauseActivity.super.onBackPressed();
            }
        });

        quits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PauseActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });

        howto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PauseActivity.this,HowActivity.class);
                startActivity(intent);
            }
        });
    }
}
