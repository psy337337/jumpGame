package com.example.test_jump;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

//장애물 클래스
public class MoveHurdle extends Handler {
    private ConstraintLayout constraintLayout;
    private MainActivity mainActivity;
    private MoveCharacter moveCharacter;
    private HP hp;

    private ImageView hurdle;
    private ImageView floor;

    public static int forwardDegree = -10;
    public static boolean stopHurdle = false;
    private int[][] point = {{2500,730},{2500,600},{2500,860}};
    private Random rand = new Random();

    //생성자
    public MoveHurdle(ConstraintLayout constraintLayout, MainActivity mainActivity, MoveCharacter moveCharacter,HP hp){
        this.constraintLayout = constraintLayout;
        this.mainActivity = mainActivity;
        this.moveCharacter = moveCharacter;
        this.hp = hp;

    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopHurdle) {
            switch (msg.what) {
                case 1://앞으로 가는 장애물 처리
                    forwardHurdle(hurdle);
                    hit();
                    if (hurdle.getX() < -hurdle.getWidth()) {
                        constraintLayout.removeView(hurdle);
                        MadeHurdleHandler.movingHurdles.remove(0);
                        MadeHurdleHandler.hudlesNumber.remove(0);
                    }
                    else
                        this.sendMessageDelayed(this.obtainMessage(1), 1);
                    break;
                case 2:
                    forwardHurdle(floor);
                    fall();
                    if (floor.getX() < -floor.getWidth()) {
                        constraintLayout.removeView(floor);
                        MadeHurdleHandler.movingHurdles.remove(0);
                        MadeHurdleHandler.hudlesNumber.remove(0);
                    }
                    else
                        this.sendMessageDelayed(this.obtainMessage(2), 1);
                    break;
                case 3:
                    int kindOfHurdle = rand.nextInt(2);
                    makeHurdle(point[kindOfHurdle][0],point[kindOfHurdle][1]);
                    break;
                case 4:
                    makeFloor(point[msg.what-2][0],point[msg.what-2][1]);

            }
        }
    }
    //앞으로 가는 메소드
    private void forwardHurdle(ImageView image){
        image.setX(image.getX()+forwardDegree);
    }
    //장애물 만드는 메소드
    private void makeHurdle(int x, int y){
        hurdle = new ImageView(mainActivity);
        if(y == point[1][1])
            hurdle.setImageResource(R.drawable.slidehurdle);
        else
            hurdle.setImageResource(R.drawable.hurdle);
        hurdle.setX(x);
        hurdle.setY(y);

        constraintLayout.addView(hurdle);
        this.sendMessageDelayed(new Handler().obtainMessage(1),1);
    }
    //낙하처리 바닥 만드는 메소드
    private void makeFloor(int x, int y){
        floor = new ImageView(mainActivity);
        floor.setImageResource(R.drawable.fall);
        floor.setX(x);
//        Log.d("asdf","y좌표 : "+moveCharacter.getCharacter().getY()+moveCharacter.getCharacter().getHeight());
        floor.setY(y);//수정 필요

        constraintLayout.addView(floor);
        this.sendMessageDelayed(new Handler().obtainMessage(2),1);
    }

    //충돌처리 메소드
    private void hit(){
        if(hurdle.getX() < moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth() -100 &&
                hurdle.getX() + hurdle.getWidth() - 100 > moveCharacter.getCharacter().getX()&&
                hurdle.getY() < moveCharacter.getCharacter().getY() + moveCharacter.getCharacter().getHeight()-50&&
                hurdle.getY() + hurdle.getHeight() - 50 > moveCharacter.getCharacter().getY()&&!moveCharacter.getSliding()){
            Log.d("asdf","점프 충돌");
            hp.hitHurdle();
        }if(hurdle.getX() < moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth() -100 &&
                hurdle.getX() + hurdle.getWidth() - 100 > moveCharacter.getCharacter().getX()&&
                hurdle.getY() < moveCharacter.getCharacter().getY() + moveCharacter.getCharacter().getHeight()-50&&
                hurdle.getY() + hurdle.getHeight() - 50 > moveCharacter.getCharacter().getY()+(moveCharacter.getCharacter().getHeight()/2)&&moveCharacter.getSliding()){
            Log.d("asdf","슬라이드 충돌");
            hp.hitHurdle();
        }

    }
    //낙하처리 메소드
    private void fall(){
        if(floor.getX() < moveCharacter.getCharacter().getX() + 50 &&
                floor.getX() + floor.getWidth() > moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth() -50 &&
                moveCharacter.getCharacter().getY() + moveCharacter.getCharacter().getHeight() +40 > floor.getY()){
            hp.fall();
            Log.d("asdf","낙하");
        }
    }
    public MoveHurdle getMovingHurdle(){
        return this;
    }
}