package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientChangePassword;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientRegisterUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar tb;
    private TextInputLayout textInputLayoutOldPassword,textInputLayoutNewPassword,textInputLayoutReNewPassword;
    private TextInputEditText editTextReNewPassword,editTextNewPassword,editTextOldPassword;
    private Button buttonCreateAccount;
    private ApiClientChangePassword apiClientChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        tb =  findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Change Password");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        textInputLayoutOldPassword=findViewById(R.id.textInputLayoutOldPassword);
        textInputLayoutNewPassword=findViewById(R.id.textInputLayoutNewPassword);
        textInputLayoutReNewPassword=findViewById(R.id.textInputLayoutReNewPassword);

        editTextOldPassword=findViewById(R.id.editTextOldPassword);
        editTextNewPassword=findViewById(R.id.editTextNewPassword);
        editTextReNewPassword=findViewById(R.id.editTextReNewPassword);

        buttonCreateAccount=findViewById(R.id.buttonCreateAccount);


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextOldPassword.getText().toString().equals(""))
                {
                    editTextOldPassword.setError("Enter Old Password");
                    editTextOldPassword.requestFocus();
                }

                else if(editTextNewPassword.getText().toString().equals(""))
                {
                    editTextNewPassword.setError("Enter New Password");
                    editTextNewPassword.requestFocus();
                }

                else if(editTextReNewPassword.getText().toString().equals(""))
                {
                    editTextReNewPassword.setError("Re Enter New Password");
                    editTextReNewPassword.requestFocus();
                }

                else if(!editTextNewPassword.getText().toString().equals(editTextReNewPassword.getText().toString()))
                {
                    editTextReNewPassword.setError("Password did not match");
                    editTextReNewPassword.requestFocus();
                }

                else {

                    changePassword();
                }
            }
        });




    }

    private void changePassword() {


        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientChangePassword = ApiClientBase.getApiClient().create(ApiClientChangePassword.class);

        //   String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

        //    System.out.println("url               : " + url);

        Call<String> call = apiClientChangePassword.changePassword(UserSharedPreferenceData.getLoggedInUserID(this),editTextOldPassword.getText().toString(),editTextNewPassword.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res  = response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if (res != null) {
                    if(res.equals("0")) {
                        Toast.makeText(getApplicationContext(), "Password did Not Matched", Toast.LENGTH_SHORT).show();
                        editTextOldPassword.setError("Old Password did not match");
                        editTextOldPassword.requestFocus();

                    }

                    else if(res.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "error "+ res, Toast.LENGTH_SHORT).show();
                    }

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                System.out.println("failure" + "+ : " + t.getMessage());
                System.out.println("failure" + "+ : " + t.getCause());
                System.out.println("failure" + "+ : " + t.toString());
            }
        });

    }
}
