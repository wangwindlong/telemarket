
package com.example.component;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;
import com.example.telemarket.bean.AnnouncementBean;
import com.example.tool.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本垂直滚动
 *
 * @author HeJx
 */
public class VerticalScrollTextView extends TextView {
    /**
     * 每行的字符串
     */
    ArrayList<String> lineStrings = new ArrayList<String>();
    List<AnnouncementBean> beanList;
    /**
     * 当前的位移
     */
    float currentY;
    /**
     * 处理滚动消息
     */
    Handler handler;
    /**
     * 要显示的text
     */
    String scrollText = "";


    /**
     * 真实宽度,在配置width="xxdp"里起作用
     */
    private int exactlyWidth = -1;

    /**
     * 真实高度,在配置height="xxdip"里起作用
     */
    private int exactlyHeight = -1;
    private float index = 0;

    public String getScrollText() {
        return scrollText;
    }

    public void setScrollText(String scrollText) {
        this.scrollText = scrollText;
        reset();
    }

    public void setScrollList(List<AnnouncementBean> announcementList) {
        this.beanList = announcementList;
        reset();
    }

    /**
     * 重置
     */
    private void reset() {
        if (lineStrings != null)
            lineStrings.clear();
        stop();
        currentY = 0;
        absloutHeight = 0;
        this.setText("");
        if (handler != null) {
            handler.removeMessages(0);
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler.removeMessages(3);
        }
        requestLayout();
        invalidate();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VerticalScrollTextView(Context context) {
        super(context);
        init();
    }

    /**
     * 是否在滚动
     */
    boolean scrolling = false;

    /**
     * 实际高度：所有字显示完全需要的高度
     */
    float absloutHeight = 0;

    /**
     * handler发消息的时间间隔
     */
    private int delayTime = 10;  //滚动时间
    private int stopTime = 3000;  //停休时间

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * 每次滚动的距离
     */
    float speed = 0.3f;

    /**
     * 初始化
     */
    void init() {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (absloutHeight <= getHeight()) {
                    currentY = 0;
                    stop();
                    return;
                }
                switch (msg.what) {
                    case 0: {
                        currentY = currentY - speed;
                        //resetCurrentY();
                        invalidate();
                        boolean flag = true;
                        if (currentY >= absloutHeight || currentY <= -absloutHeight) {
                            flag = false;
                            handler.sendEmptyMessageDelayed(3, stopTime);
                        }
                        if (flag) {
                            if (currentY >= index || currentY <= -index || getHeight() <= 0) {
                                handler.sendEmptyMessageDelayed(2, stopTime);
                            } else {
                                handler.sendEmptyMessageDelayed(0, delayTime);
                            }
                        }
                        break;
                    }
                    case 1: {
                        currentY += msg.arg1;
                        resetCurrentY();
                        invalidate();
                    }
                    break;
                    case 2: {
                        index = index + absloutHeight / lineStrings.size();
                        stop();
                        handler.sendEmptyMessageDelayed(0, delayTime);
                    }
                    break;
                    case 3: {
                        currentY = 0;
                        index = absloutHeight / lineStrings.size();
                        stop();
                        handler.sendEmptyMessageDelayed(0, delayTime);
                    }
                }
            }

            /**
             * 重置currentY（当currentY超过absloutHeight时，让它重置为0）
             */
            private void resetCurrentY() {
                if (currentY >= absloutHeight || currentY <= -absloutHeight || getHeight() <= 0) {
                    currentY = 0;
                }
            }
        };
    }

    /**
     * 上次触发事件的手指y坐标
     */
    float lastY = 0;

    /**
     * 为true代表原来是滚动
     */
    boolean needStop;

    public void pause() {
        if (scrolling) {
            stop();
            needStop = true;
        }
    }

    public void goOn() {
        if (needStop) {
            play();
            needStop = false;
        }
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                distanceY = lastY = event.getY();
                distanceX = event.getX();
                pause();

                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - lastY;
                lastY = event.getY();
                // currentY = currentY + dy;
                Message msg = Message.obtain();
                msg.what = 1;
                msg.arg1 = (int)dy;
                handler.sendMessage(msg);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                goOn();
                float y = event.getY() - distanceY;
                float x = event.getX() - distanceX;

                if (Math.sqrt(y * y + x * x) < performUpScrollStateDistance) {
                    updateScrollStatus();
                }
                return true;

        }
        return super.onTouchEvent(event);
    }
