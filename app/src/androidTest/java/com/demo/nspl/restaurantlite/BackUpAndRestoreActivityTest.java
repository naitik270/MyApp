package com.demo.nspl.restaurantlite;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.demo.nspl.restaurantlite.activity.LogInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class BackUpAndRestoreActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> activityRule
            = new ActivityTestRule<>(LogInActivity.class);



    @Test
    public void GDriveBackUpTest() throws Exception {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

//        assertEquals("com.demo.nspl.restaurantlite", appContext.getPackageName());

//        assertNull(ClsGlobal.SetDefaultBackupSettings(appContext));

//        assertEquals("WI-FI OR CELLULAR",ClsGlobal.SetDefaultBackupSettings(appContext));
        onView(withId(R.id.btn_login)).perform(click());
//        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));



    }

}
