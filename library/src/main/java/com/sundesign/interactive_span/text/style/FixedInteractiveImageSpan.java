package com.sundesign.interactive_span.text.style;

import android.graphics.drawable.Drawable;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class FixedInteractiveImageSpan extends InteractiveImageSpan {

    public FixedInteractiveImageSpan(Drawable drawable, int width, int height) {
        this(drawable, width, height, ALIGN_BOTTOM);
    }

    /**
     * @param verticalAlignment one of {@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM} or
     *                          {@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public FixedInteractiveImageSpan(Drawable drawable, int width, int height, int verticalAlignment) {

        super(drawable, verticalAlignment);
        getDrawable().setBounds(0, 0, width, height);
    }
}