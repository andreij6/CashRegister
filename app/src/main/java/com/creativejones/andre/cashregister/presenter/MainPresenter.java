package com.creativejones.andre.cashregister.presenter;

import android.content.Context;
import android.os.Bundle;

import com.creativejones.andre.cashregister.CashRegister;
import com.creativejones.andre.cashregister.InvalidInputException;
import com.creativejones.andre.cashregister.data.ProductRepository;
import com.creativejones.andre.cashregister.models.Product;
import com.creativejones.andre.cashregister.models.ShoppingCart;
import com.creativejones.andre.cashregister.utils.ValidationConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPresenter implements CashRegister.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    public static final int CODE_LENGTH = 16;
    public static final int CODE_SECTION_LENGTH = 4;

    ShoppingCart Cart;
    ProductRepository Repository;

    public MainPresenter(Context context){
        Repository = new ProductRepository(context);
    }

    @Override
    public void initializeCart(Bundle savedInstanceState) {
        Cart = ShoppingCart.newInstance(savedInstanceState);
    }

    @Override
    public ArrayList<Product> getAllProduct() {
        return Repository.getAll();
    }

    @Override
    public ArrayList<Product> getProductInCart() {
        return Cart.getProducts();
    }

    @Override
    public boolean isCartEmpty() {
        return Cart.isEmpty();
    }

    @Override
    public void createProduct(String name, String price) throws InvalidInputException {
        String code = createProductCode();

        createProduct(name, price, code);
    }

    @Override
    public void createProduct(String name, String price, String code) throws InvalidInputException {
        code = formattedProductCode(code);

        ArrayList<Product> inventory = Repository.getAll();

        validateName(name, inventory);
        validateCode(code, inventory);
        validatePrice(price);

        Repository.add(new Product(name, code, price));
    }

    @Override
    public boolean addProductToCart(Product product) {
        return Cart.addProduct(product);
    }

    @Override
    public boolean removeProductFromCart(Product product) {
        return Cart.removeProduct(product);
    }

    @Override
    public double checkOut(String codes) {
        List<String> codeList = Arrays.asList(codes.split(";"));
        double total = 0.0;

        for (String code : codeList) {
            Product product = Repository.getByCode(code);

            total += product.getPrice().doubleValue();
        }

        return total;
    }

    @Override
    public String getCodeString() {
        ArrayList<Product> products = Cart.getProducts();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            builder.append(products.get(i).getProductCode());

            // if not at the end of the line
            // add the delimeter
            if(i + 1 != products.size()) {
                builder.append(";");
            }
        }

        return builder.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Cart.onSaveInstanceState(outState);
    }

    //region Helpers
    private String createProductCode() {
        String randomUUID = UUID.randomUUID().toString();

        return randomUUID.substring(4, 23);
    }

    private String formattedProductCode(String code) throws InvalidInputException{
        if(!matchesPattern(code)){
            return fomattCode(code);
        }

        return code;
    }

    private String fomattCode(String code) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i+= CODE_SECTION_LENGTH) {
            if(i + CODE_SECTION_LENGTH < CODE_LENGTH) {
                builder.append(code.substring(i, i + CODE_SECTION_LENGTH));

                builder.append("-");
            } else {
                builder.append(code.substring(i));
            }
        }

        return builder.toString();
    }

    private void validatePrice(String price) throws InvalidInputException {
        isStringEmpty(price, ValidationConstants.PRICE_EMPTY);

        try {
            BigDecimal value = new BigDecimal(price);
        } catch (NumberFormatException ne){
            throw new InvalidInputException(ValidationConstants.PRICE_INVALID_FORMAT);
        }

    }

    private void validateName(String name, ArrayList<Product> inventory) throws InvalidInputException {

        isStringEmpty(name, ValidationConstants.NAME_EMPTY);

        for (Product product : inventory) {
            if(product.getName().equalsIgnoreCase(name)){
                throw new InvalidInputException(ValidationConstants.NAME_EXISTS);
            }
        }

    }

    private void validateCode(String code, ArrayList<Product> inventory) throws InvalidInputException {

        for (Product product : inventory) {
            if(product.getProductCode().equalsIgnoreCase(code)){
                throw new InvalidInputException(ValidationConstants.CODE_EXISTS);
            }
        }

        isStringEmpty(code, ValidationConstants.CODE_EMPTY);

        if(!matchesPattern(code)){
            throw createException(ValidationConstants.CODE_MISMATCH);
        }
    }

    private boolean matchesPattern(String code)  {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}");

        Matcher matcher = pattern.matcher(code);

        if(!matcher.matches()){
            return false;
        }

        return true;
    }

    private void isStringEmpty(String value, String errorMessage) throws InvalidInputException{
        if(value.equalsIgnoreCase("")){
            throw new InvalidInputException(errorMessage);
        }
    }

    private InvalidInputException createException(String message){
        return new InvalidInputException(message);
    }
    //endregion

}
