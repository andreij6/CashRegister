package com.creativejones.andre.cashregister.data;

import android.content.Context;

import com.creativejones.andre.cashregister.models.Product;

import java.util.ArrayList;

public class ProductRepository {


    FileDatabase DB;

    public ProductRepository(Context context){
        DB = new FileDatabase(context);
    }


    public void add(Product product){
        ArrayList<Product> existingList = getAll();

        existingList.add(product);

        DB.add(existingList);
    }

    public Product getByCode(String code){
        ArrayList<Product> dataSource = getAll();

        for (Product product : dataSource) {
            if(product.getProductCode().equalsIgnoreCase(code)){
                return product;
            }
        }

        return Product.Empty();
    }

    public ArrayList<Product> getAll(){
        return DB.readAll();
    }

}
