package com.xyani.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;




public class Object {

    // 坐标
    int x;
    int y;
    // 宽高
    int width;
    int height;
    // 图片
    Drawable img;
    // 标记 是否能移动
    boolean flag = false;
    // 质量
    int m;
    // 积分
    int count;
    // 类型 1 金块 2 石块 3
    int type;


    void draw(Canvas g) {
        if (img!=null){
            Drawable gifDrawable =img;
            if (gifDrawable!=null){
                gifDrawable.setBounds(getRec());
                gifDrawable.draw(g);
            }
        }
    }


    public int getWidth() {
        return width;
    }


    public Rect getRec() {
        return new Rect(x, y,x+ width,y+ height);
    }
}
