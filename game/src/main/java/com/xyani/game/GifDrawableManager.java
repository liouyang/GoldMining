package com.xyani.game;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-07 18
 * Time:45
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import java.lang.ref.WeakReference;

import pl.droidsonroids.gif.GifDrawable;

public class GifDrawableManager {

    private static GifDrawableManager instance;
    private LruCache<Integer, WeakReference<Drawable>> cache;

    private GifDrawableManager() {
        // 创建一个缓存，限制最大缓存数
        int maxCacheSize = 20; // 设置适当的缓存大小
        cache = new LruCache<>(maxCacheSize);
    }

    public static GifDrawableManager getInstance() {
        if (instance == null) {
            synchronized (GifDrawableManager.class) {
                if (instance == null) {
                    instance = new GifDrawableManager();
                }
            }
        }
        return instance;
    }

    public Drawable getGifDrawable(Context context,int resourceId) {
        WeakReference<Drawable> gifDrawable = cache.get(resourceId);
        if (gifDrawable == null) {
            // 如果缓存中没有该GifDrawable，则创建一个新的
            gifDrawable =  new WeakReference<>(GifDrawable.createFromResource(context.getResources(), resourceId));
            cache.put(resourceId,gifDrawable);
        }
        return gifDrawable.get();
    }

    public Drawable getDrawable(Context context,int resourceId) {
        WeakReference<Drawable> gifDrawable = cache.get(resourceId);
        if (gifDrawable == null) {
            // 如果缓存中没有该GifDrawable，则创建一个新的
            gifDrawable =new WeakReference<>( context.getResources().getDrawable(resourceId));
            cache.put(resourceId, gifDrawable);
        }
        return gifDrawable.get();
    }

    public void clearCache() {
        cache.evictAll();
    }

    public void removeDrawable(Integer resourceId) {
        cache.remove(resourceId);
    }
}

