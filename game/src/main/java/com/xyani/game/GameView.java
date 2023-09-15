package com.xyani.game;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-01 14
 * Time:56
 */
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private  GamePlayScene gameplayScene;
    private GameEngine gameEngine;
    private SurfaceHolder surfaceHolder;

    public GameView(Context context) {
        this(context,null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        AdvancedThreadPoolUtil.initDefaultThreadPool();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameplayScene = new GamePlayScene(gameEngine, context);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameEngine=new GameEngine("GameEngine");
        gameEngine.setSurfaceHolder(surfaceHolder);
        gameEngine.setScene(gameplayScene);
        gameEngine.startGameLoop(); // 启动游戏循环
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Config.gameViewWidth=width;
        Config.gameViewHeight=height;
        gameplayScene.setSceneWithAndHeight(width,height);
        // 处理屏幕尺寸变化
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameEngine.stopGameLoop(); // 停止游戏循环
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理触摸事件
        gameEngine.handleTouch(event);
        return true;
    }

    public void setGameState(GameState gameState){
        if (gameplayScene!=null){
            gameplayScene.setGameState(gameState);
        }
    }

    public void setGameCallBack(GameCallBack gameCallBack){
        if (gameplayScene!=null){
            gameplayScene.setGameCallBack(gameCallBack);
        }
    }

    public void startGame(){
        if (gameplayScene!=null){
            gameplayScene.startGame();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        gameplayScene.onDestroy();
    }
}

