package tutorialslink.com.tutorialslinkwebview.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientMobAuth;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUserEmailLogin;
import tutorialslink.com.tutorialslinkwebview.pojos.MobAuthPOjo.SendMobPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class LoginMobActivity extends AppCompatActivity {

    private Button btnContinue;
    private EditText editTextUserPhnNumber;
    private String phnNumber="";
    private int i=0;
    private TextView textViewSkip;
  //  private Verification mVerification;
    private ProgressDialog pDialog;
    private CountDownTimer c;
    private CountryCodePicker ccp;
    private ApiClientMobAuth apiClientMobAuth;
    private String phnSuffix;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        textInputLayoutUserPhnNumber=(TextInputLayout)findViewById(R.id.textInputLayoutUserPhnNumber);
//        textInputLayoutOTP=(TextInputLayout)findViewById(R.id.textInputLayoutOTP);
        editTextUserPhnNumber=(EditText)findViewById(R.id.editTextUserPhnNumber);
//        editTextOTP=(EditText)findViewById(R.id.editTextOTP);
//        textViewResentOTP=(TextView)findViewById(R.id.textViewResentOTP);
//        textViewReEnterNumber=(TextView)findViewById(R.id.textViewReEnterNumber);


        textViewSkip = findViewById(R.id.textViewSkip);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(LoginMobActivity.this, "Updated " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
            }
        });



        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginMobActivity.this);
                builder.setTitle("Are You Sure?");
                builder.setMessage("You wont be able to receive latest Information about events, seminars and other stuffs on your phone");
                builder.setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       startActivity(new Intent(getApplicationContext(),Dashboard.class));

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });


        //requestpermission
       // getPermission();

        checkAndRequestPermissions();

        //

        btnContinue = (Button) findViewById(R.id.buttonContinue);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            btnContinue.setBackgroundResource(R.drawable.ripple_green);
//        }
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Intent intent = new Intent(LoginMobActivity.this, LoginMobOtpActivity.class);
//                startActivity(intent);






                if(!NetworkCheck.isNetworkAvailable(getApplicationContext()))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    Snackbar.make((RelativeLayout)findViewById(R.id.relativelayoutView),"Network Unavailable",Snackbar.LENGTH_LONG)
                            .show();
                }

                else if(editTextUserPhnNumber.getText().toString().length()<10)
                {
                    editTextUserPhnNumber.setError("Invalid Phone Number");
                    editTextUserPhnNumber.requestFocus();
                }

                else {

                    Intent i = new Intent(getApplicationContext(),LoginMobOtpActivity.class);
                    i.putExtra("code",ccp.getSelectedCountryCode());
                    i.putExtra("phn",editTextUserPhnNumber.getText().toString());
                    startActivity(i);

                    /*
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    phnSuffix= ccp.getSelectedCountryCodeWithPlus();
                        login(editTextUserPhnNumber.getText().toString());
                        */
                }



            }
        });
    }



    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        101);


            }
        }
    }


    private void login(final String phnNumber) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        // show it
        apiClientMobAuth = ApiClientBase.getApiClient().create(ApiClientMobAuth.class);

        String url = "https://2factor.in/API/V1/f8ad5ed3-7db0-11e8-a895-0200cd936042/SMS/+"+phnSuffix+phnNumber+"/AUTOGEN";

        Call<SendMobPojo> call= apiClientMobAuth.sendMob(url);
        call.enqueue(new Callback<SendMobPojo>() {
            @Override
            public void onResponse(Call<SendMobPojo> call, Response<SendMobPojo> response) {

                SendMobPojo output =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.getStatus().equals("Success")) {
                           Toast.makeText(getApplicationContext(),"OTP Sent", Toast.LENGTH_SHORT).show();

                           Intent i = new Intent(getApplicationContext(),LoginMobOtpActivity.class);
                           i.putExtra("data",output.getDetails());
                           i.putExtra("phn",phnNumber);
                           startActivity(i);

                    }


                    else
                    {
                        Toast.makeText(getApplicationContext(),"Operation Failed", Toast.LENGTH_SHORT).show();
                    }


                }

                else

                {
                    Toast.makeText(getApplicationContext(),"Something went wrong output", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SendMobPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                    Toast.makeText(this, "Grant Permission For Auto Verification", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private  boolean checkAndRequestPermissions() {

        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}