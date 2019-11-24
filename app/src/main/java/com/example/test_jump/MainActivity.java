package com.example.test_jump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    long pressedTime = 0;
    ConstraintLayout screen;
    Button bslide,bjump,bquit;
    ImageView ani;
    MoveCharacter moveCharacter;
    MoveBack moveBack;
    TextView score;
    ImageView back[] = new ImageView[2];
    ImageView[] redHP = new ImageView[3];
    ImageView[] whiteHP = new ImageView[3];
    HP hp;
    Score scoreClass;
    MadeHurdleHandler madeHurdleHandler;
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bslide=findViewById(R.id.bslide);
        bjump=findViewById(R.id.bjump);
        bquit=findViewById(R.id.bquit);
        ani=findViewById(R.id.imageView);
        screen=findViewById(R.id.screen);
        score = findViewById(R.id.scoreText);
        redHP[0] = findViewById(R.id.hp1);
        redHP[1] = findViewById(R.id.hp2);
        redHP[2] = findViewById(R.id.hp3);
        whiteHP[0] = findViewById(R.id.whp1);
        whiteHP[1] = findViewById(R.id.whp2);
        whiteHP[2] = findViewById(R.id.whp3);
        back[0] = findViewById(R.id.back1);
        back[1] = findViewById(R.id.back2);

        activity = MainActivity.this;

        //핸들러 객체 생성
        moveCharacter = new MoveCharacter(ani,bslide,bjump);
        hp = new HP(redHP,whiteHP,this);
        scoreClass = new Score(score);

//        //장애물 좌표 값 저장(장애물 수에 따라 추가할 것)
//        int[][] point = {{2500,550},{2500,430}};

        MoveCharacter.stopCharacter = false;
        MoveHurdle.stopHurdle = false;

        final MediaPlayer m = MediaPlayer.create(this, R.raw.bgm);
////        final boolean mplay = true;
        m.setLooping(true);
        m.start();

        //터치 이벤트
        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (view.getId()){
                    //점프 버튼 터치
                    case R.id.bjump :
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            bslide.setEnabled(false);
                            bjump.setEnabled(false);
                            MoveCharacter.jumpSoundPlay = true;
                            moveCharacter.sendMessageDelayed(moveCharacter.obtainMessage(1), 10);
                        }
                        break;
                        //슬라이드 버튼 터치
                    case R.id.bslide:
                        switch (motionEvent.getAction()){
                            case MotionEvent.ACTION_DOWN ://누르고 있을때
                                moveCharacter.setSliding(true);
                                moveCharacter.sendMessage(moveCharacter.obtainMessage(2));
                                bjump.setEnabled(false);
                                break;
                            case MotionEvent.ACTION_UP ://뗐을때
                                moveCharacter.setSliding(false);
                                moveCharacter.sendMessage(moveCharacter.obtainMessage(2));
                                bjump.setEnabled(true);
                                break;
                        }
                        break;
                }
                return false;

            }
        };
        bquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setItems(R.array.LAN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] items = getResources().getStringArray(R.array.LAN);
//                        Toast.makeText(getApplicationContext(),items[i],Toast.LENGTH_LONG).show();
                        if(items[i].equals("Continue")){
                            onRestart();
                        }else if(items[i].equals("Game Rules")){
                            Intent intent = new Intent(MainActivity.this,HowActivity.class);
                            intent.putExtra("ActivityNumber",2);

                            startActivity(intent);
                        }else if(items[i].equals("Quit")) {
//                            Intent intent = new Intent(MainActivity.this,StartActivity.class);
//                            startActivity(intent);
//                            MainActivity.super.onBackPressed();
                            Intent goStart = new Intent(MainActivity.this,StartActivity.class);
                            startActivity(goStart);
                            //        ActivityCompat.finishAffinity(this);
                            System.exit(0);//이거로 하면 깔끔하게 다 지울 수 있는데 문제는 끝내는데까지 시간이 조금 걸림
                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //리스너
        bjump.setOnTouchListener(listener);
        bslide.setOnTouchListener(listener);
        bquit.setOnTouchListener(listener);

        madeHurdleHandler = new MadeHurdleHandler(screen, this, moveCharacter, hp);
        madeHurdleHandler.sendMessageDelayed(new Handler().obtainMessage(1), 1000);
        moveBack = new MoveBack(back,getScreenSize(this).x);
        moveBack.sendMessageDelayed(new Handler().obtainMessage(1), 1);


    }

    public void dieIntent(){
        Intent dieintent = new Intent(this,ScoreActivity.class);
        dieintent.putExtra("scorein",Integer.parseInt(score.getText().toString()));
        startActivity(dieintent);
        System.exit(0);
    }
    @Override
    protected void onPause() {
        super.onPause();
        setBooleanHandlers(true);
        moveCharacter.stopAnim();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setBooleanHandlers(false);
        ArrayList<MoveHurdle> m = MadeHurdleHandler.movingHurdles;
        for(int i = 0; i < m.size();i++){
            m.get(i).sendMessageDelayed(m.get(i).obtainMessage(MadeHurdleHandler.hudlesNumber.get(i)-2), 1);
        }
        moveCharacter.compulsionJumpDel();
        madeHurdleHandler.sendMessageDelayed(madeHurdleHandler.obtainMessage(1), (2000*m.size()));
        scoreClass.sendMessageDelayed(scoreClass.obtainMessage(1), 100);
        moveCharacter.startAnim();
        moveBack.sendMessageDelayed(moveBack.obtainMessage(1),1);
    }
    public void setBooleanHandlers(boolean b){
        MoveCharacter.stopCharacter = b;
        MoveHurdle.stopHurdle = b;
        MadeHurdleHandler.stopMaking = b;
        MoveBack.stopBack = b;
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }
    @Override
    public void onBackPressed(){
        if ( pressedTime == 0 ) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
                super.onBackPressed();
                System.exit(0);
//                finish(); // app 종료 시키기
            }
        }
//        super.onBackPressed();



    }
}