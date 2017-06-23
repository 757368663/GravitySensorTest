package walden.com.gravitysensortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class RollBall extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private RenderThread renderThread;
    private float[] values;
    int mWidth = 0;
    int mHeight = 0;
    Paint ciclePaint;
    Paint textPaint;
    int mCircleX;       //圆X坐标
    int mCircleY;       //圆Y坐标
    int circleR;        //圆半径

    int mCircleX1;       //圆X坐标
    int mCircleY1;       //圆Y坐标
    int sp1 = 5;              //速度1
    int sp2 = 8;              //速度2

    public RollBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        holder = this.getHolder();
        holder.addCallback(this);

        setZOrderOnTop(true);                       //视图放置在顶层
        holder.setFormat(PixelFormat.TRANSLUCENT);  //半透明的

        renderThread = new RenderThread();
        ciclePaint = new Paint();
        ciclePaint.setAntiAlias(true);
        ciclePaint.setStyle(Paint.Style.FILL);
        ciclePaint.setColor(Color.parseColor("#CEFFC2"));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(50);

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        renderThread.isRuning = true;
        renderThread.start();

        mWidth = getWidth();
        mHeight = getHeight();
        circleR = Math.min(mWidth, mHeight) / 10;
        mCircleX = mWidth / 2;
        mCircleY = mHeight / 2;

        mCircleX1 = mWidth / 3;
        mCircleY1 = mHeight / 3;


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("ssssss", "surfaceChanged: " + "format　：" + format + "\nwidth :" + width + "\nheight : " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        renderThread.isRuning = false;
    }

    private class RenderThread extends Thread {
        boolean isRuning = true;

        @Override
        public void run() {
            while (isRuning) {
                drawUI();
            }
        }
    }

    public void drawUI() {
        Canvas canvas = holder.lockCanvas();
        try {
            drawCanvas(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //在 canvas 上绘制需要的图形  根据values绘制
        float x = values[0];   //(-10 10)
        float y = values[1];     //(-10 10)

        mCircleX = (int) (mCircleX - x * sp1);
        mCircleY = (int) (mCircleY + y * sp1);
        mCircleX1 = (int) (mCircleX1 - x * sp2);
        mCircleY1 = (int) (mCircleY1 + y * sp2);


        if (mCircleX < circleR) {
            mCircleX = circleR + 1;
        } else if (mCircleX > mWidth - circleR) {
            mCircleX = mWidth - circleR - 1;
        }
        if (mCircleY < circleR) {
            mCircleY = circleR + 1;
        } else if (mCircleY > mHeight - circleR) {
            mCircleY = mHeight - circleR - 1;
        }
        if (mCircleX1 < circleR) {
            mCircleX1 = circleR + 1;
        } else if (mCircleX1 > mWidth - circleR) {
            mCircleX1 = mWidth - circleR - 1;
        }
        if (mCircleY1 < circleR) {
            mCircleY1 = circleR + 1;
        } else if (mCircleY1 > mHeight - circleR) {
            mCircleY1 = mHeight - circleR - 1;
        }


      /*  需求: 实现多个小球的不重叠运动，
           绘制实现：
              四种情况：
                向左运动：x>0
                向右运动：x<0
                向下运动：y>0
                向上运动：y<0

          控件动画实现
            多个View 重力控制动画轨迹

      */
        double space = Math.sqrt((mCircleX - mCircleX1) * (mCircleX - mCircleX1) + (mCircleY - mCircleY1) * (mCircleY - mCircleY1));
        if (2 <= space && space <= circleR * 2 + 1) {
            if (mCircleX > mCircleX1 && x < 0) {           //右  x<0
                mCircleX1 = (int) (mCircleX1 + x * sp2);
            } else if (mCircleX <= mCircleX1 && x < 0) {   //右 x<0
                mCircleX = (int) (mCircleX + x * sp1);

            } else if (mCircleX > mCircleX1 && x > 0) {    //向左 x>0
                mCircleX = (int) (mCircleX + x * sp1);
            } else if (mCircleX < mCircleX1 && x > 0) {    //向左 x>0
                mCircleX1 = (int) (mCircleX1 + x * sp2);
            }

            if (mCircleY > mCircleY1 && y < 0) {               //上 y<0
                mCircleY = (int) (mCircleY - y * sp1);
            } else if (mCircleY < mCircleY1 && y < 0) {       //上 y<0
                mCircleY1 = (int) (mCircleY1 - y * sp2+0.5);
            } else if (mCircleY > mCircleY1 && y > 0) {         //下 y>0
                mCircleY1 = (int) (mCircleY1 - y * 8);
            } else if (mCircleY <= mCircleY1 && y > 0) {        //下 y>0
                mCircleY = (int) (mCircleY - y * 6);
            }
        }



        canvas.drawCircle(mCircleX, mCircleY, circleR, ciclePaint);
        canvas.drawText("A", (float) (mCircleX - 0.2 * circleR), (float) (mCircleY + 0.1 * circleR), textPaint);

        canvas.drawCircle(mCircleX1, mCircleY1, circleR, ciclePaint);
        canvas.drawText("B", (float) (mCircleX1 - 0.2 * circleR), (float) (mCircleY1 + 0.1 * circleR), textPaint);
    }

    public void setValue(float[] value) {
        values = value;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        renderThread.isRuning = false;
    }
}
