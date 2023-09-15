package com.xyani.game;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-11 15
 * Time:53
 */
public interface GameCallBack {


    /**
     * 游戏时间
     */
    void onGameTimeCallBack(long gameTime);

    /**
     * 关卡
     */
    void onLevel(String level);


    /**
     * 积分
     */
    void onScore(String scoreCount);


    /**
     * 目标积分
     */
    void onTargetonScore(String targetScoreCount);


    /**
     * 胜利
     */
    void onWin();


    /**
     * 失败
     */

    void onFail();


}
