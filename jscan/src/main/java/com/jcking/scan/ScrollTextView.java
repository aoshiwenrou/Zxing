package com.jcking.scan;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrollTextView extends TextView {

    private boolean isAotuScrollBottom = true;
    private long mLastTime;

    public ScrollTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setAotuScrollBottom(boolean auto) {
        this.isAotuScrollBottom = auto;
    }

    public void appendln(CharSequence text) {
        append(text + "\n");
    }

    public void appendWithTime(CharSequence text) {
        String duraing = "";
        long current = System.currentTimeMillis();
        if(mLastTime != 0){
            long cost = current - mLastTime;
            duraing = " (" + formatMS(cost) + ")";
        }
        mLastTime = current;
        appendln(getTime() + "  " + text + duraing);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss SSS");
        return format.format(new Date());
    }

    private String formatMS(long time){
        if(time < 1000)
            return time + "ms";
        long s = time / 1000;
        long ms = time % 1000;
        return s + "s " + ms + "ms";
    }

    @Override
    public void append(CharSequence text, int start, int end) {
        super.append(text, start, end);
        if (isAotuScrollBottom)
            scrollToBottom();
    }

    private void scrollToBottom() {
        int height = getHeight();
        int contentHeight = getLineHeight() * getLineCount();
        if (height - getLineHeight() >= contentHeight)
            return;
        setScrollY(contentHeight - height + getLineHeight());
    }
}
