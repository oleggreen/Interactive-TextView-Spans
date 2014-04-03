package com.sundesign.interactive_span;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.*;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.sundesign.interactive_span.text.method.InteractiveSpanMovementMethod;
import com.sundesign.interactive_span.text.style.FixedInteractiveImageSpan;
import com.sundesign.interactive_span.text.style.InteractiveImageSpan;
import com.sundesign.interactive_span.text.style.RelativeInteractiveImageSpan;

/**
 * @author Oleg Green
 * @since 14.07.13
 */
public class InteractiveSpanActivity extends Activity {

    public static final String TEXT1 = "Android is a Linux-based operating system designed primarily "
            + "for touchscreen mobile devices such as smartphones and tablet computers. ";
    public static final String TEXT2 = "Here's a link to a developers website: " +
            "link http://developer.android.com ";

    public static final int TEXT_SIZE_CHANGE_STEP = 2;
    public static final int TEXT_SIZE_MIN_VALUE = 6;
    public static final int TEXT_SIZE_MAX_VALUE = 60;

    private TextView mSimpleTextView;
    private TextView mComplexTextView;
    private CheckBox mTestButton;
    private CheckBox mIsEnabledButton;
    private CheckBox mIsCheckedButton;
    private InteractiveImageSpan mImageSpan;
    private InteractiveImageSpan mImageSpan2;
    private InteractiveImageSpan mImageSpan3;

    private int[] drawableIdList = {
            R.drawable.button_text_widget,
            R.drawable.button_text_widget1,
            R.drawable.button_text_widget2,
            R.drawable.button_text_widget3 };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mSimpleTextView = (TextView) findViewById(R.id.TextView);
        mComplexTextView = (TextView) findViewById(R.id.TextView2);
        mTestButton = (CheckBox) findViewById(R.id.test_button);
        mIsCheckedButton = (CheckBox) findViewById(R.id.checkbox_is_checked);
        mIsEnabledButton = (CheckBox) findViewById(R.id.checkbox_is_enabled);

        initEventsListeners();

        initializeTextViews(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        for (int i = 0; i < drawableIdList.length; i++) {
            menu.add("selector " + i).setOnMenuItemClickListener(new MenuItemClickListener(i));
        }
        return true;
    }

    private void initEventsListeners() {

        findViewById(R.id.down_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float textSize = mComplexTextView.getTextSize();
                if (textSize >= TEXT_SIZE_CHANGE_STEP + TEXT_SIZE_MIN_VALUE)
                    mComplexTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            textSize - TEXT_SIZE_CHANGE_STEP);
            }
        });

        findViewById(R.id.up_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float textSize = mComplexTextView.getTextSize();
                if (textSize <= TEXT_SIZE_MAX_VALUE - TEXT_SIZE_CHANGE_STEP) {
                    mComplexTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            textSize + TEXT_SIZE_CHANGE_STEP);
                }
            }
        });

        mIsCheckedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setButtonsChecked(isChecked);
            }
        });

        mIsEnabledButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setButtonsEnable(isChecked);
            }
        });
    }

    private void setButtonsEnable(boolean isEnable) {

        if (mTestButton != null) mTestButton.setEnabled(isEnable);
        if (mImageSpan != null) mImageSpan.setEnabled(isEnable);
        if (mImageSpan2 != null) mImageSpan2.setEnabled(isEnable);
        if (mImageSpan3 != null) mImageSpan3.setEnabled(isEnable);
        if (mComplexTextView != null) mComplexTextView.invalidate();
    }

    private void setButtonsChecked(boolean isChecked) {

        if (mTestButton != null) mTestButton.setChecked(isChecked);
        if (mImageSpan != null) mImageSpan.setChecked(isChecked);
        if (mImageSpan2 != null) mImageSpan2.setChecked(isChecked);
        if (mImageSpan3 != null) mImageSpan3.setChecked(isChecked);
        if (mComplexTextView != null) mComplexTextView.invalidate();
    }

    private void initializeTextViews(int mode) {

        mSimpleTextView.setText(TEXT1);
        Spannable spannable = (Spannable) mSimpleTextView.getText();

        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 13, 24, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(TEXT2 + TEXT1 + TEXT1 + TEXT1);

        mImageSpan = new RelativeInteractiveImageSpan(getDrawable(mode), 2.4f, 5);
        mImageSpan2 = new FixedInteractiveImageSpan(getDrawable(mode), 150, 30);
        mImageSpan3 = new RelativeInteractiveImageSpan(getDrawable(mode), 1.4f, 5);

        mImageSpan.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span1)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span1 clicked");
            }
        });
        mImageSpan2.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span2)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span2 clicked");
            }
        });
        mImageSpan3.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span3)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span3 clicked");
            }
        });

        ssb.setSpan(mImageSpan, 16, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(mImageSpan2, 37, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(mImageSpan3, 437, 438, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        URLSpan urlSpan = new URLSpan("http://developer.android.com") {

            @Override
            public void onClick(View widget) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getURL()));
                startActivity(i);
            }
        };
        ssb.setSpan(urlSpan, 44, 72, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setButtonsChecked(mIsCheckedButton.isChecked());
        setButtonsEnable(mIsEnabledButton.isChecked());
        mComplexTextView.setText(ssb, TextView.BufferType.SPANNABLE);
        mComplexTextView.setSoundEffectsEnabled(true);
        mComplexTextView.setMovementMethod(InteractiveSpanMovementMethod.getInstance());

        mTestButton.setBackgroundResource(getBackgroundDrawableId(mode));
    }

    private Drawable getDrawable(int type) {
        return getResources().getDrawable(getBackgroundDrawableId(type));
    }

    private int getBackgroundDrawableId(int type) {

        if (type >= drawableIdList.length) {
            throw new IllegalArgumentException("no such selector exists");
        }

        return drawableIdList[type];
    }

    private class MenuItemClickListener implements MenuItem.OnMenuItemClickListener {

        private int mSelectorType;

        public MenuItemClickListener(int selectorType) {
            mSelectorType = selectorType;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            initializeTextViews(mSelectorType);
            return false;
        }
    }
}
