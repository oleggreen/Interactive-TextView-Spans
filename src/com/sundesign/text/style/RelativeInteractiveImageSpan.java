package com.sundesign.text.style;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class RelativeInteractiveImageSpan extends InteractiveImageSpan {

    public static final int DEFAULT_TEXT_SIZE = 1;

    private float mToTextProportion;
    private float mWidthHeightProportion;
    private float mTextSize = DEFAULT_TEXT_SIZE;

    public RelativeInteractiveImageSpan(Drawable drawable, float toTextProportion,
                                        float widthHeightProportion) {
        this(drawable, toTextProportion, widthHeightProportion, ALIGN_BOTTOM);
    }

    /**
     * @param verticalAlignment one of {@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM} or
     *                          {@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public RelativeInteractiveImageSpan(Drawable drawable, float toTextProportion,
                                        float widthHeightProportion, int verticalAlignment) {

        super(drawable, verticalAlignment);
        mWidthHeightProportion = widthHeightProportion;
        mToTextProportion = toTextProportion;
        setDrawableBounds();
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        mTextSize = paint.getTextSize();
        setDrawableBounds();

        return super.getSize(paint, text, start, end, fm);
    }

    private void setDrawableBounds() {
        getDrawable().setBounds(getDrawableRect());
    }

    private Rect getDrawableRect() {

        Rect drawableBounds = new Rect();

        drawableBounds.left = 0;
        drawableBounds.top = 0;
        drawableBounds.bottom = Math.round(mTextSize * mToTextProportion);
        drawableBounds.right = Math.round(drawableBounds.bottom * mWidthHeightProportion);
        return drawableBounds;
    }
}