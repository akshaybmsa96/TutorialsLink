package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientForgotPassword;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Toolbar tb;
    private Button buttonForgotPassword;
    private TextInputEditText textInputEditTextEmail;
    private LinearLayout layout;
    private ApiClientForgotPassword apiClientForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Forgot Password");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        textInputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        buttonForgotPassword=findViewById(R.id.buttonForgotPassword);

        layout=findViewById(R.id.layout);


        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textInputEditTextEmail.getText().toString().equals(""))
                {
                    textInputEditTextEmail.setError("Enter Email");
                }

                else {

                    if(NetworkCheck.isNetworkAvailable(ForgotPasswordActivity.this))
                    {
                        forgetPassword();
                    }

                    else {

                        Snackbar.make(layout,"Ntwork Unavailable",Snackbar.LENGTH_LONG).show();

                    }

                }

            }
        });

    }

    private void forgetPassword() {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Verifying");
        pDialog.setCancelable(false);
        pDialog.show();



        apiClientForgotPassword = ApiClientBase.getApiClient().create(ApiClientForgotPassword.class);
        String url = "ForgotPassword?email="+textInputEditTextEmail.getText().toString();

        Call<String> call = apiClientForgotPassword.forgetPassword(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();

                pDialog.dismiss();

                if(res.equals("0"))
                {
                        textInputEditTextEmail.setError("Email is not Registered");
                        textInputEditTextEmail.requestFocus();
                }

                else if(res.equals("1"))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Activation link sent to your email",Toast.LENGTH_SHORT).show();
                }

                else {

                    Toast.makeText(ForgotPasswordActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();

                Toast.makeText(ForgotPasswordActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());

            }
        });

    }
}
