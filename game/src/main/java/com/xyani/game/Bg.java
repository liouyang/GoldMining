package com.xyani.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-07 17
 * Time:40
 */
public class Bg {

    private  GameCallBack gameCallBack;
    private  Context context;
    private  Bitmap bgBig;
    private  Bitmap bgSky;
    private  Bitmap peo;
    private  Bitmap water;
    private  Matrix bgMatrix;
    private  int bgWidth;
    private  int bgHeight;
    private  Matrix skyMatrix;
    private  int skyWidth;
    private  int skyHeight;
    private  Paint mPaint;
    private  Matrix propMatrix;
    private  int peoWidth;
    private  int peoHeight;

    private int visibleRectWidth;
    private int visibleRectHeight;


    private int srcWidth;
    private int srcHeight;
    // 坐标
    int x;
    int y;
    // 宽高
    int width;
    int height;

    private double propWith;


    // 关卡数
    static int level = 1;
    // 目标得分
    static int goal = (level * 500) + 60;
    // 总分
    static int count = 0;
    // 开始时间
    long startTime;
    // 结束时间
    long endTime;
    private GamePlayScene gamePlayScene;


    public Bg(Context context, GamePlayScene gamePlayScene) {
        this.context = context;
        this.gamePlayScene = gamePlayScene;
        //字体 画笔
        mPaint = new Paint();

        // 载入图片
        bgBig = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        bgSky = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
        peo = BitmapFactory.decodeResource(context.getResources(), R.drawable.peo);


        bgMatrix = new Matrix();

        skyMatrix = new Matrix();

        propMatrix = new Matrix();

        bgWidth = bgBig.getWidth();
        bgHeight = bgBig.getHeight();

        skyWidth = bgSky.getWidth();
        skyHeight = bgSky.getHeight();

        peoWidth = peo.getWidth();
        peoHeight = peo.getHeight();


    }



    // 绘制
    void draw(Canvas g) {
        drawBitmap(g);
        if (gamePlayScene==null||gamePlayScene.state==null){
            return;
        }
        switch (gamePlayScene.state) {
            case un_started:

                break;
            case running:
                long time=0;
                endTime = System.currentTimeMillis();
                time = Config.clearanceTime - (endTime - startTime) / 1000;
                if (gameCallBack!=null){
                    // 实时赋值
                    gameCallBack.onGameTimeCallBack(time);
                }
                if (!Config.isShowDefaultUI){
                    return;
                }
                drawImage(g, peo,( Config.getScreenWidth(context)-244)/2, Config.skyBoundary-252, 244, 252);

                goal = (level * 500) + 60;

                String ad = "目标：" + Bg. goal;

                drawWord(g, 22, Color.BLACK,ad, 15, 37);
                drawWord(g, 22, Color.BLACK, "分数：" + count, 15, 62);
                String level = "关卡：" + Bg. level;

                mPaint.setTextSize(22);
                float v = mPaint.measureText(level);
                // 关卡数&目标得分
                drawWord(g, 20, Color.BLACK, level, Config.getScreenWidth(context)-(int)v, 37);


                String timeStr="时间：" + (time > 0 ? time : 0);
                mPaint.reset();
                mPaint.setTextSize(22);
                float tv = mPaint.measureText(timeStr);
                drawWord(g, 22, Color.BLACK, timeStr, Config.getScreenWidth(context)-(int)tv, 62);
                break;
            case shop:

                break;
            case win:

                break;
            case fail:

                break;

        }

    }

    private void drawImage(Canvas g, Bitmap bitmap, int x, int y, int desWith, int desHeight) {
        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, desWith, desHeight, true);
            g.drawBitmap(scaledBitmap, x, y, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scaledBitmap.recycle();
        }

    }

    // t倒计时完成，f未完成倒计时
    boolean gameTime() {
        long time = (endTime - startTime) / 1000;
        if (time >= Config.clearanceTime) {
            return true;
        }
        return false;
    }

    // 重置元素
    void reGame() {
        // 关卡数
        level = 1;
        // 目标得分
        goal = (level * 500) + 60;
        // 总分
        count = 0;
    }

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

    public void update() {


    }



    public void setGameCallBack(GameCallBack gameCallBack) {
        this.gameCallBack = gameCallBack;
    }

    // 绘制字符串
    public void drawWord(Canvas g, int size, int color, String str, int x, int y) {
        try {
            mPaint.reset();
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(size);
            g.drawText(str, x, y, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void recycle() {
        try {
            bgBig.recycle();
            bgSky.recycle();
            peo.recycle();
            bgBig =null;
            bgSky =null;
            peo=null;

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
