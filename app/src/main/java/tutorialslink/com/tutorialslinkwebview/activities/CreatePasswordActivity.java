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

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientRegisterUser;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientSupport;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.TablePojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CreatePasswordActivity extends AppCompatActivity {

    private String data;
    private TablePojo userDetail;
    private Button buttonCreateAccount;
    private TextInputEditText editTextPassword;
    private LinearLayout layout;
    private ApiClientRegisterUser apiClientRegisterUser;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        data = getIntent().getStringExtra("data");

        userDetail = new Gson().fromJson(data,TablePojo.class);

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Create Password");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        editTextPassword=findViewById(R.id.editTextPassword);
        buttonCreateAccount=findViewById(R.id.buttonCreateAccount);
        layout = findViewById(R.id.layout);


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextPassword.getText().toString().equals("")) {

                    editTextPassword.setError("Set Password");

                }

                else {

                        if (!NetworkCheck.isNetworkAvailable(CreatePasswordActivity.this))
                        {

                            Snackbar.make(layout,"Network Unavailable",Snackbar.LENGTH_LONG).show();

                        }

                        else {

                            userDetail.setPassword(editTextPassword.getText().toString());
                               createAccount();
                        }
                }
            }
        });



    }

    private void createAccount()
    {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientRegisterUser = ApiClientBase.getApiClient().create(ApiClientRegisterUser.class);

     //   String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

        //    System.out.println("url               : " + url);

        Call<String> call = apiClientRegisterUser.registerUser(userDetail.getFirst_name(),userDetail.getEmail(),userDetail.getPassword(),userDetail.getCity(),userDetail.getState(),userDetail.getCountry(),userDetail.getPostal_Code());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res  = response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if (res != null) {
                     if(res.equals("1")) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                    }

                    else if(res.equals("2"))
                     {
                         Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_SHORT).show();
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
