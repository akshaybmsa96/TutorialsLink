package tutorialslink.com.tutorialslinkwebview.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
public class EditProfileSocialFragment extends Fragment {

    private LoginDetailPojo loginDetailPojo;
    private TextInputEditText textInputEditTextBlog,textInputEditTextLinkedin,textInputEditTextGithub,textInputEditTextTwitter,textInputEditTextFacebook;
    private Button buttonUpdate;
    private EditProfileSocialFragment.OnCompleteListener3 mListener;


    public EditProfileSocialFragment(LoginDetailPojo loginDetailPojo) {
        this.loginDetailPojo = loginDetailPojo;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_social, container, false);

        textInputEditTextBlog = view.findViewById(R.id.textInputEditTextBlog);
        textInputEditTextLinkedin = view.findViewById(R.id.textInputEditTextLinkedin);
        textInputEditTextGithub = view.findViewById(R.id.textInputEditTextGithub);
        textInputEditTextTwitter = view.findViewById(R.id.textInputEditTextTwitter);
        textInputEditTextFacebook = view.findViewById(R.id.textInputEditTextFacebook);


        buttonUpdate = view.findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onComplete3(true,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());
            }
        });


        updateUI();

        textInputEditTextBlog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete3(false,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditTextLinkedin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete3(false,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditTextGithub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.onComplete3(false,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditTextTwitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mListener.onComplete3(false,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textInputEditTextFacebook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mListener.onComplete3(false,textInputEditTextBlog.getText().toString(),textInputEditTextLinkedin.getText().toString(),textInputEditTextGithub.getText().toString(),textInputEditTextTwitter.getText().toString(),textInputEditTextFacebook.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return view;
    }

    private void updateUI() {


        if (loginDetailPojo.getTable().get(0).getBlog_link()!=null&&!loginDetailPojo.getTable().get(0).getBlog_link().equals("")&&!loginDetailPojo.getTable().get(0).getBlog_link().equals("0"))
        {
            textInputEditTextBlog.setText(loginDetailPojo.getTable().get(0).getBlog_link());
        }

        if (loginDetailPojo.getTable().get(0).getLinkedin_profile()!=null&&!loginDetailPojo.getTable().get(0).getLinkedin_profile().equals("")&&!loginDetailPojo.getTable().get(0).getLinkedin_profile().equals("0"))
        {
            textInputEditTextLinkedin.setText(loginDetailPojo.getTable().get(0).getLinkedin_profile());
        }

        if (loginDetailPojo.getTable().get(0).getGithub_profile()!=null&&!loginDetailPojo.getTable().get(0).getGithub_profile().equals("")&&!loginDetailPojo.getTable().get(0).getGithub_profile().equals("0"))
        {
            textInputEditTextGithub.setText(loginDetailPojo.getTable().get(0).getGithub_profile());
        }

        if (loginDetailPojo.getTable().get(0).getTwitter_profile()!=null&&!loginDetailPojo.getTable().get(0).getTwitter_profile().equals("")&&!loginDetailPojo.getTable().get(0).getTwitter_profile().equals("0"))
        {
            textInputEditTextTwitter.setText(loginDetailPojo.getTable().get(0).getTwitter_profile());
        }

        if (loginDetailPojo.getTable().get(0).getFaebook_profile()!=null&&!loginDetailPojo.getTable().get(0).getFaebook_profile().equals("")&&!loginDetailPojo.getTable().get(0).getFaebook_profile().equals("0"))
        {
            textInputEditTextFacebook.setText(loginDetailPojo.getTable().get(0).getFaebook_profile());
        }
    }


    public static interface OnCompleteListener3
    {
        public abstract void onComplete3(Boolean update,String blog,String linkedIn,String github,String twitter,String facebook);
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (EditProfileSocialFragment.OnCompleteListener3)context;

        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnCompleteListener");
        }
    }
}
