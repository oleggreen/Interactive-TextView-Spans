package com.sundesign.text.style;

import android.R;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class InteractiveImageSpan extends DynamicDrawableSpan implements TouchableSpan {

    private boolean mIsSoundEffectEnabled = true;
    private Drawable mDrawable;
    //TODO change size when changing text size in TextView
    private int mWidth;
    private int mHeight;

    public InteractiveImageSpan(Drawable drawable, int width, int height) {
        this(drawable, ALIGN_BOTTOM, width, height);
    }

    /**
     * @param verticalAlignment one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     *                          {@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public InteractiveImageSpan(Drawable drawable, int width, int height, int verticalAlignment) {
        super(verticalAlignment);
        mDrawable = drawable;
        mWidth = width;
        mHeight = height;
        mDrawable.setBounds(0, 0, width, height);
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    //TODO remove this hardcode
    static int[] pressed = new int[]{R.attr.state_pressed};
    static int[] notPressed = new int[]{-R.attr.state_pressed};

    @Override
    public boolean onTouchEvent(MotionEvent event, TextView widget) {

        if (mTouchListener != null && mTouchListener.onTouch(widget, event)) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDrawable.setState(pressed);
            Log.d("test", "in normal state, setting pressed state");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mDrawable.setState(notPressed);
            Log.d("test", "in other state, setting normal state + onClick()");
            performClick(widget);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            mDrawable.setState(notPressed);
            Log.d("test", "in other state, setting normal state");
        }
        widget.invalidate();

        return true;
    }

    public boolean isSoundEffectsEnabled() {
        return mIsSoundEffectEnabled;
    }

    public void setIsSoundEffectEnabled(boolean isEnabled) {
        mIsSoundEffectEnabled = isEnabled;
    }

    public boolean performClick(View widget) {

        if (mOnClickListener != null) {
            if (isSoundEffectsEnabled())
                widget.playSoundEffect(SoundEffectConstants.CLICK);
            mOnClickListener.onClick();
            return true;
        }

        return false;
    }

    /**
     * Register a callback to be invoked when this span is clicked.
     *
     * @param listener The callback that will run
     */
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * Register a callback to be invoked when a touch event is sent to this span.
     * @param listener the touch listener to attach to this span
     */
    public void setOnTouchListener(OnTouchListener listener) {
        mTouchListener = listener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        Log.d("test2", "updateDrawState, ds.getTextSize() = " + ds.getTextSize());
        ds.setTextSize(ds.getTextSize() * 5);
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        Log.d("test2", "updateMeasureState, ds.getTextSize() = " + ds.getTextSize());
        ds.setTextSize(ds.getTextSize() * 5);
    }

    private OnClickListener mOnClickListener;
    private OnTouchListener mTouchListener;

    public interface OnClickListener {
        /**
         * Called when a span has been clicked.
         */
        void onClick();
    }

    public interface OnTouchListener {
        /**
         * Called when a touch event is dispatched to a span in TextView. This allows listeners to
         * get a chance to respond before the target span.
         *
         * @param v The TextView the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *        the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        boolean onTouch(TextView v, MotionEvent event);
    }
}