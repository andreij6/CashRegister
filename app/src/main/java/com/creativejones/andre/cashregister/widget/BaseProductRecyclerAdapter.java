package com.creativejones.andre.cashregister.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.creativejones.andre.cashregister.R;
import com.creativejones.andre.cashregister.models.Product;
import com.creativejones.andre.cashregister.utils.PriceFormatter;

import java.util.ArrayList;

public abstract class BaseProductRecyclerAdapter extends RecyclerView.Adapter<BaseProductRecyclerAdapter.BaseHolder>{

    protected Context mContext;
    protected ArrayList<Product> Products = new ArrayList<>();

    public BaseProductRecyclerAdapter(Context context, ArrayList<Product> products){
        mContext = context;
        Products.addAll(products);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public int getItemCount() {
        return Products.size();
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.bindView(position);
    }

    public void setProducts(ArrayList<Product> data) {
        Products = data;
    }

    protected abstract View.OnClickListener getActionBtnClickListener(Product product);

    protected abstract String getActionBtnLabel();

    public class BaseHolder extends RecyclerView.ViewHolder{

        TextView Name;
        TextView Price;
        Button AddOrRemoveButton;

        public BaseHolder(View itemView) {
            super(itemView);
            Name = (TextView)itemView.findViewById(R.id.product_item_name);
            Price = (TextView)itemView.findViewById(R.id.product_item_price);
            AddOrRemoveButton = (Button)itemView.findViewById(R.id.product_item_action_btn);
        }

        public void bindView(final int position){
            Product product = Products.get(position);

            Name.setText(product.getName());
            Price.setText(PriceFormatter.formattPrice(mContext, product.getPrice().doubleValue()));

            AddOrRemoveButton.setText(getActionBtnLabel());
            AddOrRemoveButton.setOnClickListener(getActionBtnClickListener(product));
        }
    }
}
