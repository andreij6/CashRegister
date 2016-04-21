package com.creativejones.andre.cashregister;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.creativejones.andre.cashregister.app.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.creativejones.andre.cashregister.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void emptyCartShowsMessageHidesRecycler(){
        onView(withId(R.id.main_cart_empty_label))
                .check(matches(isDisplayed()));

        onView(withId(R.id.main_cart_recycler))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void canAddANewProduct(){
        onView(withId(R.id.main_new_product_btn))
                .perform(click());

        onView(withId(R.id.dialog_product_name_et))
                .perform(typeText("Biscuits"));

        onView(withId(R.id.dialog_product_price_et))
                .perform(typeText("9.25"));

        onView(withText("Finish"))
                .perform(click());

        onView(withRecyclerView(R.id.main_all_products_recycler)
                .atPositionOnView(0, R.id.product_item_name))
                .check(matches(withText("Peaches")))
                .check(matches(isDisplayed()));


    }


}
