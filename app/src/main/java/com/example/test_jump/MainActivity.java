package com.example.test_jump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout screen;
    Button bslide,bjump,bquit;
    ImageView ani;
    ProgressBar HpProgressBar;
    MoveCharacter moveCharacter;
    MoveHP hp;
    TextView score;
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
        HpProgressBar = findViewById(R.id.HPprogressBar);
        score = findViewById(R.id.scoreText);

        //핸들러 객체 생성
        moveCharacter = new MoveCharacter(ani,bslide,bjump);
        hp = new MoveHP(HpProgressBar,score);

        //장애물 좌표 값 저장(장애물 수에 따라 추가할 것)
        int[][] point = {{2500,550},{2500,430}};

        MoveCharacter.stopCharacter = false;
        MoveHurdle.stopHurdle = false;
        MoveFloor.stopFloor = false;

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
                    case R.id.bquit:
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            MoveCharacter.stopCharacter = true;
                            MoveHurdle.stopHurdle = true;
                            MoveFloor.stopFloor = true;
                        }
                        break;
                }
                return false;

            }
        };
        //리스너
        bjump.setOnTouchListener(listener);
        bslide.setOnTouchListener(listener);
        bquit.setOnTouchListener(listener);

        new MoveHurdle(screen,this,moveCharacter,hp,point[1]).sendMessageDelayed(new Handler().obtainMessage(1),2000);
        new MoveHurdle(screen,this,moveCharacter,hp,point[0]).sendMessageDelayed(new Handler().obtainMessage(1),5000);
        new MoveHurdle(screen,this,moveCharacter,hp,point[1]).sendMessageDelayed(new Handler().obtainMessage(1),8000);
        new MoveFloor(screen,this,moveCharacter,hp).sendMessage(new Handler().obtainMessage(1));
        new MoveFloor(screen,this,moveCharacter,hp).sendMessageDelayed(new Handler().obtainMessage(1),7000);
    }
}
//캐릭터 움직이는 클래스
class MoveCharacter extends Handler{
    private ImageView character;
    private Button bslide,bjump;
    private int jumpDegree = -7;
    private boolean isSliding = false;
    public static boolean stopCharacter = false;
    //생성자
    public MoveCharacter(ImageView character, Button bslide, Button bjump){
        this.character = character;
        this.bslide = bslide;
        this.bjump = bjump;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopCharacter) {
            switch (msg.what) {
                case 1://case1일때 점프 실행
                    //점프 메소드 불러옴
                    jumpCharacter();
                    if (character.getY() < 437)
                        this.sendMessageDelayed(this.obtainMessage(1), 1);
                    else {
                        jumpDegree = -jumpDegree;
                        bslide.setEnabled(true);
                        bjump.setEnabled(true);
                    }
                    break;
                case 2:// 슬라이드일 때
                    slideCharacter();
                    break;
            }
        }
    }
    //점프 메소드
    private void jumpCharacter(){
        character.setY(character.getY()+jumpDegree);
        if(character.getY()<220)
            jumpDegree = -jumpDegree;
    }
    //슬라이드 메소드(이미지 변경)
    private  void slideCharacter(){
        if(isSliding) {
            //크기 변경(기존 높이의 절반)
            character.getLayoutParams().height = character.getLayoutParams().height/2;
            character.requestLayout();
            character.setImageResource(R.drawable.slide);
        }
        else {
            //크기 변경(원상복구)
            character.getLayoutParams().height = character.getLayoutParams().height*2;
            character.requestLayout();
            character.setImageResource(R.drawable.ex);
        }
    }
    //슬라이드 여부 세팅하는 메소드
    public void setSliding(boolean sliding) {
        isSliding = sliding;
    }
    //이미지 리턴해주는 메소드
    public ImageView getCharacter() {
        return character;
    }
}
//장애물 클래스
class MoveHurdle extends Handler{
    private ConstraintLayout constraintLayout;
    private MainActivity mainActivity;
    private ImageView hurdle;
    private MoveCharacter moveCharacter;
    private int forwardDegree = -10;
    public static boolean stopHurdle = false;
    private MoveHP moveHP;
    //생성자
    public MoveHurdle(ConstraintLayout constraintLayout, MainActivity mainActivity, MoveCharacter moveCharacter,MoveHP moveHP, int[] point){
        this.constraintLayout = constraintLayout;
        this.mainActivity = mainActivity;
        this.moveCharacter = moveCharacter;
        this.moveHP = moveHP;

        makeHurdle(point[0],point[1]);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopHurdle) {
            switch (msg.what) {
                case 1://앞으로 가는 장애물 처리
                    forwardHurdle();
                    hit();
                    if (hurdle.getX() < -hurdle.getWidth())
                        constraintLayout.removeView(hurdle);
                    else
                        this.sendMessageDelayed(this.obtainMessage(1), 1);
                    break;

            }
        }
    }
    //장애물 만드는 메소드
    private void makeHurdle(int x, int y){
        hurdle = new ImageView(mainActivity);
        hurdle.setImageResource(R.drawable.exb);
        hurdle.setX(x);
        hurdle.setY(y);

        constraintLayout.addView(hurdle);
    }
    //앞으로 가는 메소드
    private void forwardHurdle(){
        hurdle.setX(hurdle.getX()+forwardDegree);
    }
    //충돌처리 메소드
    private void hit(){
        if(hurdle.getX() < moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth() -100 &&
                hurdle.getX() + hurdle.getWidth() - 100 > moveCharacter.getCharacter().getX()&&
                hurdle.getY() < moveCharacter.getCharacter().getY() + moveCharacter.getCharacter().getHeight() -100 &&
                hurdle.getY() + hurdle.getHeight() - 100 > moveCharacter.getCharacter().getY()){
            moveHP.hitHurdle(1000);
            Log.d("asdf","충돌");
        }
//            Log.d("asdf","충돌 아님!");

    }
}
//장애물 클래스랑 합치거나 그걸 상속받는게 더 나을 수도 있겠다,, 나중에 수정할 것!
class MoveFloor extends Handler{
    private ConstraintLayout constraintLayout;
    private MainActivity mainActivity;
    private MoveCharacter moveCharacter;
    private ImageView floor;
    private MoveHP moveHP;
    private int forwardDegree = -10;
    public static boolean stopFloor = false;

