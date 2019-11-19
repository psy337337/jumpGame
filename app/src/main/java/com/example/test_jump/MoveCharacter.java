package com.example.test_jump;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

//캐릭터 움직이는 클래스
public class MoveCharacter extends Handler {
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
