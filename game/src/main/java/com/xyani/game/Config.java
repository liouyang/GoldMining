package com.xyani.game;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-07 17
 * Time:40
 */
public class Config {

    /**
     * 分布区与天空区域的分界线 距离屏幕顶部高度
     */
    public static final int skyBoundary=300;


    public static final int mDisBoundaryBottomCenterY=60;

    /**
     * 游戏场景的宽高
     */
    public static  int gameViewWidth=0;

    public static  int gameViewHeight=0;

    /**
     * 是否显示默认ui
     */
    public static boolean isShowDefaultUI=true;


    /**
     * 生成小石头的几率 0-1
     */
    public static float mGoldMiniProbability=0.5f;


    /**
     * 生成大石头的几率 0-1
     */
    public static float mGoldProbability=0.7f;



    /**
     * 关卡数 通关
     */
    public static float mNumberStages=10;

    /**
     * 最小长度线
     */
    public static float MIN_LENGTH_LINE=40;

    /**
     * 每关通关时间 s  时间到还没达到分数表示结束
     */
    public static int clearanceTime=1000;

    /**
     * 摆动的幅度因子 越大摆动越快 0-1
     */
    public static float mFactorSwing=0.5f;

    /**
     * 每次前进的长度
     */
    public static float mLengthVector=10f;

    public static int getScreenWidth(Context context){
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
        // 如果无法获取屏幕宽度，则返回0或者其他适当的默认值
        return 0;
    }

    public static int getScreenHeight(Context context){
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.heightPixels;
        }
        // 如果无法获取屏幕宽度，则返回0或者其他适当的默认值
        return 0;
    }

    public int dpToPixels(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
