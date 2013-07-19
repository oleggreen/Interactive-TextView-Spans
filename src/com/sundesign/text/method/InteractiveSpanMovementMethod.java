package com.sundesign.text.method;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import com.sundesign.MotionEventHelper;
import com.sundesign.text.style.TouchableSpan;

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

        Log.d("test" , "onTouchEvent(), touch event = " + MotionEventHelper.actionToString(event.getAction())
                + ", coords = [" + event.getX() + ", " + event.getY() + "]");

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = buffer.getSpans(off, off, TouchableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {

//                    if (link[0] != currentImageSpan && currentImageSpan != null) {
//
//                        MotionEvent cancelEvent = MotionEvent.obtain(event);
//                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
//                        currentImageSpan.onTouchEvent(cancelEvent, widget);
//                        currentImageSpan = null;
//                    }
                    link[0].onTouchEvent(event, widget);

                } else if (action == MotionEvent.ACTION_DOWN) {

                    link[0].onTouchEvent(event, widget);
//                    currentImageSpan = link[0];
                }

                return true;
            } else {

//                if (currentImageSpan != null) {
//                    currentImageSpan.onTouchEvent(event, widget);
//                    currentImageSpan = null;
//                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

//    private InteractiveImageSpan currentImageSpan;
//    @Override
//    protected boolean up(TextView widget, Spannable buffer) {
//        Log.d("test" , "up, buffer = " + buffer.toString());
//        if (action(UP, widget, buffer)) {
//            return true;
//        }
//
//        return super.up(widget, buffer);
//    }
//
//    @Override
//    protected boolean down(TextView widget, Spannable buffer) {
//        Log.d("test" , "down, buffer = " + buffer.toString());
//        if (action(DOWN, widget, buffer)) {
//            return true;
//        }
//
//        return super.down(widget, buffer);
//    }
//
//    @Override
//    protected boolean left(TextView widget, Spannable buffer) {
//        Log.d("test" , "left, buffer = " + buffer.toString());
//        if (action(UP, widget, buffer)) {
//            return true;
//        }
//
//        return super.left(widget, buffer);
//    }
//
//    @Override
//    protected boolean right(TextView widget, Spannable buffer) {
//        Log.d("test" , "right, buffer = " + buffer.toString());
//        if (action(DOWN, widget, buffer)) {
//            return true;
//        }
//
//        return super.right(widget, buffer);
//    }
//
//    private boolean action(int what, TextView widget, Spannable buffer) {
//
//        Log.d("test" , "action, buffer = " + buffer.toString());
//
//        Layout layout = widget.getLayout();
//
//        int padding = widget.getTotalPaddingTop() +
//                widget.getTotalPaddingBottom();
//        int areatop = widget.getScrollY();
//        int areabot = areatop + widget.getHeight() - padding;
//
//        int linetop = layout.getLineForVertical(areatop);
//        int linebot = layout.getLineForVertical(areabot);
//
//        int first = layout.getLineStart(linetop);
//        int last = layout.getLineEnd(linebot);
//
//        ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);
//
//        int a = Selection.getSelectionStart(buffer);
//        int b = Selection.getSelectionEnd(buffer);
//
//        int selStart = Math.min(a, b);
//        int selEnd = Math.max(a, b);
//
//        if (selStart < 0) {
//            if (buffer.getSpanStart(FROM_BELOW) >= 0) {
//                selStart = selEnd = buffer.length();
//            }
//        }
//
//        if (selStart > last)
//            selStart = selEnd = Integer.MAX_VALUE;
//        if (selEnd < first)
//            selStart = selEnd = -1;
//
//        switch (what) {
//            case CLICK:
//                if (selStart == selEnd) {
//                    return false;
//                }
//
//                ClickableSpan[] link = buffer.getSpans(selStart, selEnd, ClickableSpan.class);
//
//                if (link.length != 1)
//                    return false;
//
//                link[0].onClick(widget);
//                break;
//
//            case UP:
//                int beststart, bestend;
//
//                beststart = -1;
//                bestend = -1;
//
//                for (int i = 0; i < candidates.length; i++) {
//                    int end = buffer.getSpanEnd(candidates[i]);
//
//                    if (end < selEnd || selStart == selEnd) {
//                        if (end > bestend) {
//                            beststart = buffer.getSpanStart(candidates[i]);
//                            bestend = end;
//                        }
//                    }
//                }
//
//                if (beststart >= 0) {
//                    Selection.setSelection(buffer, bestend, beststart);
//                    return true;
//                }
//
//                break;
//
//            case DOWN:
//                beststart = Integer.MAX_VALUE;
//                bestend = Integer.MAX_VALUE;
//
//                for (int i = 0; i < candidates.length; i++) {
//                    int start = buffer.getSpanStart(candidates[i]);
//
//                    if (start > selStart || selStart == selEnd) {
//                        if (start < beststart) {
//                            beststart = start;
//                            bestend = buffer.getSpanEnd(candidates[i]);
//                        }
//                    }
//                }
//
//                if (bestend < Integer.MAX_VALUE) {
//                    Selection.setSelection(buffer, beststart, bestend);
//                    return true;
//                }
//
//                break;
//        }
//
//        return false;
//    }
}
