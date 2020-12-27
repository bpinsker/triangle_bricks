package com.bp.trianglebricks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView TriangeleIvBTN;
    private Button mPlayBtn;
    private Button mOptionBtn;
    private Button mHighScoresBtn;
    private Button mNewGameBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PlayBTN
        mPlayBtn = (Button) findViewById(R.id.play);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        //NEWGameBTN
        mNewGameBtn = (Button) findViewById(R.id.resetgame);
        mNewGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.LogClearFile(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        // mHighScoresBtn
        mHighScoresBtn = (Button) findViewById(R.id.highscores);
        mHighScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScore.class);
                startActivity(intent);
            }
        });
        // OptionsBtn
        mOptionBtn = (Button) findViewById(R.id.options);
        mOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Options.class);
                startActivity(intent);
                Log.d("TAG", "mOptionBtn ");
            }
        });
        //TriangeleBTN
        TriangeleIvBTN = (ImageView) findViewById(R.id.triangleIv);
        TriangeleIvBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.Msg(getApplicationContext(), "triangle btn click");
            }
        });
        Log.d("TAG", "onCreate done");

    }
}