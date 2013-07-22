package com.sundesign;

import android.app.ActionBar;
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
            + "for touchscreen mobile devices such as smartphones and tablet computers";
    public static final String TEXT2 = "Here's a link to a developers website: " +
            "link http://developer.android.com";
    private TextView mTextView;
    private TextView mTextView2;
    private Button mTestButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActionBar()
                .getThemedContext(), R.array.my_menu_spinner_list, android.R.layout.simple_spinner_dropdown_item);
        ActionBar.OnNavigationListener mNavigationCallback = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                Toast.makeText(getApplicationContext(), "onItemSelected, pos = " + itemPosition,
                        Toast.LENGTH_SHORT).show();

                initializeTextViews(itemPosition);

                return true;
            }
        };

        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mNavigationCallback);

        mTextView = (TextView) findViewById(R.id.TextView);
        mTextView2 = (TextView) findViewById(R.id.TextView2);
        mTestButton = (Button) findViewById(R.id.test_button);
    }

    private void initializeTextViews(int mode) {

        mTextView.setText(TEXT1);
        Spannable spannable = (Spannable) mTextView.getText();

        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, 13, 24, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(TEXT2 + TEXT1 + TEXT1 + TEXT1);

        InteractiveImageSpan imageSpan =
                new RelativeInteractiveImageSpan(getDrawable(mode), 2.4f, 5);
        InteractiveImageSpan imageSpan2 =
                new FixedInteractiveImageSpan(getDrawable(mode), 150, 30);
        InteractiveImageSpan imageSpan3 =
                new RelativeInteractiveImageSpan(getDrawable(mode), 1.4f, 5);

        imageSpan.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span1)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span1 clicked");
            }
        });
        imageSpan2.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span2)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span2 clicked");
            }
        });
        imageSpan3.setOnClickListener(new InteractiveImageSpan.OnClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(getApplicationContext(),
                        "hello from click method(span3)", Toast.LENGTH_SHORT).show();
                Log.d("test", "span3 clicked");
            }
        });

        ssb.setSpan(imageSpan, 16, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(imageSpan2, 37, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(imageSpan3, 437, 438, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            case 0: return R.drawable.button_text_widget;
            case 1: return R.drawable.button_checked_disabled;
            case 2: return R.drawable.button_unchecked_disabled;
            case 3: default: return R.drawable.button_unpressed_unchecked;
        }
    }
}
