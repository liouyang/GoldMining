package com.xyani.game;

import android.content.Context;




public class Rock extends Object {
    Rock(Context context) {
        this.width = 71;
        this.height = 71;
        this.x = (int) (RandomUtils.getRandomIntInRange(0,(Config.getScreenWidth(context)-width)));
        this.y = (int) (RandomUtils.getRandomIntInRange(Config.skyBoundary,(Config.getScreenHeight(context))-height));
        this.flag = false;
        this.m = 100;
        this.count = 15;
        this.type = 2;
       this.img =GifDrawableManager.getInstance().getDrawable(context,R.drawable.rock1);
    }
}
