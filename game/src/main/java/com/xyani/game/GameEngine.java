package com.xyani.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-01 14
 * Time:55
 */
public class GameEngine extends Thread {
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private GameScene currentScene;

    /**
     * 每30s 刷新屏幕
     */
    public static final int TIME_IN_FRAME = 30;
    private Canvas canvas;

    public GameEngine(String threadName) {
        super();
        setName(threadName);
    }

    public void startGameLoop() {
        isRunning = true;
        start();
    }

    public void stopGameLoop() {
        isRunning = false;
        try {
            join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setScene(GameScene scene) {
        currentScene = scene;
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        this.surfaceHolder = holder;
    }

    public void handleTouch(MotionEvent event) {
        if (currentScene != null) {
            currentScene.handleTouch(event);
        }
    }



    @Override
    public void run() {
        while (isRunning) {
           synchronized (this){
               long startTime = System.currentTimeMillis();
               /**在这里加上线程安全锁**/
               try {
                   if (surfaceHolder.getSurface().isValid()) {
                       canvas = surfaceHolder.lockCanvas();
                       if (currentScene != null&&canvas!=null) {
                           currentScene.update();
                           currentScene.draw(canvas);
                       }
                   }
               } catch (Exception e) {
                   Log.d("game", "游戏画布异常");
                   e.printStackTrace();
               } finally {
                   if (canvas!=null){
                       surfaceHolder.unlockCanvasAndPost(canvas);
                   }
               }


               /**取得更新游戏结束的时间**/

               long endTime = System.currentTimeMillis();

               /**计算出游戏一次更新的毫秒数**/

               int diffTime = (int) (endTime - startTime);

               /**确保每次更新时间为30帧**/

               while (diffTime <= TIME_IN_FRAME) {

                   diffTime = (int) (System.currentTimeMillis() - startTime);

                   /**线程等待**/

                   Thread.yield();

               }
           }

        }
    }

}


