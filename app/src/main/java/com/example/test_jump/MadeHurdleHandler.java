package com.example.test_jump;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class MadeHurdleHandler extends Handler {
    private ConstraintLayout constraintLayout;
    private MainActivity mainActivity;
    private MoveCharacter moveCharacter;
    private HP hp;
    public static boolean stopMaking = false;
    private Random rand = new Random();

    public MadeHurdleHandler(ConstraintLayout constraintLayout, MainActivity mainActivity, MoveCharacter moveCharacter, HP hp){
        this.constraintLayout = constraintLayout;
        this.mainActivity = mainActivity;
        this.moveCharacter = moveCharacter;
        this.hp = hp;
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopMaking) {
            switch (msg.what) {
                case 1:
                    new MoveHurdle(constraintLayout, mainActivity, moveCharacter, hp).sendMessageDelayed(new android.os.Handler().obtainMessage(rand.nextInt(2) + 3), 1);
                    this.sendMessageDelayed(this.obtainMessage(1), (2000 + rand.nextInt(1000)));//난이도 조절할때 이부분 수정
                    break;
            }
        }
    }
}
