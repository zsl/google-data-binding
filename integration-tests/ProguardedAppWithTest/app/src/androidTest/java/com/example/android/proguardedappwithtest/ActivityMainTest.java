package com.example.android.proguardedappwithtest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ActivityMainTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
            MainActivity.class);
    @Test
    public void testName() throws Throwable {
        final MainActivity activity = mActivityTestRule.getActivity();
        View view = activity.findViewById(R.id.name_text);
        assertThat(view, instanceOf(TextView.class));
        TextView tv = (TextView) view;
        assertThat(tv.getText().toString(), is("foo"));
    }
}
