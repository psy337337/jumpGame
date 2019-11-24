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
    Realm realm;
    EditText nameEdit;
    TextView tvscore;
    Intent intent,goStart;
    Button okButton;
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
        okButton = findViewById(R.id.button);
        intent = new Intent(this.getIntent());
        score = intent.getIntExtra("scorein",1);


        tvscore.setText("최종 점수 : "+score+"\n\n아쉽게도 랭킹에 들지 못하였습니다:(");
        setBooleanEditText(false);

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
            settingRanking();
        }
        for(SaveScores a : results){
            scoreList.add(new ListViewScores(a.getName(),a.getScore()));
            if(a.getScore() <= score) {
                settingRanking();
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
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            //오브젝트 생성
                            SaveScores saveScores = realm.createObject(SaveScores.class);

                            //입력한 값을 이 클래스에 저장
                            saveScores.setName(nameEdit.getText().toString());
                            saveScores.setScore(score);

                            tvscore.setText("최종 점수 : "+score+"\n\n감사합니다. 랭킹에 반영했습니다!XD");
                            nameEdit.setText("");
                            setBooleanEditText(false);

                            results = realm.where(SaveScores.class).findAll();
                            results = results.sort("score", Sort.DESCENDING);

                            if(results.size() > 15){
                                results.deleteLastFromRealm();
                            }

                        }
                    });
                    scoreList.clear();
                    for(SaveScores a : results){
//                        ListViewScores listViewScores =
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
        finishActivity();
    }
    public void settingRanking(){
        isRanking = true;
        setBooleanEditText(true);
        tvscore.setText("최종 점수 : "+score+"\n\n축하합니다! 랭킹에 들었습니다. 아래를 클릭하여 닉네임을 입력해주세요:)");
    }
    public void setBooleanEditText(boolean b) {
        nameEdit.setEnabled(b);
        nameEdit.setFocusable(b);
        nameEdit.setClickable(b);
        nameEdit.setFocusableInTouchMode(b);
        okButton.setEnabled(b);
    }
}
