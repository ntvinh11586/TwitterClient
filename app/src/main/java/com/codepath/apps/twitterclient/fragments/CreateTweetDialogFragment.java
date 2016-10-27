package com.codepath.apps.twitterclient.fragments;

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

import com.codepath.apps.twitterclient.R;

/**
 * Created by Vinh on 10/25/2016.
 */

public class CreateTweetDialogFragment extends DialogFragment {

    private EditText mEditText;
    private TextView tvNumber;
    private Button mButton;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public CreateTweetDialogFragment() {

    }

    public static CreateTweetDialogFragment newInstance(String title) {
        CreateTweetDialogFragment frag = new CreateTweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNumber = (TextView) view.findViewById(R.id.text_number);
        tvNumber.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
        mEditText = (EditText) view.findViewById(R.id.edit_tweet);
        mButton = (Button) view.findViewById(R.id.button_tweet);

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
