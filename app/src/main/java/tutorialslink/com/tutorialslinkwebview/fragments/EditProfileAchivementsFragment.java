package tutorialslink.com.tutorialslinkwebview.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileAchivementsFragment extends Fragment {
    private TextInputEditText textInputLayoutTechnologies,textInputLayoutAwards;
    private Button buttonNext;
    private LoginDetailPojo loginDetailPojo;
    private EditProfileAchivementsFragment.OnCompleteListener2 mListener;


    public EditProfileAchivementsFragment(LoginDetailPojo loginDetailPojo) {
        this.loginDetailPojo = loginDetailPojo;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_achivements, container, false);

        textInputLayoutTechnologies = view.findViewById(R.id.textInputLayoutTechnologies);
        textInputLayoutAwards = view.findViewById(R.id.textInputLayoutAwards);
        buttonNext = view.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onComplete2(true,textInputLayoutTechnologies.getText().toString(),textInputLayoutAwards.getText().toString());

            }
        });


        updateUI();

        textInputLayoutTechnologies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mListener.onComplete2(false,textInputLayoutTechnologies.getText().toString(),textInputLayoutAwards.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputLayoutAwards.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mListener.onComplete2(false,textInputLayoutTechnologies.getText().toString(),textInputLayoutAwards.getText().toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void updateUI() {

        if(!loginDetailPojo.getTable().get(0).getTechnologies().equals("")&&loginDetailPojo.getTable().get(0).getTechnologies()!=null) {
            textInputLayoutTechnologies.setText(loginDetailPojo.getTable().get(0).getTechnologies());
        }


        if(!loginDetailPojo.getTable().get(0).getAwards().equals("")&&loginDetailPojo.getTable().get(0).getAwards()!=null) {
            textInputLayoutAwards.setText(loginDetailPojo.getTable().get(0).getAwards());
        }


    }

    public static interface OnCompleteListener2
    {
        public abstract void onComplete2(Boolean next,String technologies,String awards);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (EditProfileAchivementsFragment.OnCompleteListener2)context;

        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnCompleteListener");
        }
    }

}
