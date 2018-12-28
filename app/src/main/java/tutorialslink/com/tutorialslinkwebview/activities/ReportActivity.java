package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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
import tutorialslink.com.tutorialslinkwebview.network.ApiClientReportBug;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientSupport;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class ReportActivity extends AppCompatActivity {

    private Toolbar tb;
    private TextInputEditText textInputEditTextName,textInputEditTextMessage;
    private Button buttonSubmit;
    private ApiClientReportBug apiClientReportBug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Support");
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        buttonSubmit=findViewById(R.id.buttonSubmit);
        textInputEditTextName=findViewById(R.id.textInputEditTextName);
        textInputEditTextMessage=findViewById(R.id.textInputEditTextMessage);

        textInputEditTextName.setText(UserSharedPreferenceData.getPrefLoggedinUserName(this));

        if(textInputEditTextName.getText().toString().length()>0) {
            textInputEditTextMessage.requestFocus();
        }


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    if(NetworkCheck.isNetworkAvailable(ReportActivity.this))
                    {
                        reportBug();
                    }

                    else {
                        Snackbar.make(findViewById(R.id.layout),"Network Unavaiable",Snackbar.LENGTH_LONG).show();
                    }
                }

            }
        });


    }

    private void reportBug() {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientReportBug = ApiClientBase.getApiClient().create(ApiClientReportBug.class);

        String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

        //    System.out.println("url               : " + url);

        Call<String> call = apiClientReportBug.reportBug(UserSharedPreferenceData.getLoggedInUserID(this),textInputEditTextName.getText().toString(),textInputEditTextMessage.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res  = response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if (res != null) {
                    if (res.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        //   Toast.makeText(getApplicationContext(), "1 and success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReportActivity.this,ThankyouActivity.class);
                        intent.putExtra("data","2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

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

    private boolean validate() {

        if(textInputEditTextName.getText().toString().equals(""))
        {
            textInputEditTextName.setError("Enter Name");
            textInputEditTextName.requestFocus();
            return false;
        }

        else if(textInputEditTextMessage.getText().toString().equals(""))
        {
            textInputEditTextMessage.setError("Enter Message");
            textInputEditTextMessage.requestFocus();
            return false;
        }

        return true;
    }
}
