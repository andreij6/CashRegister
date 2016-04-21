package com.creativejones.andre.cashregister.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creativejones.andre.cashregister.CashRegister;
import com.creativejones.andre.cashregister.R;
import com.creativejones.andre.cashregister.models.Product;
import com.creativejones.andre.cashregister.presenter.MainPresenter;
import com.creativejones.andre.cashregister.widget.AllProductsAdapter;
import com.creativejones.andre.cashregister.widget.BaseProductRecyclerAdapter;
import com.creativejones.andre.cashregister.widget.CartItemAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements CashRegister.RegisterView {

    private CashRegister.Presenter Presenter;

    @Bind(R.id.main_cart_recycler) RecyclerView CartRecycler;
    @Bind(R.id.main_all_products_recycler) RecyclerView AllItemsRecycler;
    @Bind(R.id.main_checkout_button) Button CheckoutBtn;
    @Bind(R.id.main_cart_empty_label) TextView CartEmptyLabel;

    CartItemAdapter CartAdapter;
    AllProductsAdapter ProductsAdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Presenter == null)
            Presenter = new MainPresenter(this);

        Presenter.initializeCart(savedInstanceState);

        toggleCheckoutSectionVisibility();

        CartAdapter = new CartItemAdapter(this, Presenter.getProductInCart());
        ProductsAdater = new AllProductsAdapter(this, Presenter.getAllProduct());

        setupRecycler(CartRecycler, CartAdapter);
        setupRecycler(AllItemsRecycler, ProductsAdater);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isFinishing())
            Presenter = null;
    }

    @Override
    public void addNewProduct(View view){
        AddProductDialogFragment fragment = AddProductDialogFragment.newInstance(Presenter);

        fragment.show(getSupportFragmentManager(), "AddProductDialog");
    }

    @Override
    public void removeProductFromCart(Product product) {
        boolean removed = Presenter.removeProductFromCart(product);

        if(removed)
            CartItemsChanged();
    }

    @Override
    public void addProductToCart(Product product) {
        boolean added = Presenter.addProductToCart(product);

        if(added)
            CartItemsChanged();
    }

    @Override
    public void checkOut(View view){
                                //would be easier to just pass in the Products but the Requirements were to pass in code string
        double result = Presenter.checkOut(Presenter.getCodeString());

        CheckoutDialogFragment fragment = CheckoutDialogFragment.newInstance(result);

        fragment.show(getSupportFragmentManager(), "CheckOutDialog");
    }

    @Override
    public void newProductAdded() {
        ArrayList<Product> products = Presenter.getAllProduct();

        updateRecyclerData(AllItemsRecycler, ProductsAdater, products);
    }

    //region Helpers
    private void setupRecycler(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void toggleCheckoutSectionVisibility() {
        boolean empty = Presenter.isCartEmpty();

        CartRecycler.setVisibility(empty == true ? View.GONE : View.VISIBLE);
        CheckoutBtn.setVisibility(empty == true ? View.GONE : View.VISIBLE);

        CartEmptyLabel.setVisibility(empty == true ? View.VISIBLE : View.GONE);
    }

    private void CartItemsChanged() {
        updateRecyclerData(CartRecycler, CartAdapter, Presenter.getProductInCart());

        toggleCheckoutSectionVisibility();
    }

    private void updateRecyclerData(RecyclerView recycler, BaseProductRecyclerAdapter adapter, ArrayList<Product> data) {
        adapter.setProducts(data);

        adapter.notifyItemChanged(data.size() - 1);

        recycler.swapAdapter(adapter, true);
    }
    //endregion
}
