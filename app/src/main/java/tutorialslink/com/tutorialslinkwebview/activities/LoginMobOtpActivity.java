package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientMobAuth;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientOtpAuth;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUpdateMobile;
import tutorialslink.com.tutorialslinkwebview.pojos.MobAuthPOjo.SendMobPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.MobAuthPOjo.VerifyOtpPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;


public class LoginMobOtpActivity extends AppCompatActivity implements VerificationListener {

    private Button buttonContinue;
    private String sessionId,phn,code,otp;
    private ApiClientOtpAuth apiClientOtpAuth;
    private EditText editTextOTP;
    private ProgressDialog pDialog;
    private TextView textViewResendOTP,textViewVoiceOTP;
    private ApiClientMobAuth apiClientMobAuth;
    private Verification mVerification;
    private ApiClientUpdateMobile apiClientUpdateMobile;
    private CountDownTimer c;
    private int i  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mob_otp);

        sessionId=getIntent().getStringExtra("data");
        phn=getIntent().getStringExtra("phn");
        code=getIntent().getStringExtra("code");

     //   Toast.makeText(getApplicationContext(),code+phn,Toast.LENGTH_LONG).show();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending OTP...");
        pDialog.setCancelable(false);
        pDialog.show();

        mVerification = SendOtpVerification.createSmsVerification
                (SendOtpVerification
                        .config(code + phn)
                        .senderId("TUTLNK")
                        .otplength("4")
                        .context(this)
                        .autoVerification(true)
                        .build(), this);

        mVerification.initiate();


        editTextOTP = findViewById(R.id.editTextOTP);

        buttonContinue=findViewById(R.id.buttonContinue);

        textViewVoiceOTP=findViewById(R.id.textViewVoiceOTP);
        textViewResendOTP=findViewById(R.id.textViewResendOTP);


        textViewResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVerification.resend("text");

            }
        });


        textViewVoiceOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mVerification.resend("voice");
             //   getVoiceOTP();
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!NetworkCheck.isNetworkAvailable(getApplicationContext()))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    Snackbar.make((RelativeLayout)findViewById(R.id.relativelayoutView),"Network Unavailable",Snackbar.LENGTH_LONG)
                            .show();

                }

                else if(editTextOTP.getText().toString().length()<1)
                {
                    editTextOTP.setError("Enter OTP");
                    editTextOTP.requestFocus();
                }

                else {

                    otp = editTextOTP.getText().toString();
                    if (otp.length() < 4) {
                        editTextOTP.setError("Invalid OTP");
                    } else {
                        mVerification.verify(otp);
                    }
                }
            }
        });
    }

    private void getVoiceOTP() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        // show it
        apiClientMobAuth = ApiClientBase.getApiClient().create(ApiClientMobAuth.class);

       // String url = "https://2factor.in/API/V1/f8ad5ed3-7db0-11e8-a895-0200cd936042/SMS/+"+phnSuffix+phnNumber+"/AUTOGEN";
        String url = "http://2factor.in/API/V1/f8ad5ed3-7db0-11e8-a895-0200cd936042/VOICE/"+phn+"/AUTOGEN";

        Call<SendMobPojo> call= apiClientMobAuth.sendMob(url);
        call.enqueue(new Callback<SendMobPojo>() {
            @Override
            public void onResponse(Call<SendMobPojo> call, Response<SendMobPojo> response) {

                SendMobPojo output =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.getStatus().equals("Success")) {
                        Toast.makeText(getApplicationContext(),"You will receive Call Soon", Toast.LENGTH_SHORT).show();

                        sessionId = output.getDetails();
                        i=2;

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

    private void verifyOTP(String url) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        // show it
        apiClientOtpAuth = ApiClientBase.getApiClient().create(ApiClientOtpAuth.class);

        Call<VerifyOtpPojo> call= apiClientOtpAuth.sendMob(url);
        call.enqueue(new Callback<VerifyOtpPojo>() {
            @Override
            public void onResponse(Call<VerifyOtpPojo> call, Response<VerifyOtpPojo> response) {

            //    Toast.makeText(getApplicationContext(), new Gson().toJson(response.body())+"", Toast.LENGTH_SHORT).show();

                VerifyOtpPojo output =response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.getStatus().equals("Success")&&output.getDetails().equals("OTP Matched")) {
                    //    Toast.makeText(getApplicationContext(),"OTP Matched", Toast.LENGTH_SHORT).show();

                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),true);

                        Intent intent = new Intent(LoginMobOtpActivity.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }


                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid OTP", Toast.LENGTH_SHORT).show();
                    }

                }

                else

                {
                    Toast.makeText(getApplicationContext(),"Something went wrong output", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<VerifyOtpPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("(|^)\\d{6}");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

              //  Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                editTextOTP.setText(parseCode(message));
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    @Override
    public void onInitiated(String response) {

        pDialog.dismiss();

        Toast.makeText(LoginMobOtpActivity.this,"OTP Sent",Toast.LENGTH_SHORT).show();

        c = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewResendOTP.setText("Resend After : " + millisUntilFinished / 1000 + "s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                textViewResendOTP.setText("Resend OTP ?");
                textViewVoiceOTP.setText("Receive OTP By Call ?");
            }

        }.start();

    }

    @Override
    public void onInitiationFailed(Exception paramException) {

        System.out.println("Verification initialization failed: " + paramException.getMessage());
        System.out.println("Failed due to"+ paramException.getCause().toString());

        Toast.makeText(LoginMobOtpActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
        onBackPressed();

    }

    @Override
    public void onVerified(String response) {

        Toast.makeText(this,"Verified",Toast.LENGTH_SHORT).show();
  //      Intent intent = new Intent(getApplicationContext(), Dashboard.class);

//        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),true);
//        UserSharedPreferenceData.setLoggedInUserPhn(getApplicationContext(),phn);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);

        saveNumber();

    }

    private void saveNumber() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        // show it
        apiClientUpdateMobile = ApiClientBase.getApiClient().create(ApiClientUpdateMobile.class);

        String url = "UpdateMobile?email="+UserSharedPreferenceData.getLoggedInUserID(this)+"&mobile="+code+phn;

        Call<String> call= apiClientUpdateMobile.updateMobile(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //    Toast.makeText(getApplicationContext(), new Gson().toJson(response.body())+"", Toast.LENGTH_SHORT).show();

                String output =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.equals("1")) {
                        //    Toast.makeText(getApplicationContext(),"OTP Matched", Toast.LENGTH_SHORT).show();

                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),true);
                        UserSharedPreferenceData.setLoggedInUserPhn(getApplicationContext(),phn);

                        Intent intent = new Intent(LoginMobOtpActivity.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                    else if(output.equals("0"))

                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                    }


                }

                else

                {
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
    public void onVerificationFailed(Exception paramException) {

 //       System.out.println("Verification failed: " + paramException.getMessage());
//        System.out.println("Failed due to"+ paramException.getCause().toString());
        Toast.makeText(LoginMobOtpActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
        //OTP  verification failed.

    }
}
