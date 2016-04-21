package com.creativejones.andre.cashregister;

import android.os.Bundle;
import android.view.View;

import com.creativejones.andre.cashregister.models.Product;
import com.creativejones.andre.cashregister.widget.AllProductsAdapter;
import com.creativejones.andre.cashregister.widget.CartItemAdapter;

import java.util.ArrayList;

public interface CashRegister {

    interface RegisterView extends
            CartItemAdapter.CartInteractionListener,
            AllProductsAdapter.ProductInteractionListener
    {
        void addNewProduct(View view);

        void checkOut(View view);

        void newProductAdded();
    }

    interface Presenter {

        void onSaveInstanceState(Bundle outState);

        void initializeCart(Bundle savedInstanceState);

        ArrayList<Product> getAllProduct();

        ArrayList<Product> getProductInCart();

        boolean isCartEmpty();

        boolean addProductToCart(Product product);

        boolean removeProductFromCart(Product product);

        double checkOut(String codes);

        String getCodeString();

        void createProduct(String name, String price) throws InvalidInputException;

        void createProduct(String name, String price, String code) throws InvalidInputException;
    }
}
