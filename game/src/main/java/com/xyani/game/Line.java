package com.xyani.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


public class Line {
    /**
     * 存放图形变换后的数据
     */
    private final RectF scaleRec;
    /**
     * 爪 的原始宽度
     */
    private final int hookOriginalWidth;

    /**
     * 爪 的原始高度
     */
    private final int hookOriginalHeight;

    /**
     * 爪 的原始位置
     */
    private final RectF originalRect;

    /**
     * 位图
     */
    private final Bitmap hook;

    /**
     * 画笔
     */
    private final Paint paint;

    /**
     * 矩阵操作数据
     */
    private final Matrix matrix;

    /**
     * 起点坐标 x
     */
    int x = 0;

    /**
     * 起点坐标 y
     */
    int y = 0;
    /**
     * 终点坐标 x
     */
    int endx = 0;

    /**
     * 终点坐标 y
     */
    int endy = 0;

    /**
     * 线段长度
     */
    double length = 40;

    /**
     * 最小长度
     */
    double MIN_length = Config.MIN_LENGTH_LINE;

    /**
     * 最大长度
     */
    double MAX_length = 750;

    /**
     * 左右 向量大小
     */
    double n = 0;

    /**
     * 钟摆方向
     */
    int dir = 1;

    /**
     * 0摇摆，1抓取，2回收，3抓取返回
     */
    int state = 0;


    GamePlayScene frame;
    /**
     * 角度
     */
    private double currentAngle;
    /**
     * 物体质量
     */
    int m = 1;
    private Context context;

    Line(Context context, GamePlayScene frame) {
        this.frame = frame;
        this.context = context;
        x = Config.getScreenWidth(context) / 2;
        y = Config.skyBoundary -Config. mDisBoundaryBottomCenterY;
        MAX_length = TriangleCalculator.calculateHypotenuse(Config.getScreenWidth(context)/2f,(Config.getScreenHeight(context)-y));
        hook = BitmapFactory.decodeResource(context.getResources(), R.drawable.hook);
        hookOriginalWidth = hook.getWidth();
        hookOriginalHeight = hook.getHeight();
        matrix = new Matrix();
        // 创建原始矩形
        originalRect = new RectF(0, 0, hookOriginalWidth, hookOriginalHeight);
        scaleRec = getScaleRec();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * 判断是否抓取成功
     */
    void logic() {
        for (Object obj : this.frame.objectList) {
            if (endx > obj.x && endx < obj.x + obj.width && endy > obj.y && endy < obj.y + obj.height) {
                state = 3;
                obj.flag = true;
            }
        }

    }

    /**
     * 绘制方法
     *
     * @param g
     */
    void lines(Canvas g) {
        try {
            endx = (int) (x + length * Math.cos(n * Math.PI));
            endy = (int) (y + length * Math.sin(n * Math.PI));
            g.drawLine(x, y, endx, endy, paint);
            drawImage(g);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 绘制爪
     *
     * @param g
     */
    private void drawImage(Canvas g) {
        try {
            if (hook != null) {
                matrix.reset();
                //结束点相对起始点的角度偏移
                currentAngle = calculateAngle(x, y, endx, endy);
                //缩放图形爪
                matrix.postScale(0.3f, 0.3f);
                //映射缩放后的大小
                matrix.mapRect(scaleRec, originalRect);

                matrix.postRotate((float) currentAngle - 90, scaleRec.width() / 2, scaleRec.height() / 2);

                matrix.postTranslate(endx - scaleRec.width() / 2, endy - scaleRec.height() / 2);

                // 设置图片绘制位置, 保证图片中心处于曲线上

                g.drawBitmap(hook, matrix, paint);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RectF getScaleRec() {
        return new RectF(0, 0, 0, 0);
    }


    /**
     * 计算点点位偏移角度
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double calculateAngle(double x1, double y1, double x2, double y2) {
        // 计算两点之间的水平和垂直距离
        double dx = x2 - x1;
        double dy = y2 - y1;

        // 使用反正切函数计算夹角（弧度）
        double radians = Math.atan2(dy, dx);

        // 将弧度转换为度数
        double degrees = Math.toDegrees(radians);
        // 保证角度在 0 到 360 度之间
        if (degrees < 0) {
            degrees += 360;
        }

        return degrees;
    }


    void paintSelf(Canvas g) {

        logic();
        switch (state) {
            //摇摆
            case 0:
                if (length < MIN_length) {
                    length = MIN_length;
                }
                if (n < 0.1) {
                    dir = 1;
                } else if (n > 0.9) {
                    dir = -1;
                }

                n = n + Config.mFactorSwing * dir;
                lines(g);
                break;
            //抓取
            case 1:
                if (!checkCollision(endx,endy,0,Config.getScreenWidth(context),Config.gameViewHeight)) {
                    length = length + Config.mLengthVector;
                    lines(g);
                } else {
                    state = 2;
                }
                break;
            //回收
            case 2:
                if (length > MIN_length) {
                    length = length - Config.mLengthVector;
                    lines(g);
                } else {
                    if (length < MIN_length) {
                        length = MIN_length;
                    }
                    state = 0;
                }
                break;
            //抓取返回
            case 3:
                if (length > MIN_length) {
                    getLengthByQuality();
                    lines(g);
                    for (Object obj : this.frame.objectList) {
                        if (obj.flag) {
                            m = obj.m;
                            obj.x = endx - obj.getWidth() / 2;
                            obj.y = endy;
                            if (length <= MIN_length) {
                                obj.x = -150;
                                obj.y = -150;
                                obj.flag = false;
                                // 加分
                                Bg.count += obj.count;
                                state = 0;
                            }

                        }
                    }
                }

                break;
            default:
        }
    }
    public boolean checkCollision(float x, float y, float rectLeft, float rectRight, float rectBottom) {
        // 检查点是否在矩形的左边、右边或底部
        if (x <= rectLeft || x >= rectRight || y >= rectBottom) {
            return true; // 发生碰撞
        } else {
            return false; // 没有碰撞
        }
    }

    /**
     * 通过不同的质量计算长度
     */
    private void getLengthByQuality() {
        if (m == 30) {
            length = length - 8;
        } else if (m == 100) {
            length = length - 3;
        } else {
            length = length - 5;
        }
    }


    // 重置线
    void reGame() {
        n = 0;
        state = 0;
        length = 40;
        m = 1;
    }

}
