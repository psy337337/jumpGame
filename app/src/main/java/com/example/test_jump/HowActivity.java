package com.example.test_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HowActivity extends AppCompatActivity {
    Intent intent, goMain, goStart ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_how);

        intent = new Intent(this.getIntent());
        goMain = new Intent(HowActivity.this,MainActivity.class);
        goStart = new Intent(HowActivity.this,StartActivity.class);

    }
    public void onBackPressed(){
//        super.onBackPressed();
        finishActivity();
    }

    public void quitClick(View view) {
        finishActivity();
    }
    public void finishActivity(){
        if(intent.getIntExtra("ActivityNumber",1) == 1)
            goMain.putExtra("ActivityNumber",2);
        else if(intent.getIntExtra("ActivityNumber",1) == 0){
//            MainActivity.activity.finish();
            startActivity(goStart);
        }
        finish();
    }
}
