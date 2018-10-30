package top.aiboom.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyProgressView extends View {

    private int color;
    private Path path;
    private Paint paint;
    private RectF rectF;
    private float radius;
    private float progress;
    private float outRadius;

    public MyProgressView(Context context) {
        this(context, null);
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.getResources().obtainAttributes(attrs, R.styleable.MyProgressView);
            color = a.getColor(R.styleable.MyProgressView_color, Color.GRAY);
            radius = a.getDimension(R.styleable.MyProgressView_inner_radius, 0);
            progress = a.getFloat(R.styleable.MyProgressView_start_progress, 0);
            outRadius = a.getDimension(R.styleable.MyProgressView_outer_radius, 0);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectF == null) {
            rectF = new RectF(getWidth()/2-radius, getHeight()/2-radius, getWidth()/2+radius, getHeight()/2+radius);
        }

        if (path == null) {
            path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.addRect(0,0, getWidth(), getHeight(), Path.Direction.CCW);
            path.addCircle(getWidth()/2, getHeight()/2, outRadius, Path.Direction.CCW);
        }

        if (paint == null) {
            paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        canvas.drawPath(path, paint);
        canvas.drawArc(rectF, 0, progress, true, paint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
        Log.e("HGL", "setProgress: "+progress);
        if ((int)progress == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    //第一个参数为 view对象，第二个参数为 动画改变的类型，第三，第四个参数依次是开始透明度和结束透明度。
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(MyProgressView.this, "alpha", 1f, 0f);
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(MyProgressView.this, "scaleX", 1f, 3f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(MyProgressView.this, "scaleY", 1f, 3f);
                    /*alpha.setDuration(2000);//设置动画时间
                    alpha.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
                    alpha.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
                    alpha.setRepeatMode(ValueAnimator.REVERSE);//设置动画循环模式。
                    alpha.start();//启动动画。*/
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(alpha, scaleX, scaleY);
                    set.start();
                }
            });
        }
    }

    public float getProgress() {
        return progress;
    }
}
