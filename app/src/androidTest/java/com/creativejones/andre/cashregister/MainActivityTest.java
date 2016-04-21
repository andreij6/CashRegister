package com.creativejones.andre.cashregister;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.creativejones.andre.cashregister.app.MainActivity;
import com.creativejones.andre.cashregister.data.FileDatabase;
import com.creativejones.andre.cashregister.utils.ValidationConstants;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static MainActivityViewActions Actions;
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setupClass(){
        Actions = new MainActivityViewActions();
    }

    @Before
    public void setup(){
        FileDatabase database = new FileDatabase(mActivityTestRule.getActivity());

        database.clear();
    }

    @AfterClass
    public static void tearDown(){
        Actions = null;
    }

    @Test
    public void emptyCartShowsMessageHidesRecycler(){
        CheckEmptyCartViews();
    }

    //region Create Product Tests
    @Test
    public void canAddANewProduct(){
        String productName = "Biscuits";
        String productPrice = "9.25";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .AssertRecyclerItemNameEqual(4, productName)
                .AssertRecyclerItemPriceEqual(4, "$" + productPrice);
    }

    @Test
    public void canAddNewProductWithValidCustomProductCode(){
        String productName = "Biscuits";
        String productPrice = "9.25";
        String validProductCode = "22eeUUii22eeUUii";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .ClickManualProductCodeButton()
                .TypeProductCode(validProductCode)
                .FinishAddNewProduct()
                .AssertRecyclerItemNameEqual(4, productName)
                .AssertRecyclerItemPriceEqual(4, "$" + productPrice);
    }

    @Test
    public void attemptToAddProductWithShortCodeFails(){
        String productName = "Biscuits";
        String productPrice = "9.25";
        String validProductCode = "22eeUUii";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .ClickManualProductCodeButton()
                .TypeProductCode(validProductCode)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.CODE_MISMATCH);
    }

    @Test
    public void attemptToAddProductWithNonAlphaNumericCharactersInCodeFails(){
        String productName = "Biscuits";
        String productPrice = "9.25";
        String validProductCode = "22eeUUi@22eeUUi$";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .ClickManualProductCodeButton()
                .TypeProductCode(validProductCode)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.CODE_MISMATCH);

    }

    @Test
    public void attemptToAddProductWithNoNameFails(){
        String productName = "";
        String productPrice = "9.25";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.NAME_EMPTY);

    }

    @Test
    public void attemptToAddProductWithExistingNameFails(){
        String productName = "Peaches";
        String productPrice = "9.25";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.NAME_EXISTS);

    }

    @Test
    public void attemptToAddProductWithNoPriceFails(){
        String productName = "Brownie";
        String productPrice = "";

        Actions.ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.PRICE_EMPTY);

    }
    //endregion

    @Test
    public void canAddProductToCart(){
        Actions.AddProductToCart(1)
                .AddProductToCart(3)
                .CheckCartItemName(0, "Apples")
                .CheckCartItemName(1, "Bacon");
    }

    @Test(expected = Exception.class)
    public void attemptAddSameProductToCartFails(){
        Actions.AddProductToCart(16);
    }

    @Test
    public void canRemoveProductFromCart(){
        Actions.AddProductToCart(1)
                .AddProductToCart(3)
                .RemoveCartItem(0)
                .CheckCartItemName(0, "Bacon");
    }

    @Test
    public void removingAllCartItemsShowsEmptyCartMessage(){
        Actions.AddProductToCart(1)
                .AddProductToCart(3)
                .RemoveCartItem(0)
                .RemoveCartItem(0);

        CheckEmptyCartViews();
    }

    @Test
    public void checkoutShowTotalPrice(){

        Actions.AddProductToCart(1)
                .AddProductToCart(3)
                .CheckCartItemName(0, "Apples")
                .CheckCartItemName(1, "Bacon")
                .Checkout()
                .PriceShouldBe("Total: $9.86");
    }

    @Test
    public void cartItemsStayAfterRotate(){
        Actions.AddProductToCart(1)
                .AddProductToCart(3)
                .CheckCartItemName(0, "Apples")
                .CheckCartItemName(1, "Bacon");

        rotateScreen();

        Actions.CheckCartItemName(0, "Apples").CheckCartItemName(1, "Bacon");
    }

    //region Helpers
    private void CheckEmptyCartViews() {
        onView(withId(R.id.main_cart_empty_label))
                .check(matches(isDisplayed()));

        onView(withId(R.id.main_cart_recycler))
                .check(matches(not(isDisplayed())));
    }

    public void rotateScreen(){
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        Activity activity = mActivityTestRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    //endregion
}
