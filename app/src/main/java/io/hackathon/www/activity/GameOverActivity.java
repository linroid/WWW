package io.hackathon.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.hackathon.www.R;
import io.hackathon.www.android.GameLauncher;

public class GameOverActivity extends AppCompatActivity {

    @InjectView(R.id.score)
    TextView scoreTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        ButterKnife.inject(this);
        int score = getIntent().getExtras().getInt("score");
        scoreTV.setText(getString(R.string.score, score));
    }
    @OnClick( R.id.btn_again)
    public void onSingleBtnClick(ImageButton btn){
        Intent intent = new Intent(this, GameLauncher.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_props)
    public void onPropsBtnClick(ImageButton btn) {
        Intent intent = new Intent(this, PropsActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_exit)
    public void onExitBtnClick(ImageButton btn) {
        System.exit(0);
    }
}
