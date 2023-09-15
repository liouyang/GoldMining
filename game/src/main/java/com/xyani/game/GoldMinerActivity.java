package com.xyani.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-01 14
 * Time:47
 */
public class GoldMinerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_gold_miner);
        GameView mGameView = findViewById(R.id.game);

        mGameView.startGame();

        mGameView.setGameCallBack(new GameCallBack() {
            @Override
            public void onGameTimeCallBack(long gameTime) {
                Log.d("GameCallBack","=====onGameTimeCallBack====="+gameTime);
            }

            @Override
            public void onLevel(String level) {
                Log.d("GameCallBack","=======onLevel==="+level);
            }

            @Override
            public void onScore(String scoreCount) {
                Log.d("GameCallBack","=======scoreCount==="+scoreCount);
            }

            @Override
            public void onTargetonScore(String targetScoreCount) {
                Log.d("GameCallBack","=======targetScoreCount==="+targetScoreCount);
            }

            @Override
            public void onWin() {
                Log.d("GameCallBack","=======onWin===");
            }

            @Override
            public void onFail() {
                Log.d("GameCallBack","=======onFail===");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
