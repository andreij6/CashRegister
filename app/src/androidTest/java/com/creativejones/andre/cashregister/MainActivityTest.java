package com.creativejones.andre.cashregister;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.creativejones.andre.cashregister.app.MainActivity;
import com.creativejones.andre.cashregister.data.FileDatabase;
import com.creativejones.andre.cashregister.utils.ValidationConstants;

import org.junit.Before;
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

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        FileDatabase database = new FileDatabase(mActivityTestRule.getActivity());

        database.clear();
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

        new MainActivityViewActions()
                .ClickNewProductBtn()
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

        new MainActivityViewActions()
                .ClickNewProductBtn()
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

        new MainActivityViewActions()
                .ClickNewProductBtn()
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

        new MainActivityViewActions()
                .ClickNewProductBtn()
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

        new MainActivityViewActions()
                .ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.NAME_EMPTY);

    }

    @Test
    public void attemptToAddProductWithExistingNameFails(){
        String productName = "Peaches";
        String productPrice = "9.25";

        new MainActivityViewActions()
                .ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.NAME_EXISTS);

    }

    @Test
    public void attemptToAddProductWithNoPriceFails(){
        String productName = "Brownie";
        String productPrice = "";

        new MainActivityViewActions()
                .ClickNewProductBtn()
                .TypeProductName(productName)
                .TypeProductPrice(productPrice)
                .FinishAddNewProduct()
                .CheckValidationPrompt(mActivityTestRule.getActivity(), ValidationConstants.PRICE_EMPTY);

    }
    //endregion

    @Test
    public void canAddProductToCart(){
        new MainActivityViewActions()
                .AddProductToCart(1)
                .AddProductToCart(3)
                .CheckCartItemName(0, "Apples")
                .CheckCartItemName(1, "Bacon");
    }

    @Test(expected = Exception.class)
    public void attemptAddSameProductToCartFails(){
        new MainActivityViewActions()
                .AddProductToCart(16);
    }

    @Test
    public void canRemoveProductFromCart(){
        new MainActivityViewActions()
                .AddProductToCart(1)
                .AddProductToCart(3)
                .RemoveCartItem(0)
                .CheckCartItemName(0, "Bacon");
    }

    @Test
    public void removingAllCartItemsShowsEmptyCartMessage(){
        new MainActivityViewActions()
                .AddProductToCart(1)
                .AddProductToCart(3)
                .RemoveCartItem(0)
                .RemoveCartItem(0);

        CheckEmptyCartViews();
    }

    @Test
    public void checkoutShowTotalPrice(){

        new MainActivityViewActions()
                .AddProductToCart(1)
                .AddProductToCart(3)
                .CheckCartItemName(0, "Apples")
                .CheckCartItemName(1, "Bacon")
                .Checkout()
                .PriceShouldBe("Total: $9.86");
    }

    //region Helpers
    private void CheckEmptyCartViews() {
        onView(withId(R.id.main_cart_empty_label))
                .check(matches(isDisplayed()));

        onView(withId(R.id.main_cart_recycler))
                .check(matches(not(isDisplayed())));
    }
    //endregion
}
