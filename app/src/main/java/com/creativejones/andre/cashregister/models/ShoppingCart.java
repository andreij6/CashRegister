package com.creativejones.andre.cashregister.models;

import android.os.Bundle;

import com.creativejones.andre.cashregister.entities.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ShoppingCart {

    private static final String TAG = ShoppingCart.class.getSimpleName();

    private static final String SHOPPING_CART_KEY = "shopping_cart_key";
    private static final String SHOPPING_CART_CODES_KEY = "shopping_cart_codes_key";

    private ArrayList<Product> Products = new ArrayList<>();
    private StringBuilder ProductCodes = new StringBuilder();

    public static ShoppingCart newInstance(Bundle savedInstanceState) {
        ShoppingCart cart = new ShoppingCart();

        if(savedInstanceState != null && savedInstanceState.containsKey(SHOPPING_CART_KEY)){
            ArrayList<Product> products = savedInstanceState.getParcelableArrayList(SHOPPING_CART_KEY);
            String codes = savedInstanceState.getString(SHOPPING_CART_CODES_KEY);

            cart.ProductCodes.append(codes);

            if(products != null)
                cart.Products = products;
        }

        return cart;
    }

    public ArrayList<Product> getProducts() {
        return Products;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SHOPPING_CART_KEY, Products);
        outState.putString(SHOPPING_CART_CODES_KEY, ProductCodes.toString());
    }

    public boolean isEmpty() {
        return Products.isEmpty();
    }

    public boolean removeProduct(Product product) {
        Product target = getByProductCode(product.getProductCode());

        if(target != null) {
            removeProductCode(product.getProductCode());
            return Products.remove(target);
        }

        return false;
    }

    public boolean addProduct(Product product) {
        Product target = getByProductCode(product.getProductCode());

        if(target == null) {
            addProductCode(product.getProductCode());
            return Products.add(product);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Product product : Products) {
            builder.append(product.toString() + "\n");
        }

        return builder.toString();
    }

    //region Helpers
    private void addProductCode(String productCode) {
        if(ProductCodes.length() == 0){
            ProductCodes.append(productCode);
        } else {
            ProductCodes.append(";" + productCode);
        }
    }

    private void removeProductCode(String productCode) {
        ArrayList<String> codes = new ArrayList<>(Arrays.asList(ProductCodes.toString().split(";")));

        Iterator<String> codeIterator = codes.iterator();

        while(codeIterator.hasNext()){
            if (codeIterator.next().equalsIgnoreCase(productCode)) {
                codeIterator.remove();
            }
        }

        ProductCodes = joinCodeStrings(codes);
    }

    private StringBuilder joinCodeStrings(List<String> codes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < codes.size(); i++) {
            builder.append(codes.get(i));

            // if not at the end of the line
            // add the delimeter
            if(i + 1 != codes.size()) {
                builder.append(";");
            }
        }

        return builder;
    }

    private Product getByProductCode(String code){
        for (Product product : Products) {
            if(product.getProductCode().equalsIgnoreCase(code)){
                return product;
            }
        }

        return null;
    }

    public String getCodeString() {
        return ProductCodes.toString();
    }
    //endergion
}
