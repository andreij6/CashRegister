package com.creativejones.andre.cashregister.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.creativejones.andre.cashregister.R;
import com.creativejones.andre.cashregister.utils.PriceFormatter;

public class CheckoutDialogFragment extends DialogFragment {

    private double TotalPrice;

    public static CheckoutDialogFragment newInstance(double total) {
        CheckoutDialogFragment fragment = new CheckoutDialogFragment();
        fragment.TotalPrice = total;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_checkout_title)
                .setMessage(String.format(getActivity().getString(R.string.dialog_checkout_price_format), formattedPrice()))
                .setPositiveButton(R.string.dialog_checkout_pay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private String formattedPrice() {
        return PriceFormatter.formattPrice(getActivity(), TotalPrice);
    }
}
