package com.creativejones.andre.cashregister;

import com.creativejones.andre.cashregister.entities.Product;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ProductUnitTests {

    @Test
    public void productWithSameProductCodeShouldBeEqual(){
        Product product = new Product("Burger", "555", "2.58");
        Product other = new Product("Ham", "555", "202");

        assertTrue(product.equals(other));
    }

    @Test
    public void productWithDifferentProductCodeShouldNotBeEqual(){
        Product product = new Product("Burger", "555", "2.58");
        Product other = new Product("Ham", "545", "202");

        assertTrue(!product.equals(other));
    }
}
