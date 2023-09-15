package com.xyani.game;

import java.util.Random;

public class RandomUtils {

    // 生成指定区间内的随机整数
    public static int getRandomIntInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("最小值必须小于或等于最大值");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
