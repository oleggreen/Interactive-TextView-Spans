package com.sundesign.text.style;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class RelativeInteractiveImageSpan extends InteractiveImageSpan {

    private float mToTextProportion;
    private float mWidthHeightProportion;

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
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        Rect drawableBounds = new Rect();

        drawableBounds.left = 0;
        drawableBounds.top = 0;
        drawableBounds.bottom = Math.round(paint.getTextSize() * mToTextProportion);
        drawableBounds.right = Math.round(drawableBounds.bottom * mWidthHeightProportion);

        getDrawable().setBounds(drawableBounds);

        return super.getSize(paint, text, start, end, fm);
    }
}