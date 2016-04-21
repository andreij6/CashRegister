package com.creativejones.andre.cashregister.data;

import android.content.Context;

import com.creativejones.andre.cashregister.models.Product;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FileDatabase {

    Context mContext;
    private final static String FILE_NAME = "products.txt";

    public FileDatabase(Context context) {
        mContext = context;
    }

    public void add(List<Product> _items) {
        File filesDir = mContext.getFilesDir();
        File todoFile = new File(filesDir, FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, productToStrings(_items));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Product> readAll() {
        File filesDir = mContext.getFilesDir();
        File todoFile = new File(filesDir, FILE_NAME);

        ArrayList<String> items = new ArrayList<>();
        boolean threwExecption = false;

        try {
            items.addAll(FileUtils.readLines(todoFile));
        } catch (IOException io) {
            io.printStackTrace();
            threwExecption = true;
        } finally {
            ArrayList<Product> result = stringsToProduct(items);

            if (result.size() == 0 && isFirstTimeOnApp() && !threwExecption) {
                addDummyData();

                return readAll();
            }

            if(threwExecption && isFirstTimeOnApp()){
                addDummyData();

                return readAll();
            }

            return result;
        }
    }

    private void addDummyData() {
        ArrayList<Product> dataSource = new ArrayList<>();

        dataSource.add(new Product("Peaches", "UUID7", "5.42"));
        dataSource.add(new Product("Apples", "UUID4", "1.4555"));
        dataSource.add(new Product("Corn", "UUID5", "3.42"));
        dataSource.add(new Product("Bacon", "UUID6", "8.4"));

        add(dataSource);
    }

    //region Helpers
    private Collection<String> productToStrings(List<Product> items) {
        ArrayList<String> result = new ArrayList<>();

        for (Product item : items) {
            result.add(item.toString());
        }

        return result;
    }

    private ArrayList<Product> stringsToProduct(ArrayList<String> items) {
        ArrayList<Product> result = new ArrayList<>();

        final int PROPERTY_COUNT = 3;
        final int PRODUCT_NAME = 0;
        final int PRODUCT_CODE = 1;
        final int PRODUCT_PRICE = 2;

        for (String item : items) {
            List<String> productProperties = Arrays.asList(item.split(";"));

            if (productProperties != null && productProperties.size() == PROPERTY_COUNT) {

                result.add(new Product(productProperties.get(PRODUCT_NAME),
                        productProperties.get(PRODUCT_CODE),
                        productProperties.get(PRODUCT_PRICE)));
            }
        }

        return result;
    }

    private boolean isFirstTimeOnApp() {
        //TODO: get value from shared Preferences
        return true;
    }

    public void clear() {
        add(new ArrayList<Product>());
    }
    //endregion

}
