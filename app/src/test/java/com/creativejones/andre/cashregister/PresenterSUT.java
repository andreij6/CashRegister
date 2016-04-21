package com.creativejones.andre.cashregister;

import com.creativejones.andre.cashregister.data.IProductRepository;
import com.creativejones.andre.cashregister.models.Product;
import com.creativejones.andre.cashregister.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PresenterSUT {

    private CashRegister.Presenter SUT;

    public PresenterSUT(){
        SUT = new MainPresenter(MockDatabase());
        SUT.initializeCart(null);
    }

    public PresenterSUT CreateProduct(String name, String price, String code) throws InvalidInputException {
        SUT.createProduct(name, price, code);
        return this;
    }

    public PresenterSUT CreateProduct(String name, String price) throws InvalidInputException {
        SUT.createProduct(name, price);
        return this;
    }

    public PresenterSUT DatabaseShouldContainProduct(String name, String price) throws Exception{
        ArrayList<Product> actual = SUT.getAllProduct();

        assertProductInListByName(name, price, actual);

        return this;
    }

    public PresenterSUT CodeStringShouldBeEmpty() {
        String actual = SUT.getCodeString();

        assertEquals("", actual);

        return this;
    }

    public PresenterSUT CheckProductInDatabase(String name, String price) throws Exception {
        ArrayList<Product> actual = SUT.getAllProduct();

        assertProductInListByName(name, price, actual);

        return this;
    }

    public PresenterSUT CartShouldBeEmpty() {
        ArrayList<Product> actual = SUT.getProductInCart();

        assertTrue(actual.isEmpty());
        assertTrue(SUT.isCartEmpty());

        return this;
    }

    public PresenterSUT ListShouldMatchDatabase(ArrayList<Product> expected) {
        ArrayList<Product> actual = SUT.getAllProduct();

        for (Product product : actual) {
            assertTrue(String.format("%s : Product Not in List", product.getName()), expected.contains(product));
        }

        assertEquals(actual.size(), expected.size());

        return this;
    }

    public PresenterSUT Code_TotalPrice_OutputShouldMatchOnCheckout(HashMap<String, Double> testCollection) {
        for (String code  : testCollection.keySet()) {
            double actual = SUT.checkOut(code);

            assertEquals(testCollection.get(code), actual);
        }

        return this;
    }

    //region Helpers
    private IProductRepository MockDatabase() {
        return new IProductRepository() {

            ArrayList<Product> data = getDummyData();

            @Override
            public void add(Product product) {
                data.add(product);
            }

            @Override
            public Product getByCode(String code) {
                for (Product product : data) {
                    if(product.getProductCode().equalsIgnoreCase(code)){
                        return product;
                    }
                }

                return Product.Empty();
            }

            @Override
            public ArrayList<Product> getAll() {
                return data;
            }
        };
    }

    private static ArrayList<Product> getDummyData() {
        ArrayList<Product> dataSource = new ArrayList<>();

        dataSource.add(new Product("Peaches", "UUID7", "5.42"));
        dataSource.add(new Product("Apples", "UUID4", "1.4555"));
        dataSource.add(new Product("Corn", "UUID5", "3.42"));
        dataSource.add(new Product("Bacon", "UUID6", "8.4"));

        return dataSource;
    }

    private void assertProductInListByName(String name, String price, ArrayList<Product> actual) throws Exception {
        boolean foundProduct = false;

        for (Product product : actual) {
            if(product.getName().equals(name)) {
                assertEquals(product.getName(), name);
                assertEquals(product.getPrice().doubleValue(), Double.parseDouble(price));
                foundProduct = true;
                break;
            }
        }

        if(!foundProduct){
            throw new Exception("Did Not Find Product");
        }
    }
    //endregion
}
