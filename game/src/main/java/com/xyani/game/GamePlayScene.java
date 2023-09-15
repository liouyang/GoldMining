package com.xyani.game;

import static com.xyani.game.Config.mNumberStages;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import pl.droidsonroids.gif.GifDrawable;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-01 15
 * Time:18
 */
public class GamePlayScene extends GameScene {


    private Bg bg;
    private int width;
    private int height;

    protected GameState state=GameState.un_started;


    /**
     * 存储金块、石块
     */
    List<Object> objectList = new CopyOnWriteArrayList<>();
    /**
     * 金矿的数量
     */
    private int mGoldNum = 20;

    /**
     * 石头的数量
     */
    private int mRockNum = 4;
    private Line line;

    private GestureDetectorCompat gestureDetectorCompat;

     GameCallBack gameCallBack;


     private boolean flag=true;

     private Thread thread=null;


    private GestureDetector.OnGestureListener onGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            setEventAdapter(e);
            return true;
        }
    };
    private Handler handler;
    private Context context;


    public GamePlayScene(GameEngine gameEngine, Context context) {
        super(gameEngine);
        this.context = context;
        initScene(context);
    }

    private void initScene(Context context) {
        bg = new Bg(context,GamePlayScene.this);
        handler = new Handler(Looper.myLooper());

        gestureDetectorCompat = new GestureDetectorCompat(context, onGestureListener);
        line = new Line(context, this);

        createMineral(context);

        AdvancedThreadPoolUtil.executeTask(() -> {
            while (flag) {
                nextLevel();
            }
        });

    }

    private void createMineral(Context context) {

        AdvancedThreadPoolUtil.executeTask(() -> {
            // 是否可以放置
            synchronized (GamePlayScene.this){
                createElement(context);
            }
        });
    }

    /**
     * 设置游戏为开始状态
     */
    public void startGame(){
        state =GameState.running;
        bg.startTime = System.currentTimeMillis();
    }

    /**
     * 创建游戏的元素
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
                if (random <Config.mGoldMiniProbability) {
                    gold = new GoldMini(context);
                } else if (random < Config.mGoldProbability) {
                    gold = new Gold(context);
                } else {
                    gold = new GoldPlus(context);
                }
                for (Object obj : objectList) {
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

    /**
     * 更新画布 背景等此操作
     */
    @Override
    public void update() {
        // 在这里可以处理金矿和石头的逻辑，例如移动它们或检测碰撞
        bg.update();
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {

        try {
            if (bg!=null){
                // 绘制金矿和石头
                bg.draw(canvas);
            }

            for (int i = 0; i < objectList.size(); i++) {
                Object object = objectList.get(i);
                object.draw(canvas);
            }
            line.paintSelf(canvas);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 下一关
     */
    public void nextLevel(){
        if (bg.gameTime() && state == GameState.running) {
            if (Bg.count >= Bg.goal) {
                if (Bg.level == mNumberStages) {
                    state = GameState.win;
                    handler.post(() -> {
                        if (gameCallBack!=null){
                            gameCallBack.onWin();
                        }
                    });
                } else {
//                    state = GameState.shop;
                    Bg.level++;
                    if (gameCallBack!=null){
                        gameCallBack.onLevel(String.valueOf(Bg.level));
                    }

                    if (gameCallBack!=null){
                        gameCallBack.onScore(String.valueOf(Bg.count));
                    }

                    if (gameCallBack!=null){
                        gameCallBack.onTargetonScore(String.valueOf(Bg.goal));
                    }
                    onNextL();

                }
            } else {
                state = GameState.fail;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (gameCallBack!=null){
                            gameCallBack.onFail();
                        }
                    }
                });
            }

        }
   }

    private void onNextL() {
        bg.startTime = System.currentTimeMillis();
        line.reGame();
        createMineral(context);
    }

    /**
     * 设置游戏的状态
     * @param state
     */
    public void setGameState(GameState state) {
        this.state = state;
    }


    @Override
    public void handleTouch(MotionEvent event) {
        // 处理触摸事件，例如点击金矿或石头时的交互
        gestureDetectorCompat.onTouchEvent(event);
    }

    /**
     * 点击事件适配器
     * @param event
     */
    private void setEventAdapter(MotionEvent event) {
//        0未开始，1运行中，2商店，3失败，4胜利
        switch (state) {
            case un_started:

                break;
            case running:
                if (line.state == 0) {
                    line.state = 1;
                }
                break;
            case fail:
            case win:
                break;
            default:
        }

    }

    /**
     * 重置
     */
    public void reGame() {
        bg.startTime = System.currentTimeMillis();
        line.reGame();
        createMineral(context);
        bg.reGame();
    }

    /**
     * 初始化宽高
     * @param visibleRectWidth
     * @param visibleRectHeight
     */
    public void setSceneWithAndHeight(int visibleRectWidth, int visibleRectHeight) {
        this.width = visibleRectWidth;
        this.height = visibleRectHeight;
        bg.setViewWidthAndHeight(width, height);
    }

    /**
     * 游戏回调监听
     * @param gameCallBack
     */
    public void setGameCallBack(GameCallBack gameCallBack) {
        this.gameCallBack = gameCallBack;
        if (bg!=null){
            bg.setGameCallBack(gameCallBack);
        }

        if (gameCallBack!=null){
            gameCallBack.onLevel(String.valueOf(Bg.level));
        }

        if (gameCallBack!=null){
            gameCallBack.onScore(String.valueOf(Bg.count));
        }

        if (gameCallBack!=null){
            gameCallBack.onTargetonScore(String.valueOf(Bg.goal));
        }
    }

    /**
     * 销毁 回收
     */
    public void onDestroy() {
        GifDrawableManager.getInstance().clearCache();
        for (int i = 0; i < objectList.size(); i++) {
            Drawable img = objectList.get(i).img;
            if (img != null && img instanceof GifDrawable) {
                ((GifDrawable) img).stop();
                ((GifDrawable) img).recycle();
            }
        }
        AdvancedThreadPoolUtil.cancelAllTasks();
        AdvancedThreadPoolUtil.shutdownThreadPool();
        flag=false;
        objectList.clear();
        bg.reGame();
        bg.recycle();
    }

}
