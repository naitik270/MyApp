package com.demo.nspl.restaurantlite.activity;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.demo.nspl.restaurantlite.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Test
    public void logInActivityTest() {
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.edt_mobile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_user_id),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.edt_mobile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_user_id),
                                        0),
                                0)));
        textInputEditText2.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.edt_mobile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_user_id),
                                        0),
                                0)));
        textInputEditText3.perform(scrollTo(), click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.edt_mobile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_user_id),
                                        0),
                                0)));
        textInputEditText4.perform(scrollTo(), replaceText("8980442840"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.edt_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0)));
        textInputEditText5.perform(scrollTo(), replaceText("09876"), closeSoftKeyboard());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.chk_remember), withText("REMEMBER ME"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0)));
        appCompatCheckBox.perform(scrollTo(), click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction checkableImageButton = onView(
                allOf(withId(R.id.text_input_end_icon), withContentDescription("Show password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                2)));
        checkableImageButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.edt_password), withText("09876"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0)));
        textInputEditText6.perform(scrollTo(), replaceText("09875"));

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.edt_password), withText("09875"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.edt_password), withText("09875"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0)));
        textInputEditText8.perform(scrollTo(), replaceText("09865"));

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.edt_password), withText("09865"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_login), withText("LOGIN"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.edt_password), withText("09865"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0)));
        textInputEditText10.perform(scrollTo(), click());

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.edt_password), withText("09865"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0)));
        textInputEditText11.perform(scrollTo(), replaceText("09865"));

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.edt_password), withText("09865"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.input_password),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText12.perform(closeSoftKeyboard());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
