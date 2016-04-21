package com.creativejones.andre.cashregister.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativejones.andre.cashregister.CashRegister;
import com.creativejones.andre.cashregister.InvalidInputException;
import com.creativejones.andre.cashregister.R;

public class AddProductDialogFragment extends DialogFragment {

    private CashRegister.Presenter Presenter;
    private CashRegister.RegisterView RegisterView;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_product, null);

        final EditText nameInput = (EditText)view.findViewById(R.id.dialog_product_name_et);
        final EditText priceInput =(EditText)view.findViewById(R.id.dialog_product_price_et);
        final EditText codeInput =(EditText)view.findViewById(R.id.dialog_product_code_et);
        final Button toggleCodeBtn = (Button)view.findViewById(R.id.dialog_product_code_btn);

        toggleCodeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(codeInput.getVisibility() == View.VISIBLE){
                    codeInput.setVisibility(View.GONE);
                    toggleCodeBtn.setText(getString(R.string.dialog_product_manually_add_code));
                } else {
                    codeInput.setVisibility(View.VISIBLE);
                    toggleCodeBtn.setText(getString(R.string.dialog_product_automatically_add_code));
                }
            }
        });

        builder.setTitle(R.string.new_product)
                .setView(view)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if(codeInput.getVisibility() == View.VISIBLE){
                                Presenter.createProduct(nameInput.getText().toString(), priceInput.getText().toString(), codeInput.getText().toString());
                            } else {
                                Presenter.createProduct(nameInput.getText().toString(), priceInput.getText().toString());
                            }

                            RegisterView = (CashRegister.RegisterView)getActivity();

                            RegisterView.newProductAdded();

                            dialog.cancel();
                        } catch (InvalidInputException iie){
                            Toast.makeText(getActivity(), iie.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public static AddProductDialogFragment newInstance(CashRegister.Presenter presenter) {
        AddProductDialogFragment fragment = new AddProductDialogFragment();
        fragment.Presenter = presenter;
        return fragment;
    }
}
