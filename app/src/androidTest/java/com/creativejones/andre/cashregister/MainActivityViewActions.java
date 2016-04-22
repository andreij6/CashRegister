package com.creativejones.andre.cashregister;


import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.contrib.RecyclerViewActions;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.creativejones.andre.cashregister.TestUtils.withRecyclerView;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;

class MainActivityViewActions {


    public MainActivityViewActions ClickNewProductBtn() {
        onView(withId(R.id.main_new_product_btn))
                .perform(click());

        return this;
    }

    public MainActivityViewActions TypeProductName(String productName) {
        onView(withId(R.id.dialog_product_name_et))
                .perform(typeText(productName));
        return this;
    }

    public MainActivityViewActions TypeProductPrice(String productPrice) {
        onView(withId(R.id.dialog_product_price_et))
                .perform(typeText(productPrice));
        return this;
    }

    public MainActivityViewActions FinishAddNewProduct() {
        onView(withText("Finish"))
                .perform(click());
        return this;
    }

    public MainActivityViewActions AssertRecyclerItemNameEqual(int position, String productName) {
        onView(withRecyclerView(R.id.main_all_products_recycler)
                .atPositionOnView(position, R.id.product_item_name))
                .check(matches(withText(productName)));
        return this;
    }

    public MainActivityViewActions AssertRecyclerItemPriceEqual(int position, String formattedPrice) {
        onView(withRecyclerView(R.id.main_all_products_recycler)
                .atPositionOnView(position, R.id.product_item_price))
                .check(matches(withText(formattedPrice)));
        return this;
    }

    public MainActivityViewActions ClickManualProductCodeButton() {
        onView(withId(R.id.dialog_product_code_btn))
                .perform(click());

        return this;
    }

    public MainActivityViewActions TypeProductCode(String productCode) {
        onView(withId(R.id.dialog_product_code_et))
                .perform(typeText(productCode));

        return this;
    }

    public MainActivityViewActions CheckValidationPrompt(Activity activity, String message) {
        onView(withText(message))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        return this;
    }

    public MainActivityViewActions AddProductToCart(int position) {
        onView(withId(R.id.main_all_products_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, InnerViewAction.clickChildViewWithId(R.id.product_item_action_btn)));

        return this;
    }

    public MainActivityViewActions CheckCartItemName(int position, String itemName) {
        onView(withRecyclerView(R.id.main_cart_recycler)
                .atPositionOnView(position, R.id.product_item_name))
                .check(matches(withText(itemName)));

        return this;
    }

    public MainActivityViewActions Checkout() {
        onView(withId(R.id.main_checkout_button))
                .perform(click());

        return this;
    }

    public MainActivityViewActions PriceShouldBe(String formattedPrice) {
        onView(withText(formattedPrice))
                .check(matches(isDisplayed()));

        return this;
    }

    public MainActivityViewActions RemoveCartItem(int position) {
        onView(withId(R.id.main_cart_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, InnerViewAction.clickChildViewWithId(R.id.product_item_action_btn)));

        return this;
    }
}
