package com.xyani.game;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-09-15 13
 * Time:59
 */
import java.lang.Math;

public class TriangleCalculator {
    public static double calculateHypotenuse(double side1, double side2) {
        // 使用勾股定理计算斜边长度
        double hypotenuse = Math.sqrt(Math.pow(side1, 2) + Math.pow(side2, 2));
        return hypotenuse;
    }
}
