package com.creativejones.andre.cashregister.widget;

import android.content.Context;
import android.view.View;

import com.creativejones.andre.cashregister.R;
import com.creativejones.andre.cashregister.models.Product;

import java.util.ArrayList;

public class AllProductsAdapter extends BaseProductRecyclerAdapter {

    ProductInteractionListener Listener;

    public AllProductsAdapter(Context context, ArrayList<Product> products){
        super(context, products);
        Listener = (ProductInteractionListener)context;
    }

    @Override
    protected View.OnClickListener getActionBtnClickListener(final Product product) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.addProductToCart(product);
            }
        };
    }

    @Override
    protected String getActionBtnLabel() {
        return mContext.getString(R.string.add);
    }

    public interface ProductInteractionListener {
        void addProductToCart(Product product);
    }
}
