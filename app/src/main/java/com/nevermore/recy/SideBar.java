package com.nevermore.recy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/19.
 */

public class SideBar extends View {

    private int width;
    private int height;
    private String[] charaters;
    private Paint paint;
    private int cellHeight;

    public SideBar(Context context) {
        this(context,null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        charaters = new String[26];
        for (int i = 0; i < 26; i++) {
            char c = (char) ('A'+i);
            charaters[i] = String.valueOf(c);
            Log.i(TAG, "init: "+charaters[i]);
        }
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
    }

    private static final String TAG = "SideBar";

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        cellHeight = (h-getPaddingTop()-getPaddingBottom()) / charaters.length;
        float v = (float) ((float) (cellHeight)*0.8);
        paint.setTextSize(v);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfW = width / 2;
        for (int i = 0; i < charaters.length; i++) {
            int y = cellHeight * (i + 1)+getPaddingTop();
            float v = paint.measureText(charaters[i]);
            float x = halfW - v / 2;
            canvas.drawText(charaters[i],x,y,paint);
        }
    }

    private int getIndex(float y){
        int index = (int) ((y - getPaddingTop()) / cellHeight);
        if(index>charaters.length-1){
            index = charaters.length-1;
        }
        return index;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int index = getIndex(y);

                if(onIndexTouch!=null){
                    onIndexTouch.onIndex(index,charaters[index]);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(onIndexTouch!=null){
                    onIndexTouch.onCancel();
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    private OnIndexTouch onIndexTouch;

    public void setOnIndexTouch(OnIndexTouch onIndexTouch) {
        this.onIndexTouch = onIndexTouch;
    }

    public interface OnIndexTouch{
        void onIndex(int index,String charater);

        void onCancel();
    }
}
