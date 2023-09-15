package com.xyani.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-01 15
 * Time:16
 */
public abstract class GameScene {
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    protected GameEngine gameEngine;

    public GameScene(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public abstract void update();


    public abstract void draw(Canvas canvas);

    public abstract void handleTouch(MotionEvent event);
}
