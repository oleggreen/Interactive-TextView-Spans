package com.sundesign.text.style;

import android.R;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.widget.TextView;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class InteractiveImageSpan extends DynamicDrawableSpan implements TouchableSpan {

    private boolean mIsSoundEffectEnabled = true;
    private boolean mIsCheckable = true;
    private boolean mIsChecked = false;
    private boolean mIsPressed = false;
    private boolean mIsEnabled = true;
    private Drawable mDrawable;

    /*package*/ InteractiveImageSpan(Drawable drawable) {
        this(drawable, ALIGN_BOTTOM);
    }

    /**
     * @param verticalAlignment one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     *                          {@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    /*package*/ InteractiveImageSpan(Drawable drawable, int verticalAlignment) {
        super(verticalAlignment);
        mDrawable = drawable;
        drawableStateChanged();
    }

//    /**
//     * <b>NOTE!!!: for ImageSpan drawable to be applied: </b><br/>
//     * ImageSpan holder must be invalidated (for ex: TextView.invalidate()).
//     */
//    public void setDrawable(Drawable drawable) {
//
//        mDrawable = drawable;
//        drawableStateChanged();
//
//    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    //TODO remove this hardcode
    static int pressed = R.attr.state_pressed;
    static int unPressed = -R.attr.state_pressed;

    static int checked = R.attr.state_checked;
    static int unChecked = -R.attr.state_checked;

    static int enabled = R.attr.state_enabled;
    static int disabled = -R.attr.state_enabled;

    @Override
    public boolean onTouchEvent(MotionEvent event, TextView widget) {

        if (!mIsEnabled) return false;

        if (mTouchListener != null && mTouchListener.onTouch(widget, event)) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mIsPressed = true;
            Log.d("test", "in normal state, setting pressed state");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mIsPressed = false;

            if (isCheckable()) mIsChecked = !mIsChecked;

            Log.d("test", "in other state, setting normal state + onClick()");
            performClick(widget);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            mIsPressed = false;
            Log.d("test", "in other state, setting normal state");
        }

        drawableStateChanged();
        widget.invalidate();

        return true;
    }

    int[] mDrawableState;

    protected void drawableStateChanged() {

        Drawable d = getDrawable();
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    public final int[] getDrawableState() {

//        if ((mDrawableState != null)) {
//            return mDrawableState;
//        } else {
            mDrawableState = onCreateDrawableState();
//            mPrivateFlags &= ~PFLAG_DRAWABLE_STATE_DIRTY;
            return mDrawableState;
//        }
    }

    private int[] onCreateDrawableState() {

        int[] stateList = new int[3];
        stateList[0] = mIsPressed ? pressed : unPressed;
        stateList[1] = mIsChecked ? checked : unChecked;
        stateList[2] = mIsEnabled ? enabled : disabled;

        return stateList;
    }

    public boolean isSoundEffectsEnabled() {
        return mIsSoundEffectEnabled;
    }

    public void setIsSoundEffectEnabled(boolean isEnabled) {
        mIsSoundEffectEnabled = isEnabled;
    }

    public boolean isPressed() {
        return mIsPressed;
    }

    public boolean isCheckable() {
        return mIsCheckable;
    }

    public void setCheckable(boolean isCheckable) {
        mIsCheckable = isCheckable;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    /**
     * <b>NOTE!!!: if ImageSpan drawable is selector: </b><br/>
     * for ImageSpan to become new state an ImageSpan holder
     * must be invalidated (for ex: TextView.invalidate()).
     */
    public void setEnabled(boolean isEnabled) {

        mIsEnabled = isEnabled;
        drawableStateChanged();
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    /**
     * <b>NOTE!!!: if ImageSpan drawable is selector: </b><br/>
     * for ImageSpan to become new state an ImageSpan holder
     * must be invalidated (for ex: TextView.invalidate()).
     */
    public void setChecked(boolean isChecked) {

        mIsChecked = isChecked;
        drawableStateChanged();
    }

    public boolean performClick(TextView widget) {

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

//    @Override
//    public void updateDrawState(TextPaint ds) {
//        Log.d("test2", "updateDrawState, ds.getTextSize() = " + ds.getTextSize());
//        ds.setTextSize(ds.getTextSize() * 5);
//    }
//
//    @Override
//    public void updateMeasureState(TextPaint ds) {
//        Log.d("test2", "updateMeasureState, ds.getTextSize() = " + ds.getTextSize());
//        ds.setTextSize(ds.getTextSize() * 5);
//    }

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