package com.jdsolutions.customprogressbar.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by juned on 10-Apr-17 at 3:00 PM.
 */

public class CircleView extends View
{
    private Paint backgroundPaint;
    private Paint progressPaint;
    private float centerWidth,sweepAngle = 0;
    private float centerHeight,radius = 100f;
    private float textSize ;
    private String labelText = "0%";
    private Handler viewHandler;
    private Runnable updateView;
    private boolean isTextVisible = true;

    public CircleView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setAntiAlias(false);
        backgroundPaint.setStrokeWidth(3);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setColor(Color.GRAY);

        textSize = 50;
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(5);
        progressPaint.setTextAlign(Paint.Align.CENTER);
        progressPaint.setTextSize(textSize);
        progressPaint.setColor(Color.parseColor("#ff0000"));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // get the center of the view
        centerWidth = canvas.getWidth() / 2;
        centerHeight = canvas.getHeight() / 2;

        canvas.drawCircle(centerWidth,centerHeight,radius,backgroundPaint);

        if(isTextVisible())
        {
            canvas.drawText(getLabelText(),centerWidth,centerHeight+(textSize/4),progressPaint);
        }
        drawRect(canvas);
    }

    private void drawRect(Canvas canvas)
    {
        Paint myPaint = new Paint();
        myPaint.setColor(Color.parseColor("#ff0000"));
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(10);

        float left = centerWidth - radius;
        float right = centerWidth + radius;
        float top = centerHeight - radius;
        float bottom = centerHeight + radius;

        RectF oval = new RectF(left, top, right, bottom);
        canvas.drawArc(oval,270,sweepAngle,false,myPaint);

        /*if(sweepAngle == 0)
        {
            timer();
        }*/
    }

    private void timer()
    {
        viewHandler = new Handler();
        updateView = new Runnable()
        {
            @Override
            public void run()
            {
                sweepAngle = sweepAngle+10;
                invalidate();
                if(sweepAngle != 360)
                {
                    viewHandler.postDelayed(updateView, 100);
                }
                else
                {
                    Log.e("finish","finish");
                }
            }
        };
        viewHandler.post(updateView);
    }

    private void drawSemiCircle(Canvas canvas)
    {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        radius = 300f;

        Path path = new Path();
        path.addCircle(width / 2,height / 2, radius,Path.Direction.CW);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);

        float center_x, center_y;
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);

        center_x = width / 2;
        center_y = height / 2;

        oval.set(center_x - radius,center_y - radius,center_x + radius,center_y + radius);
        canvas.drawArc(oval, 90, 180, false, paint);
    }

    private void drawArcs(Canvas canvas)
    {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height)
        {
            radius = height / 4;
        }
        else
        {
            radius = width / 4;
        }

        Path path = new Path();
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        float center_x, center_y;
        center_x = width / 2;
        //center_y = height / 4;
        center_y = height / 2;
        final RectF oval = new RectF();
        oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);
        canvas.drawArc(oval, 0,90, true, paint);

        paint.setColor(Color.GREEN);
        canvas.drawArc(oval, 90,90, true, paint);

        paint.setColor(Color.RED);
        canvas.drawArc(oval, 180,90, true, paint);

        paint.setColor(Color.GRAY);
        canvas.drawArc(oval, 270,90, true, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int desiredWidth = 300;
        int desiredHeight = 300;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.e("widthSize",widthSize+"   "+ widthMode);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("heightSize",heightSize+"  "+ heightMode);
        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            width = widthSize;
            textSize = width/4;
        }
        else if (widthMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
            textSize = 80;
        }
        else
        {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            height = heightSize;
        }
        else if (heightMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);

        }
        else
        {
            //Be whatever you want
            height = desiredHeight;
        }

        radius = (Math.min(width,height)/2)-10;

        progressPaint.setTextSize(textSize);

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public boolean isTextVisible()
    {
        return isTextVisible;
    }

    public void setTextVisible(boolean textVisible)
    {
        isTextVisible = textVisible;
    }

    public String getLabelText()
    {
        return labelText;
    }

    private void setLabelText(String labelText)
    {
        this.labelText = labelText;
    }

    public void setPercent(float percent)
    {
        setLabelText((int)percent+"%");
        sweepAngle = 360*(percent/100);
        invalidate();
    }
}