*/
    /**
     * 当手指移动此大小的距离以内，被认为是滚动状态改变的事件，大于该值代表是手指搬运
     */
    public static final long performUpScrollStateDistance = 5;
    public float distanceY = 0;
    public float distanceX = 0;

    /**
     * 更改滚动状态
     */
    public void updateScrollStatus() {
        if (scrolling) {
            stop();
        } else {
            play();
        }
    }

    /**
     * 开始滚动
     */
    public void play() {
        if (!scrolling) {
            handler.sendEmptyMessage(0);
            scrolling = true;
        }
    }

    /**
     * 停止滚动
     */
    public void stop() {
        if (scrolling) {
            handler.removeMessages(0);
            scrolling = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureWidth(widthMeasureSpec);
        int height = MeasureHeight(width, heightMeasureSpec);
        setMeasuredDimension(width, height);
        currentY = 0;
        if (height < absloutHeight) {
            play();
        } else {
            stop();
        }
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int MeasureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 如果是wrap_content
        if (mode == MeasureSpec.AT_MOST) {
            double abwidth = getPaint().measureText(scrollText);
            width = Math.min((int) Math.rint(abwidth), width);
            exactlyWidth = -1;
        }
        if (mode == MeasureSpec.EXACTLY) {
            exactlyWidth = width;
        }
        return width;
    }

    /**
     * 测量高度
     *
     * @param width:宽度
     * @param heightMeasureSpec
     * @return
     */
    private int MeasureHeight(int width, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        generateTextList(width);
        int lines = lineStrings.size();

        absloutHeight = lines * getLineHeight() + getPaddingBottom() + getPaddingTop();
        index = absloutHeight / lines;
        // 如果是wrap_content
        if (mode == MeasureSpec.AT_MOST) {
            height = (int) Math.min(absloutHeight, height);
            exactlyHeight = -1;
        } else if (mode == MeasureSpec.EXACTLY) {
            exactlyHeight = height;
        }
        return height;
    }

    /**
     * 是否为英文单词的首字母
     *
     * @param str
     * @param i
     * @return
     */
    boolean isENWordStart(String str, int i) {
        if (i == 0) {
            return true;
        } else if (str.charAt(i - 1) == ' ') {
            return true;
        }
        return false;
    }

    /**
     * 获取一行的字符
     *
     * @param MaxWidth
     * @param str
     * @return
     */
    private String getLineText(int MaxWidth, String str) {
        // 真实行
        StringBuffer trueStringBuffer = new StringBuffer();
        // 临时行
        StringBuffer tempStringBuffer = new StringBuffer();
        boolean isLine;
        for (int i = 0; i < str.length(); i++) {
            isLine = false;
            char c = str.charAt(i);
            String add = "";
            // 如果c是字母需要考虑英文单词换行功能
            if (!isChinese(c) && isENWordStart(str, i)) {
                int place = getNextSpecePlace(i, str);
                // 找到下一个空格
                if (place > -1) {
                    add = str.substring(i, place) + " ";
                    if (getPaint().measureText(add) > MaxWidth) {
                        add = "" + c;
                    } else {
                        i = place;
                    }
                } else {
                    add = "" + c;
                }
            } else {
                if (c == '\n') { //判断是否到了换行的标示符
                    isLine = true;
                    add = "" + c;
                } else
                    add = "" + c;
            }
            tempStringBuffer.append(add);
            String temp = tempStringBuffer.toString();
            float width = getPaint().measureText(temp.toString());
            if (isLine)
                break;
            if (width <= MaxWidth) {
                trueStringBuffer.append(add);
            } else {
                break;
            }
        }
        return trueStringBuffer.toString();
    }

    /**
     * 找到下一个空格的地方
     *
     * @param i
     * @param str
     * @return
     */
    int getNextSpecePlace(int i, String str) {
        for (int j = i; j < str.length(); j++) {
            char c = str.charAt(j);
            if (c == '\n') {
                return j;
            }
        }
        return -1;
    }

    /**
     * 生成多行字符串列表
     *
     * @param MaxWidth
     */
    public void generateTextList(int MaxWidth) {
        lineStrings.clear();
        // String remain = scrollText;
        if (beanList != null && beanList.size() >= 0) {
            for (int i = 0; i < beanList.size(); i++) {
                lineStrings.add(beanList.get(i).getTitle());
            }
        }
        if (StringUtil.isNotBlank(scrollText)) {
            String[] contenx = scrollText.split("k#");
            for (String string : contenx) {
                lineStrings.add(string);
            }
        }
     /*   while (!remain.equals("")) {
            String line = getLineText(MaxWidth, remain);
            lineStrings.add(line);
            remain = remain.substring(line.length(), remain.length());

        }*/
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        float x = getPaddingLeft();
        float y = getPaddingTop();

        float lineHeight = getLineHeight();
        float textSize = getPaint().getTextSize();

        for (int i = 0; i < lineStrings.size(); i++) {
            y = lineHeight * i + textSize + currentY;

            float min = 0;
            if (exactlyHeight > -1) {
                min = Math.min(min, exactlyHeight - absloutHeight);
            }
            if (y < min) {
                y = y + absloutHeight;
            } else if (y >= min && y < textSize + min) {
                //如果最顶端的文字已经到达需要循环从下面滚出的时候
                canvas.drawText(lineStrings.get(i), x, y + absloutHeight, getPaint());
            }
            if (y >= absloutHeight) {
                //如果最底端的文字已经到达需要循环从上面滚出的时候
                canvas.drawText(lineStrings.get(i), x, y, getPaint());
                y = y - absloutHeight;
            }
            canvas.drawText(lineStrings.get(i), x, y, getPaint());
        }
    }

    /**
     * 判断是否为中文
     *
     * @param c
     * @return
     */
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public int getCurrentIndex(){
        int size = lineStrings.size();
        return (int)(Math.abs(currentY)*size/absloutHeight);
    }
}
