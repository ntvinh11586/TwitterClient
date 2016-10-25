package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Vinh on 10/25/2016.
 */

public class EditNameDialogFragment extends DialogFragment {

    private EditText mEditText;
    private TextView tvNumber;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvNumber = (TextView) view.findViewById(R.id.text_number);
        tvNumber.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
        mEditText = (EditText) view.findViewById(R.id.edit_tweet);
        final Button mButton = (Button) view.findViewById(R.id.button_tweet);

        mEditText.requestFocus();

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currentTextSize = mEditText.getText().toString().length();

                if (140 - currentTextSize >= 0) {
                    tvNumber.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
                    mButton.setEnabled(true);
                } else {
                    tvNumber.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
                    mButton.setEnabled(false);
                }

                tvNumber.setText(String.valueOf(140 - currentTextSize));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();

                listener.onFinishEditDialog(mEditText.getText().toString());

                dismiss();
            }
        });

    }
}
