package com.creativejones.andre.cashregister.data;

import com.creativejones.andre.cashregister.entities.Product;

import java.util.ArrayList;

public interface IProductRepository {
    void add(Product product);

    Product getByCode(String code);

    ArrayList<Product> getAll();
}
