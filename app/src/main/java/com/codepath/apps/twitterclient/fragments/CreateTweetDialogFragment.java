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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Vinh on 10/25/2016.
 */

public class CreateTweetDialogFragment extends DialogFragment {

    @BindView(R.id.etTweet)
    EditText etTweet;
    @BindView(R.id.tvAvailableCharacters)
    TextView tvAvailableCharacters;
    @BindView(R.id.btnTweet)
    Button btnTweet;
    private Unbinder unbinder;

    public interface CreateNewTweetListener {
        void onFinishCreateNewTweet(String inputText);
    }


    public CreateTweetDialogFragment() {

    }

    public static CreateTweetDialogFragment newInstance() {
        return new CreateTweetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tweet, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAvailableCharacters.setTextColor(ContextCompat.getColor(
                getContext(), android.R.color.holo_green_light));

        etTweet.requestFocus();
        etTweet.setMinLines(10);
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTextAvailableCharacter(etTweet.getText().toString().length());
            }

            private void setTextAvailableCharacter(int size) {
                if (140 - size >= 0) {
                    tvAvailableCharacters.setTextColor(ContextCompat.getColor(getContext(),
                            android.R.color.holo_green_light));
                    btnTweet.setEnabled(true);
                } else {
                    tvAvailableCharacters.setTextColor(ContextCompat.getColor(getContext(),
                            android.R.color.holo_red_light));
                    btnTweet.setEnabled(false);
                }
                tvAvailableCharacters.setText(String.valueOf(140 - size));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreateNewTweetListener) getTargetFragment())
                        .onFinishCreateNewTweet(etTweet.getText().toString());
                dismiss();
            }
        });
    }
}