    public MoveFloor(ConstraintLayout constraintLayout, MainActivity mainActivity, MoveCharacter moveCharacter, MoveHP moveHP){
        this.constraintLayout = constraintLayout;
        this.mainActivity = mainActivity;
        this.moveCharacter = moveCharacter;
        this.moveHP = moveHP;

        makeFloor();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopFloor) {
            switch (msg.what) {
                case 1:
                    forwardHurdle();
                    fall();
                    if (floor.getX() < -floor.getWidth())
                        constraintLayout.removeView(floor);
                    else
                        this.sendMessageDelayed(this.obtainMessage(1), 1);
                    break;

            }
        }
    }
    //변경 필요
    private void makeFloor(){
        floor = new ImageView(mainActivity);
        floor.setImageResource(R.drawable.fall);
        floor.setX(2500);
//        Log.d("asdf","y좌표 : "+moveCharacter.getCharacter().getY()+moveCharacter.getCharacter().getHeight());
        floor.setY(690);//수정 필요 베이직은 이 좌표로, 점프 해야할때는 다른 좌표로

        constraintLayout.addView(floor);
        //이부분도 변수로 변경 필요
//        floor.getLayoutParams().height = 200;
//        floor.getLayoutParams().width = 5000;
//        floor.requestLayout();
    }
    private void forwardHurdle(){
        floor.setX(floor.getX()+forwardDegree);
    }
    private void fall(){
        Log.d("asdf","floor size : "+floor.getX() + floor.getWidth());
        Log.d("asdf","캐릭터 X : "+(moveCharacter.getCharacter().getX()));
        Log.d("asdf","캐릭터 크기 : "+(moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth()));
        if(floor.getX() < moveCharacter.getCharacter().getX() + 50 &&
                floor.getX() + floor.getWidth() > moveCharacter.getCharacter().getX() + moveCharacter.getCharacter().getWidth() -50 &&
                moveCharacter.getCharacter().getY() + moveCharacter.getCharacter().getHeight() +40 > floor.getY()){
            moveHP.setHpProgress(0);
            MoveFloor.stopFloor = true;
            Log.d("asdf","낙하");
        }
//            Log.d("asdf","낙하 아님!");
    }
}
class MoveHP extends Handler{
    ProgressBar hpProgress;
    TextView score;
    int spendTime = 0;

    public MoveHP(ProgressBar hpProgress, TextView score){
        this.hpProgress = hpProgress;
        this.score = score;
        hpProgress.setProgress(hpProgress.getMax());
        this.sendMessageDelayed(this.obtainMessage(1), 1);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!MoveHurdle.stopHurdle&&!MoveCharacter.stopCharacter){
            switch (msg.what){
                case 1:
                    minusHP();
                    if(hpProgress.getProgress() != 0)
                        this.sendMessageDelayed(this.obtainMessage(1), 10);
                    else
                        zeroProgress();
                    break;

            }
        }

    }
    private void minusHP(){
        hpProgress.setProgress(hpProgress.getProgress()-1);
        if(hpProgress.getProgress()%10 == 0)
            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + 1));
        if(spendTime != 0)
            spendTime--;
    }
    public void hitHurdle(int hp){
        if(spendTime == 0) {
            if (hpProgress.getProgress() - hp > 0) {
                hpProgress.setProgress(hpProgress.getProgress() - hp);
                spendTime = 100;
            } else {
                hpProgress.setProgress(0);
                zeroProgress();
            }
        }
    }
    private void zeroProgress(){
        MoveCharacter.stopCharacter = true;
        MoveHurdle.stopHurdle = true;
    }
    public void setHpProgress(int x){
        hpProgress.setProgress(x);
    }
}