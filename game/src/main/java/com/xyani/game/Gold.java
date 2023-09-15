package com.xyani.game;

import android.content.Context;




public class Gold extends Object {

    Gold(Context context) {
        this.width = 52;
        this.height = 52;
        this.x =  (RandomUtils.getRandomIntInRange(0,(Config.getScreenWidth(context)-width)));
        this.y =(RandomUtils.getRandomIntInRange(Config.skyBoundary,(Config.getScreenHeight(context))-height));
        this.flag = false;
        this.m = 30;
        this.count = 250;
        this.type = 1;
        this.img = GifDrawableManager.getInstance().getGifDrawable(context,R.drawable.gold0);
    }
}

class GoldMini extends Gold {
    GoldMini(Context context) {
        super(context);
        this.width = 36;
        this.height = 36;
        this.x =(RandomUtils.getRandomIntInRange(0,(Config.getScreenWidth(context)-width)));
        this.y = (RandomUtils.getRandomIntInRange(Config.skyBoundary,(Config.getScreenHeight(context))-height));
        this.m = 30;
        this.count = 150;
        this.img =  GifDrawableManager.getInstance().getGifDrawable(context,R.drawable.gold1);
    }
}

class GoldPlus extends Gold {
    GoldPlus(Context context){
        super(context);
        this.width = 105;
        this.height = 105;
        this.x = (RandomUtils.getRandomIntInRange(0,(Config.getScreenWidth(context)-width)));
        this.y =(RandomUtils.getRandomIntInRange(Config.skyBoundary,(Config.getScreenHeight(context))-height));
        this.m = 100;
        this.count = 500;
        this.img = GifDrawableManager.getInstance().getGifDrawable(context,R.drawable.gold2);
    }
}


