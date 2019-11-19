package com.example.test_jump;

import android.view.View;
import android.widget.ImageView;

public class HP{
    ImageView[][] hp = new ImageView[3][2];
    private MainActivity main;
    public static boolean isDie = false;
    public HP(ImageView[] rhp, ImageView[] whp,MainActivity main){
        this.main = main;
        for(int i = 0; i < hp.length; i++){
            hp[i][0] = rhp[i];
            hp[i][1] = whp[i];

        }

    }
    public void hitHurdle() {
        if (Score.spendTime == 0){
            for (int i = 0; i < hp.length; i++) {
                if (hp[1][0].getVisibility() == View.INVISIBLE) {
                    hp[2][0].setVisibility(View.INVISIBLE);
                    hp[2][1].setVisibility(View.VISIBLE);
                    stopALL();
                } else if (hp[i][0].getVisibility() == View.VISIBLE) {
                    hp[i][0].setVisibility(View.INVISIBLE);
                    hp[i][1].setVisibility(View.VISIBLE);
                    Score.spendTime = 10;
                    break;
                }
            }
        }
    }
    public void fall(){
        for(int i = 0; i < hp.length; i++){
            hp[i][0].setVisibility(View.INVISIBLE);
            hp[i][1].setVisibility(View.VISIBLE);
        }
        stopALL();
    }
    public void stopALL(){
        isDie = true;
        MoveCharacter.stopCharacter = true;
        MoveHurdle.stopHurdle = true;

        main.dieIntent();

    }
}
