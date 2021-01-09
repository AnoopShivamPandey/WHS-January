package com.apk_home_service.customer.UI.fragment;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

    private OnTouchListener onTouchListener;

    public TouchableWrapper(Context context) {
        super(context);
    }

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchListener!=null) {
                    onTouchListener.onTouch();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (onTouchListener!=null) {
                    onTouchListener.onRelease();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchListener {
        public void onTouch();

        public void onRelease();
    }
}