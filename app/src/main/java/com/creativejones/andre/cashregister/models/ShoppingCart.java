package com.creativejones.andre.cashregister.models;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ShoppingCart {

    private static final String TAG = ShoppingCart.class.getSimpleName();

    private static final String SHOPPING_CART_KEY = "shopping_cart_key";

    private ArrayList<Product> Products = new ArrayList<>();

    public static ShoppingCart newInstance(Bundle savedInstanceState) {
        ShoppingCart cart = new ShoppingCart();

        if(savedInstanceState != null && savedInstanceState.containsKey(SHOPPING_CART_KEY)){
            ArrayList<Product> products = savedInstanceState.getParcelableArrayList(SHOPPING_CART_KEY);

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
    }

    public boolean isEmpty() {
        return Products.isEmpty();
    }

    public boolean removeProduct(Product product) {
        Product target = getByProductCode(product.getProductCode());

        if(target != null)
            return Products.remove(target);

        return false;
    }

    public boolean addProduct(Product product) {
        Product target = getByProductCode(product.getProductCode());

        if(target == null)
            return Products.add(product);

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
    private Product getByProductCode(String code){
        for (Product product : Products) {
            if(product.getProductCode().equalsIgnoreCase(code)){
                return product;
            }
        }

        return null;
    }
    //endergion
}
