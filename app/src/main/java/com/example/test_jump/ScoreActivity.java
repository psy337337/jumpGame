package com.example.test_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        EditText nameEdit = findViewById(R.id.textViewedit);
        nameEdit.setText(nameEdit.getText());

        TextView tvscore = findViewById(R.id.textViewScore);
        Intent intent = getIntent();

        tvscore.setText("");



    }
}
