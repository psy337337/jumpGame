package com.example.test_jump;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

//캐릭터 움직이는 클래스
public class MoveCharacter extends Handler {
    int soundid;
    SoundPool s;

    private ImageView character;
    private Button bslide,bjump;
    private int jumpDegree = -7;
    private boolean isSliding = false;
    public static boolean stopCharacter = false;
    public static boolean jumpSoundPlay = false;
    private AnimationDrawable drawable;
    //생성자
    public MoveCharacter(ImageView character, Button bslide, Button bjump){
        this.character = character;
        this.bslide = bslide;
        this.bjump = bjump;
        drawable= (AnimationDrawable)character.getDrawable();
        drawable.start();

    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if(!stopCharacter) {
            switch (msg.what) {
                case 1://case1일때 점프 실행
                    stopAnim();
                    //점프 메소드 불러옴
                    jumpCharacter();
                    if (character.getY() < 633)
                        this.sendMessageDelayed(this.obtainMessage(1), 1);
                    else {
                        jumpDegree = -jumpDegree;
                        bslide.setEnabled(true);
                        bjump.setEnabled(true);
                        startAnim();
                    }
                    break;
                case 2:// 슬라이드일 때
                    stopAnim();
                    slideCharacter();
                    break;
            }
        }
    }
    //점프 메소드
    private void jumpCharacter(){
//        Log.d("asdf y좌표",character.getY()+"");//y좌표 633
//        Log.d("asdf 높이",character.getHeight()+"");//높이 263
        character.setY(character.getY()+jumpDegree);
        if(character.getY()<402)
            jumpDegree = -jumpDegree;
    }
    //슬라이드 메소드(이미지 변경)
    private  void slideCharacter(){
        if(isSliding)
            character.setImageResource(R.drawable.slidedog);
        else
            startAnim();
    }
    //슬라이드 여부 세팅하는 메소드
    public void setSliding(boolean sliding) {
        isSliding = sliding;
    }
    public boolean getSliding() {
        return isSliding;
    }
    //이미지 리턴해주는 메소드
    public ImageView getCharacter() {
        return character;
    }
    public void stopAnim(){
        if(jumpSoundPlay){
            jumpSound();
            jumpSoundPlay = false;
        }
        if(drawable.isRunning()) {
            drawable.stop();
            character.setImageResource(R.drawable.four);
        }
    }
    public void startAnim(){
        character.setImageResource(R.drawable.character_move);
        drawable= (AnimationDrawable)character.getDrawable();
        drawable.start();
    }
    public void compulsionJumpDel(){
        character.setY(633);
        bslide.setEnabled(true);
        bjump.setEnabled(true);
        jumpDegree = -7;
    }
    public void jumpSound(){
        s = new SoundPool(2, AudioManager.STREAM_ALARM,0);
        soundid = s.load(MainActivity.activity,R.raw.jumpjump,1);
//        s.play(soundid, 1.0F,1.0F, 0,  0,  1.0F);
//        int streamid =
        s.setOnLoadCompleteListener (new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
                s.play(soundId, 0.2f, 0.2f, 0, 0, 1f);
            }
        });
    }
}
