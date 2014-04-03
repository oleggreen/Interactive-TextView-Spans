package com.marchelo.interactive_span.text.method;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import com.marchelo.interactive_span.MotionEventHelper;
import com.marchelo.interactive_span.text.style.TouchableSpan;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class InteractiveSpanMovementMethod extends LinkMovementMethod {

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new InteractiveSpanMovementMethod();

        return sInstance;
    }

    private static InteractiveSpanMovementMethod sInstance;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {

        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int lineCount = layout.getLineCount();
        int line = layout.getLineForVertical(y);
        int textTopBound = layout.getLineTop(0);
        int textBottomBound = layout.getLineBottom(lineCount - 1);

        int off = layout.getOffsetForHorizontal(line, x);

        Log.d("motion", "onTouchEvent(), line = " + line
                + ", offset = " + off);

        TouchableSpan[] link = {};

        if (event.getY() > textTopBound && event.getY() < textBottomBound) {
            link = buffer.getSpans(off, off, TouchableSpan.class);
        }

        Log.d("motion", "onTouchEvent(), touch event = " + MotionEventHelper.actionToString(event.getAction())
                + ", coords = [" + event.getX() + ", " + event.getY() + "], is link = " + (link.length > 0));

        if (link.length != 0) {

            if (action == MotionEvent.ACTION_UP) {

                if (pressedTouchableSpan != null) {

                    if (link[0] != pressedTouchableSpan) {
                        onCancelEvent(event, widget);
                    } else {
                        pressedTouchableSpan.onTouchEvent(event, widget);
                        pressedTouchableSpan = null;
                    }
                }

            } else if (action == MotionEvent.ACTION_DOWN) {

                if (pressedTouchableSpan != null) {
                    onCancelEvent(event, widget);
                }
                link[0].onTouchEvent(event, widget);
                pressedTouchableSpan = link[0];

            } else if (action == MotionEvent.ACTION_MOVE) {

                if (pressedTouchableSpan != null && pressedTouchableSpan != link[0]) {
                    onCancelEvent(event, widget);
                }

            } else if (action == MotionEvent.ACTION_CANCEL) {

                if (pressedTouchableSpan != null) {
                    onCancelEvent(event, widget);
                }
            }

            return true;
        }

        if (pressedTouchableSpan != null) {

            onCancelEvent(event, widget);
            return true;
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    private void onCancelEvent(MotionEvent event, TextView widget) {

        MotionEvent cancelEvent = MotionEvent.obtain(event);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        pressedTouchableSpan.onTouchEvent(cancelEvent, widget);
        pressedTouchableSpan = null;
    }

    private TouchableSpan pressedTouchableSpan;
}
