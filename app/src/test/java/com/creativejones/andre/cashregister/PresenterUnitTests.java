package com.creativejones.andre.cashregister;

import com.creativejones.andre.cashregister.entities.Product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class PresenterUnitTests {
    private static PresenterFixture FIXTURE;

    @Before
    public void setup(){
        FIXTURE = new PresenterFixture();
    }

    @After
    public void tearDown(){
        FIXTURE = null;
    }

    @Test
    public void checkOutCalculatesCorrectPrice(){
        HashMap<String, Double> testCollection = buildMapWithValidData();

        FIXTURE.SUTTester
                .Code_TotalPrice_OutputShouldMatchOnCheckout(testCollection);
    }

    @Test
    public void checkOutShouldCalculateCorrectPriceWhenProductNotFoundForUUID(){
        HashMap<String, Double> testCollection = buildMapWithInValidData();

        FIXTURE.SUTTester
                .Code_TotalPrice_OutputShouldMatchOnCheckout(testCollection);
    }

    @Test
    public void shouldReturnProductsInDatabase(){
        ArrayList<Product> expected = getDummyData();

        FIXTURE.SUTTester.ListShouldMatchDatabase(expected);
    }

    @Test
    public void shouldReturnEmptyCartWhenNoProductsAdded(){
        FIXTURE.SUTTester.CartShouldBeEmpty();
    }

    @Test
    public void shouldCreateNewProductWithValidNamePriceInputs() throws Exception{
        String name = "Brownie";
        String price = "2.95";

        FIXTURE.SUTTester
                 .CreateProduct(name, price)
                 .CheckProductInDatabase(name, price);
    }

    @Test(expected = InvalidInputException.class)
    public void failsWhenAttemptingToCreateProductWithTooShortCode() throws InvalidInputException{
        String name = "Brownie";
        String price = "2.95";
        String code = "NotValid";

        FIXTURE.SUTTester
                .CreateProduct(name, price, code);
    }

    @Test(expected = InvalidInputException.class)
    public void failsWhenAttemptingToCreateProductWithNonAlphaNumericCharacters() throws InvalidInputException {
        String name = "Brownie";
        String price = "2.95";
        String code = "u@uuuuuu$uuuu^uu";

        FIXTURE.SUTTester
                .CreateProduct(name, price, code);
    }

    @Test
    public void shouldCreateProductWithValidCode() throws Exception{
        String name = "Brownie";
        String price = "2.95";
        String code = "uuuuuuuuuuuuu855";

        FIXTURE.SUTTester
                    .CreateProduct(name, price, code)
                    .DatabaseShouldContainProduct(name, price);
    }

    @Test
    public void shouldReturnEmptyCodeStringWhenNoItemsInCart(){
        FIXTURE.SUTTester.CodeStringShouldBeEmpty();
    }

    @Test
    public void shouldContainCorrectProductsInCodeString(){
        ArrayList<Product> allProducts = getDummyData();

        FIXTURE.SUTTester
                .AddProductsToCart(allProducts)
                .CodeStringContains("UUID7")
                .CodeStringContains("UUID4")
                .CodeStringContains("UUID5")
                .CodeStringContains("UUID6");
    }

    @Test
    public void shouldContainCorrectProductsInCodeStringAfterRemovingProducts(){
        ArrayList<Product> allProducts = getDummyData();

        FIXTURE.SUTTester
                .AddProductsToCart(allProducts)
                .CodeStringContains("UUID7")
                .CodeStringContains("UUID4")
                .RemoveProduct(allProducts.get(1)) //Apples
                .RemoveProduct(allProducts.get(0))
                .CodeStringDoesNotContain("UUID7")
                .CodeStringDoesNotContain("UUID4");
    }

    @Test
    public void cartShouldContainProducts(){
        ArrayList<Product> allProducts = getDummyData();

        FIXTURE.SUTTester
                .AddProductsToCart(allProducts)
                .MatchProductsInCart(allProducts);
    }

    @Test
    public void cartShouldContainProductsAfterRemovingSomeProducts(){
        ArrayList<Product> allProducts = getDummyData();

        FIXTURE.SUTTester
                .AddProductsToCart(allProducts)
                .RemoveProduct(allProducts.get(0))
                .MatchProductsInCart(allProducts.subList(1, 4));
    }

    @Test
    public void attemptingToAddProductWithSameNameAsExistingProductShouldFail(){
        ArrayList<Product> allProducts = getDummyData();

        FIXTURE.SUTTester
                .AddProductsToCart(allProducts)
                .AddProductsToCart(allProducts)
                .MatchProductsInCart(allProducts);
    }

    //region Helpers
    private HashMap<String, Double> buildMapWithValidData() {
        double taxRate = 0.0875;

        HashMap<String, Double> map = new HashMap<>();
        map.put("UUID7;UUID4;UUID5", 10.2955 + (10.2955 * taxRate));
        map.put("UUID7;UUID4", 6.8755 + (6.8755 * taxRate));
        map.put("UUID5;UUID4", 4.8755 + (4.8755 * taxRate));
        map.put("UUID6;UUID5", 11.82 + (11.82 * taxRate));

        return map;
    }

    private HashMap<String, Double> buildMapWithInValidData() {
        double taxRate = 0.0875;

        HashMap<String, Double> map = new HashMap<>();
        map.put("UUID7;UUID4;UUID5;DSAFAS", 10.2955 + (10.2955 * taxRate));
        map.put("UUID7;UUID4;ASDFSA", 6.8755 + (6.8755 * taxRate));
        map.put("UUID5;UUID4;DDD", 4.8755 + (4.8755 * taxRate));
        map.put("UUID6;UUID5;535353", 11.82 + (11.82 * taxRate));
        return map;
    }

    private static ArrayList<Product> getDummyData() {
        ArrayList<Product> dataSource = new ArrayList<>();

        dataSource.add(new Product("Peaches", "UUID7", "5.42"));
        dataSource.add(new Product("Apples", "UUID4", "1.4555"));
        dataSource.add(new Product("Corn", "UUID5", "3.42"));
        dataSource.add(new Product("Bacon", "UUID6", "8.4"));

        return dataSource;
    }
    //endregion
}
