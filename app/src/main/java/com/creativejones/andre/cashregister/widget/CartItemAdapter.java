package com.creativejones.andre.cashregister.widget;

import android.content.Context;
import android.view.View;

import com.creativejones.andre.cashregister.R;
import com.creativejones.andre.cashregister.entities.Product;

import java.util.ArrayList;

public class CartItemAdapter extends BaseProductRecyclerAdapter {

    CartInteractionListener Listener;

    public CartItemAdapter(Context context, ArrayList<Product> products){
        super(context, products);
        Listener = (CartInteractionListener) context;
    }

    @Override
    protected View.OnClickListener getActionBtnClickListener(final Product product) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.removeProductFromCart(product);
            }
        };
    }

    @Override
    protected String getActionBtnLabel() {
        return mContext.getString(R.string.remove);
    }

    public interface CartInteractionListener {
        void removeProductFromCart(Product product);
    }
}
