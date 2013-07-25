package com.sundesign;

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
import android.view.View;
import android.widget.*;

import com.sundesign.text.method.InteractiveSpanMovementMethod;
import com.sundesign.text.style.FixedInteractiveImageSpan;
import com.sundesign.text.style.InteractiveImageSpan;
import com.sundesign.text.style.RelativeInteractiveImageSpan;

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

    private TextView mTextView;
    private TextView mTextView2;
    private CheckBox mTestButton;
    private InteractiveImageSpan mImageSpan;
    private InteractiveImageSpan mImageSpan2;
    private InteractiveImageSpan mImageSpan3;
    private int selectorType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mTextView = (TextView) findViewById(R.id.TextView);
        mTextView2 = (TextView) findViewById(R.id.TextView2);
        mTestButton = (CheckBox) findViewById(R.id.test_button);
        findViewById(R.id.change_selector).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectorType++;
                if (selectorType >= 4) selectorType = 0;
                ((Button)v).setText("selector = " + selectorType);
                initializeTextViews(selectorType);
            }
        });

        findViewById(R.id.down_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float textSize = mTextView2.getTextSize();
                if (textSize >= TEXT_SIZE_CHANGE_STEP + TEXT_SIZE_MIN_VALUE)
                    mTextView2.setTextSize(textSize - TEXT_SIZE_CHANGE_STEP);
            }
        });

        findViewById(R.id.up_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float textSize = mTextView2.getTextSize();
                if (textSize <= TEXT_SIZE_MAX_VALUE - TEXT_SIZE_CHANGE_STEP)
                    mTextView2.setTextSize(textSize + TEXT_SIZE_CHANGE_STEP);
            }
        });

        ((CheckBox)findViewById(R.id.checkbox_is_checked))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setButtonsChecked(isChecked);
            }
        });

        ((CheckBox)findViewById(R.id.checkbox_is_enabled))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        setButtonsEnable(isChecked);
                    }
                });

        initializeTextViews(selectorType);

        setButtonsChecked(false);
        setButtonsEnable(true);
    }

    private void setButtonsEnable(boolean isEnable) {

        if (mTestButton != null) mTestButton.setEnabled(isEnable);
        if (mImageSpan != null) mImageSpan.setEnabled(isEnable);
        if (mImageSpan2 != null) mImageSpan2.setEnabled(isEnable);
        if (mImageSpan3 != null) mImageSpan3.setEnabled(isEnable);
    }

    private void setButtonsChecked(boolean isChecked) {

        if (mTestButton != null) mTestButton.setChecked(isChecked);
        if (mImageSpan != null) mImageSpan.setChecked(isChecked);
        if (mImageSpan2 != null) mImageSpan2.setChecked(isChecked);
        if (mImageSpan3 != null) mImageSpan3.setChecked(isChecked);
    }

    private void initializeTextViews(int mode) {

        mTextView.setText(TEXT1);
        Spannable spannable = (Spannable) mTextView.getText();

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
        mTextView2.setText(ssb, TextView.BufferType.SPANNABLE);
        mTextView2.setSoundEffectsEnabled(true);
        mTextView2.setMovementMethod(InteractiveSpanMovementMethod.getInstance());

        mTestButton.setBackgroundResource(getBackgroundDrawableId(mode));

        Toast.makeText(getApplicationContext(), "text size = " + mTextView2.getTextSize(),
                Toast.LENGTH_SHORT).show();
    }

    private Drawable getDrawable(int type) {
        return getResources().getDrawable(getBackgroundDrawableId(type));
    }

    private int getBackgroundDrawableId(int type) {

        switch(type) {
            case 0: default: return R.drawable.button_text_widget;
            case 1:         return R.drawable.button_text_widget1;
            case 2:         return R.drawable.button_text_widget2;
            case 3:         return R.drawable.button_text_widget3;
        }
    }
}
