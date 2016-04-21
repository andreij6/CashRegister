package com.creativejones.andre.cashregister.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Product implements Parcelable {

    private String Name;
    private String ProductCode;
    private BigDecimal Price;

    public Product(String name, String productCode, String price) {
        Name = name;
        ProductCode = productCode;
        Price = new BigDecimal(price);
    }

    //region Getters & Setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }
    //endregion

    //region Parcelable
    public Product(Parcel in){
        Name = in.readString();
        ProductCode = in.readString();
        Price = convertStringPrice(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(ProductCode);
        dest.writeString(Price.toString());
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s", Name, ProductCode, Price.toString());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    //endregion

    //region Helpers
    private BigDecimal convertStringPrice(Parcel in) {
        String total = in.readString();

        total = total != null ? total : "0.0";

        return new BigDecimal(total);
    }

    public static Product Empty() {
        return new Product("Empty", "", "");
    }
    //endregion
}
