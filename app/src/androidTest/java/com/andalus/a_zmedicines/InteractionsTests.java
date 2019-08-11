package com.andalus.a_zmedicines;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;


import com.andalus.a_zmedicines.Screens.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static  android.support.test.espresso.Espresso.* ;
import  android.support.test.espresso.matcher.* ;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.* ;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import  static android.support.test.espresso.matcher.ViewMatchers.withText ;
import static android.support.test.espresso.action.ViewActions.click ;




/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InteractionsTests {

    private CountingIdlingResource  mIdlingResource ;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTest = new ActivityTestRule<>(MainActivity.class) ;


    @Test
    public void allTests() {

       onView(withId(R.id.nav_interaactions)).perform(click()) ;




    }



}
