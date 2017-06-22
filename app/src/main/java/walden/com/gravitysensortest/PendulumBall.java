package walden.com.gravitysensortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PendulumBall extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private RenderThread renderThread;
    private float[] values;
    int mWidth = 0;
    int mHeight = 0;
    Paint linePaint;
    Paint ciclePaint;
    int mCircleX;       //圆X坐标
    int mCircleY;       //圆Y坐标
    int circleR;        //圆半径
    int mLineStopX;             //直线终点X
    int mLineStopY;             //直线终点Y

    public PendulumBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        holder = this.getHolder();
        holder.addCallback(this);

        setZOrderOnTop(true);                       //视图放置在顶层
        holder.setFormat(PixelFormat.TRANSLUCENT);  //半透明的

        renderThread = new RenderThread();

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.RED);
        linePaint.setTextSize(50);
        linePaint.setStrokeWidth(3);

        ciclePaint = new Paint();
        ciclePaint.setAntiAlias(true);
        ciclePaint.setStyle(Paint.Style.FILL);
        ciclePaint.setColor(Color.parseColor("#CEFFC2"));
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
        mCircleY = mHeight / 2 + circleR;
        mLineStopX = mWidth / 2;
        mLineStopY = mHeight / 2;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
        double scalevalue = Math.toRadians(values[0] * 9);

        double cos = Math.cos(scalevalue);
        double sin = Math.sin(scalevalue);

        canvas.drawLine(mWidth / 2, 0, (int) (mLineStopX - mLineStopY * sin), (int) (mLineStopY * cos), linePaint);

        canvas.drawCircle((int) (mCircleX - mCircleY * sin), (int) (mCircleY * cos), circleR, ciclePaint);
        canvas.drawText("晕", (float) (mCircleX - mCircleY * sin - 0.3 * circleR), (float) (mCircleY * cos + 0.2 * circleR), linePaint);
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
