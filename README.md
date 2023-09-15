# 黄金挖矿小游戏

###  这个项目最初的灵感来源于我在开发过程中需要一个小型游戏，但却发现几乎所有的Android游戏都采用了Cocos2d-x或Unity等引擎，很少有人选择使用Android原生代码进行开发。出于开源原则和分享精神，我决定将这个项目开源，供有兴趣的朋友自由下载和修改。

### 该项目的核心原理是基于Android的SurfaceView，并在此基础上利用Canvas进行图形绘制。在技术方面，我还运用了线程池来管理并发任务，以及使用了LruCache来高效地缓存图片资源。

### 所以，咚咚！希望这个项目能够为需要的人提供帮助，也欢迎大家一起来完善和分享这个开源项目。







#### 示例代码：

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <com.xyani.game.GameView
        android:id="@+id/game"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

```
     	 GameView mGameView = findViewById(R.id.game);

        mGameView.startGame();

        mGameView.setGameCallBack(new GameCallBack() {
         	/**
     		* 游戏时间
     		*/
            @Override
            public void onGameTimeCallBack(long gameTime) {

            }
			 /**
    		  * 关卡
    		  */
            @Override
            public void onLevel(String level) {

            }
  			/**
    		 * 积分
     		 */
            @Override
            public void onScore(String scoreCount) {

            }
			
    		/**
     		 * 目标积分
    		 */
            @Override
            public void onTargetonScore(String targetScoreCount) {

            }
           /**
            * 胜利
            */
            @Override
            public void onWin() {

            }
          /**
           * 失败
            */
            @Override
            public void onFail() {

            }
        });
```



#### 演示示例：

![](https://github.com/liouyang/GoldMining/blob/main/video/goldgif.gif)

#### 下面就写一些片段代码吧

##### GameEngine初始化

```
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

```



##### 背景绘制

```
 /**
     * 游戏场景的宽高
     * @param visibleRectWidth
     * @param visibleRectHeight
     */
    public void setViewWidthAndHeight(int visibleRectWidth, int visibleRectHeight) {
        this.visibleRectWidth = visibleRectWidth;
        this.visibleRectHeight = visibleRectHeight;
        skyMatrix.reset();
        float scaleHeight = Config.skyBoundary / ((float) skyHeight);
        float scaleWidth = ((float) visibleRectWidth) / skyWidth;
        skyMatrix.postScale(scaleWidth, scaleHeight);

        bgMatrix.reset();
        float max = Math.max(visibleRectHeight, Config.skyBoundary);
        float bgH = max / bgHeight;

        bgMatrix.postScale(((float) visibleRectWidth) / bgWidth, bgH);
        bgMatrix.postTranslate(0, Config.skyBoundary);

    }

    /**
     * 绘制背景
     * @param g
     */
    public void drawBitmap(Canvas g) {
        try {
            if (bgSky != null) {
                g.drawBitmap(bgSky, skyMatrix, null);
            }
            if (bgBig != null) {
                g.drawBitmap(bgBig, bgMatrix, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
```

##### 绘制区域内的金矿

```
/**
 * 创建游戏的矿石元素
 *
 * @param context
 */
private void createElement(Context context) {
    try {
        objectList.clear();
        boolean isPlace = true;
        for (int i = 0; i < mGoldNum; i++) {
            double random = Math.random();
            Gold gold; // 存放当前生成的金块
            	//小矿石概率
            if (random <Config.mGoldMiniProbability) {
                gold = new GoldMini(context);
               //中级概率
            } else if (random < Config.mGoldProbability) {
                gold = new Gold(context);
            } else {
                gold = new GoldPlus(context);
            }
            for (Object obj : objectList) {
            //金矿是否相交
                if (gold.getRec().intersect(obj.getRec())) {
                    isPlace = false;
                }
            }
            if (isPlace) {

                objectList.add(gold);
            } else {
                isPlace = true;
                i--;
            }
        }
		//黑石头块
        for (int i = 0; i < mRockNum; i++) {
            Rock rock = new Rock(context);
            for (Object obj : objectList) {
                if (rock.getRec().intersect(obj.getRec())) {
                    isPlace = false;
                }
            }
            if (isPlace) {
                objectList.add(rock);
            } else {
                isPlace = true;
                i--;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

}

    void draw(Canvas g) {
        if (img!=null){
            Drawable gifDrawable =img;
            if (gifDrawable!=null){
                gifDrawable.setBounds(getRec());
                gifDrawable.draw(g);
            }
        }
    }
```

##### 绘制爪和线核心代码：

```
/**
 * 判断是否抓取成功
 */
void logic() {
    for (Object obj : this.frame.objectList) {
        if (endx > obj.x && endx < obj.x + obj.width && endy > obj.y && endy < obj.y + obj.height) {
            state = 3;
            obj.flag = true;
        }
    }

}

/**
 * 绘制方法 n 摇摆系数0-1
 *
 * @param g
 */
void lines(Canvas g) {
    try {
        endx = (int) (x + length * Math.cos(n * Math.PI));
        endy = (int) (y + length * Math.sin(n * Math.PI));
        g.drawLine(x, y, endx, endy, paint);
        drawImage(g);
    } catch (Exception e) {
        e.printStackTrace();
    }

}

/**
 * 绘制爪
 *
 * @param g
 */
private void drawImage(Canvas g) {
    try {
        if (hook != null) {
            matrix.reset();
            //结束点相对起始点的角度偏移
            currentAngle = calculateAngle(x, y, endx, endy);
            //缩放图形爪
            matrix.postScale(0.3f, 0.3f);
            //映射缩放后的大小
            matrix.mapRect(scaleRec, originalRect);

            matrix.postRotate((float) currentAngle - 90, scaleRec.width() / 2, scaleRec.height() / 2);

            matrix.postTranslate(endx - scaleRec.width() / 2, endy - scaleRec.height() / 2);

            // 设置图片绘制位置, 保证图片中心处于曲线上

            g.drawBitmap(hook, matrix, paint);

        }
    } catch (Exception e) {
        e.printStackTrace();
    }

}


/**
 * 计算点点位偏移角度
 *
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return
 */
public double calculateAngle(double x1, double y1, double x2, double y2) {
    // 计算两点之间的水平和垂直距离
    double dx = x2 - x1;
    double dy = y2 - y1;

    // 使用反正切函数计算夹角（弧度）
    double radians = Math.atan2(dy, dx);

    // 将弧度转换为度数
    double degrees = Math.toDegrees(radians);
    // 保证角度在 0 到 360 度之间
    if (degrees < 0) {
        degrees += 360;
    }

    return degrees;
}
```





#### 有帮助到您，可以给个打赏哦~~，谢谢哦
<img src="https://github.com/liouyang/GoldMining/blob/main/video/code.jpg" width="588">
