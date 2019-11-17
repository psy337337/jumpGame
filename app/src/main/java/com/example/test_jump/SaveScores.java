package com.example.test_jump;

import io.realm.RealmObject;

public class SaveScores extends RealmObject {
    private String name;
    private int score;
    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }

}
