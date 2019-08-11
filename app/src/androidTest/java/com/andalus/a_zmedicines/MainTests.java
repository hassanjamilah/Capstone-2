package com.andalus.a_zmedicines;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;


import com.andalus.a_zmedicines.Screens.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static  android.support.test.espresso.Espresso.* ;
import  android.support.test.espresso.matcher.* ;
import android.support.v7.widget.SearchView;
import android.view.View;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.* ;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import  static android.support.test.espresso.matcher.ViewMatchers.withText ;
import static android.support.test.espresso.action.ViewActions.click ;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainTests {

    private CountingIdlingResource  mIdlingResource ;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTest = new ActivityTestRule<>(MainActivity.class) ;


    @Test
    public void allTests() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        mIdlingResource = mMainActivityTest.getActivity().getCountingIdlingResource() ;

        Espresso.registerIdlingResources(mIdlingResource) ;


        onView(withRecyclerView(R.id.mainActivity_recycerView)
        .atPositionOnView(0 , R.id.drugListItem_DrugName_TextView))
                .check(matches(withText("Panadol"))) ;

        onView(withId(R.id.mainActivity_recycerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0 , click())) ;

        onView(withId(R.id.drugDetailsFragment_details_TextView)).check(matches( withSubstring("Panadol"))) ;
        onView(withId(R.id.drugDetailsFragment_details_TextView)).check(matches( withSubstring("acetaminophen"))) ;
        onView(withId(R.id.drugDetailsFragment_details_TextView)).check(matches( withSubstring("fever reducer"))) ;
        onView(withId(R.id.nav_interaactions)).perform(click()) ;

        onView(withId(R.id.interactionsFragment_FirstDrug_SearchView)).perform(click());
        onView(withId(R.id.interactionsFragment_FirstDrug_SearchView)).perform(typeText("Panadol")) ;
        onView(withId(android.support.design.R.id.search_button)).perform(click());
        onView(allOf(withId(android.support.design.R.id.search_button))).perform(click());

        //onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("something"));
typeSearchViewText("hassan")  ;
        //onView(withId(R.id.interactionsFragment_search_RecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0 , click())) ;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // onView(withId(R.id.nav_screen)).perform(click()) ;
        //onView(withId(R.id.nav_interaactions)).perform(click()) ;
//        onView(withId(R.id.interactionsFragment_FirstDrug_SearchView)).perform(typeText("Panadol")) ;

        //onView(withId(R.id.firstDrug)).check(matches(isDisplayed()) );
      //  onView(withId(R.id.interactionsFragment_InterActionDetails_TextView)).perform(click()) ;

    }





    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }
        };
    }


}
