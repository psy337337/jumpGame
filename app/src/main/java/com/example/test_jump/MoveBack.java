package com.example.test_jump;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class MoveBack extends Handler {
    private ImageView[] backs;
    private int maxX;
    private int forwardDegree = -10;
    public static boolean stopBack = false;

    public MoveBack(ImageView[] backs, int maxX){
        this.backs = backs;
        this.maxX = maxX;
        backs[0].setX(0);
        backs[0].setY(0);
        backs[1].setY(0);
        backs[1].setX(maxX);


    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopBack) {
            switch (msg.what){
                case 1:
//                    Log.d("asdf","배경움직이는 중");
                    forwardBack();
//                    Log.d("aaaa1",backs[0].getX()+" ");
//                    if(backs[0].getX()+backs[0].getWidth() <= 0)
//                        restart(0,2);
//                    else if(backs[1].getX()+backs[1].getWidth() <= 0)
//                        restart(1,0);
//                    else if(backs[2].getX()+backs[2].getWidth() <= 0)
//                        restart(2,1);
                    if(backs[0].getX()+backs[0].getWidth() <= 0)
                        restart(0);
                    else if(backs[1].getX()+backs[1].getWidth() <= 0)
                        restart(1);
                    this.sendMessageDelayed(this.obtainMessage(1), 1);
                    break;
            }
        }
    }
    public void forwardBack(){
        backs[0].setX(backs[0].getX()+forwardDegree);
        backs[1].setX(backs[1].getX()+forwardDegree);
    }
    public void restart(int i){
        backs[i].setX(maxX);
    }
}
