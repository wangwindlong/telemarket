package com.example.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-14
 * Time: 下午7:24
 * To change this template use File | Settings | File Templates.
 */
public class MachineLetterLv extends View {
    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] SEARCH_KEY = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public MachineLetterLv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MachineLetterLv(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MachineLetterLv(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / SEARCH_KEY.length;
        for (int i = 0; i < SEARCH_KEY.length; i++) {
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(SEARCH_KEY[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(SEARCH_KEY[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * SEARCH_KEY.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c > 0 && c < SEARCH_KEY.length) {
                        listener.onTouchingLetterChanged(SEARCH_KEY[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c > 0 && c < SEARCH_KEY.length) {
                        listener.onTouchingLetterChanged(SEARCH_KEY[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
}
