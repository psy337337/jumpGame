package com.example.test_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmMigrationNeededException;

public class ScoreActivity extends AppCompatActivity {
    long pressedTime = 0;
    Realm realm;
    EditText nameEdit;
    TextView tvscore;
    Intent intent,goStart;
    int score;
    ListView listView;
    Boolean isRanking = false;
    RealmResults<SaveScores> results;
    ArrayList<ListViewScores> scoreList;
    ListAdapter listAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score);
        listView = findViewById(R.id.list);
        nameEdit = findViewById(R.id.textViewedit);
        tvscore = findViewById(R.id.textViewScore);
        Button okButton = findViewById(R.id.button);
        intent = new Intent(this.getIntent());
        score = intent.getIntExtra("scorein",1);



        tvscore.setText(Integer.toString(score));

        scoreList = new ArrayList<>();

        //Realm 초기화
        Realm.init(this);

        // myrealm.realm 파일
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);

        // 이 스레드의 Realm 인스턴스 얻음
        //Realm 객체 생성
        try {
            realm = Realm.getInstance(config);
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(config);
            realm = Realm.getInstance(config);
        }
        results = realm.where(SaveScores.class).findAll();
        results = results.sort("score", Sort.DESCENDING);

        if(results.isEmpty()||results.size() < 15) {
            isRanking = true;
        }
        for(SaveScores a : results){
            scoreList.add(new ListViewScores(a.getName(),a.getScore()));
            if(a.getScore() <= score) {
                isRanking = true;
            }
        }
        listAdapter = new ListAdapter(scoreList);
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //데이터 모두 삭제
//                results.deleteAllFromRealm();
//            }
//        });
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.button&&isRanking){
                    Log.d("asdf","버튼 작동");
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            //오브젝트 생성
                            SaveScores saveScores = realm.createObject(SaveScores.class);

                            //입력한 값을 이 클래스에 저장
                            saveScores.setName(nameEdit.getText().toString());
                            saveScores.setScore(score);

                            results = realm.where(SaveScores.class).findAll();
                            results = results.sort("score", Sort.DESCENDING);

                            if(results.size() > 15){
                                results.deleteLastFromRealm();
                            }

                        }
                    });
                    scoreList.clear();
                    for(SaveScores a : results){
//                        ListViewScores listViewScores = ;
                        scoreList.add(new ListViewScores(a.getName(),a.getScore()));
//                        setTxt(a.getName()+"\n");
                    }
                    listAdapter.notifyDataSetChanged();
                    isRanking = false;
                }
                else if(view.getId() == R.id.quitButton){
                    finishActivity();
                }
            }
        };
        okButton.setOnClickListener(listener);
        listView.setAdapter(listAdapter);

    }
    public void finishActivity(){
        goStart = new Intent(ScoreActivity.this,StartActivity.class);
        startActivity(goStart);
        finish();
    }
    public void quitClick(View view) {
        finishActivity();
    }
    public void onBackPressed(){
//        if ( pressedTime == 0 ) {
//            Toast.makeText(ScoreActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
//            pressedTime = System.currentTimeMillis();
//        }
//        else {
//            int seconds = (int) (System.currentTimeMillis() - pressedTime);
//
//            if ( seconds > 2000 ) {
//                Toast.makeText(ScoreActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
//                pressedTime = 0 ;
//            }
//            else {
//                super.onBackPressed();
////                finish(); // app 종료 시키기
//            }
//        }
        finishActivity();
    }
}
