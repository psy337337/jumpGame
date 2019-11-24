package com.example.test_jump;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Score extends Handler {
    TextView score;
    public static int spendTime = 0;

    public Score(TextView score){
        this.score = score;
        this.sendMessageDelayed(this.obtainMessage(1), 1);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!MoveHurdle.stopHurdle&&!MoveCharacter.stopCharacter){
            switch (msg.what){
                case 1:
                    plusScore();
                    speedUp();
                    if(HP.isDie == false)
                        this.sendMessageDelayed(this.obtainMessage(1), 100);
                    break;

            }
        }

    }

    private void invincibility(){
        if(spendTime != 0)
            spendTime--;
    }
    private void plusScore(){
        score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + 1));
        invincibility();
    }
    private void speedUp(){
        if(Integer.parseInt(score.getText().toString())/50 > 1){
            MoveHurdle.forwardDegree = -10-Integer.parseInt(score.getText().toString())/50;
            MoveBack.forwardDegree = -10-Integer.parseInt(score.getText().toString())/50;
        }
    }
}
