package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientPostSocialLoginData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUserEmailLogin;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;


public class LoginActivity extends AppCompatActivity {

    private ImageView imageViewGoogleLogin,imageViewFacebookLogin,imageViewTwitterLogin;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    private TwitterAuthClient mTwitterAuthClient;
    private Button buttonSignIn;
    private TextInputLayout textInputLayoutUserEmail,textInputLayoutPassword;
    private TextInputEditText editTextEmail,editTextPassword;
    private TextView textViewRegister,textViewForgetPassword;
    private ApiClientUserEmailLogin apiClientUserEmailLogin;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;
    private ApiClientPostSocialLoginData apiClientPostSocialLoginData;
    private LoginDetailPojo loginDetailPojo;
    private ImageView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Twitter.initialize(this);
        mTwitterAuthClient= new TwitterAuthClient();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();



        imageViewGoogleLogin = (ImageView)findViewById(R.id.imageViewGoogleLogin);
        imageViewFacebookLogin = (ImageView)findViewById(R.id.imageViewFacebookLogin);
        imageViewTwitterLogin = (ImageView)findViewById(R.id.imageViewTwitterLogin);

        buttonSignIn = findViewById(R.id.buttonSignIn);

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail=findViewById(R.id.editTextEmail);

        textInputLayoutUserEmail=findViewById(R.id.textInputLayoutUserEmail);
        textInputLayoutPassword=findViewById(R.id.textInputLayoutPassword);

        textViewForgetPassword=findViewById(R.id.textViewForgetPassword);
        textViewRegister = findViewById(R.id.textViewRegister);

        videoView = findViewById(R.id.videoView);


        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://tutorialslink.com/Home/Userregister"));
//                startActivity(i);

                startActivity(new Intent(LoginActivity.this,SignupActivity.class));

            }
        });

        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://tutorialslink.com/Home/ForgotPassword"));
//                startActivity(i);

                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));

            }
        });

        String path = "android.resource://" + getPackageName() + "/" + R.raw.output;

        //videoView.setVisibility(View.GONE);
        //videoView.setVideoURI(Uri.parse(path));
        //videoView.start();

//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });




        mProgress = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getApplicationContext())) {
                    if(isValidated()) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        emailSignIn();
                    }


                }

                else
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    showError();
                }

            }
        });



        imageViewGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getApplicationContext()))
                googleLogin();

                else
                {
                    showError();
                }


            }
        });

        imageViewFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getApplicationContext()))
                facebookLogin();

                else
                {
                    showError();
                }

            }
        });


        imageViewTwitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getApplicationContext()))
                twitterLoginn();

                else
                {
                    showError();
                }


            }
        });


    }

    private boolean isValidated() {


        if(editTextEmail.getText().toString().equals(""))
        {
            editTextEmail.setError("Enter Email");
            editTextEmail.requestFocus();

            return false;
        }

        else if(editTextPassword.getText().toString().equals(""))
        {
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            return false;
        }


        return true;
    }


    private void showError() {

        Snackbar.make((RelativeLayout)findViewById(R.id.relativelayoutView),"Network Unavailable",Snackbar.LENGTH_SHORT).show();
    }

    private void emailSignIn() {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientUserEmailLogin = ApiClientBase.getApiClient().create(ApiClientUserEmailLogin.class);

        String url = "login?email="+editTextEmail.getText().toString()+"&pass="+editTextPassword.getText().toString();

        Call<String> call= apiClientUserEmailLogin.SignIn(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String output =response.body();

            //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

                    if(output.equals("1")) {
                     //   Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_SHORT).show();
                        
                        getLoginDetails(editTextEmail.getText().toString());
                    }

                    else if(output.equals("0"))
                    {
                        Snackbar.make(findViewById(R.id.relativelayoutView),"Invalid Credentials", Snackbar.LENGTH_LONG).show();
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

    private void getLoginDetails(String id) {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Logging In");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email="+id;

        Call<LoginDetailPojo> call= apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(loginDetailPojo!=null) {

                    UserSharedPreferenceData.setLoggedInUserID(getApplicationContext(),loginDetailPojo.getTable().get(0).getEmail());
                    UserSharedPreferenceData.setLoggedInUserName(getApplicationContext(),loginDetailPojo.getTable().get(0).getFirst_name()+" "+loginDetailPojo.getTable().get(0).getLast_name());
                    UserSharedPreferenceData.setPrefLoggedinUserProf(getApplicationContext(),loginDetailPojo.getTable().get(0).getPicture().toString());
                    UserSharedPreferenceData.setPrefLoggedinUserProfBack(getApplicationContext(),loginDetailPojo.getTable().get(0).getBack_img());
                    UserSharedPreferenceData.setUserLoggedInStatus(getApplicationContext(),true);
                    UserSharedPreferenceData.setUserLoggedInWith(getApplicationContext(),"Email");

                    if(loginDetailPojo.getTable().get(0).getMobile_number().equals("")) {
                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(), false);
                        Intent intent = new Intent(getApplicationContext(),LoginMobActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    else {
                        UserSharedPreferenceData.setLoggedInUserPhn(getApplicationContext(),loginDetailPojo.getTable().get(0).getMobile_number());
                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(), true);

                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }



                }

                else

                {
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<LoginDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void twitterLoginn() {

        mProgress.setMessage("Logging in... ");
        mProgress.show();
        mProgress.setCancelable(false);

        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                // Success

             //   Toast.makeText(LoginActivity.this,"Success"+ twitterSessionResult.data.getUserName() ,Toast.LENGTH_SHORT).show();

                handleTwitterSession(twitterSessionResult.data);
                mProgress.dismiss();

            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });


    }

    private void facebookLogin() {

        LoginManager.getInstance().logOut();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    //    Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Canceled",
                                Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        // App code
                    }
                });



        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));




    }

    private void googleLogin() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    //     Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            mProgress.setMessage("Logging in... ");
            mProgress.show();
            mProgress.setCancelable(false);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                mProgress.dismiss();
                // Google Sign In failed, update UI appropriately
                // ...
            }


            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }


       else {
           callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);


    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                          System.out.println("signInWithCredential : "+ task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + "signInWithCredential : "+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            mProgress.dismiss();
                        }

                        else{

                         //   Toast.makeText(LoginActivity.this, account.getDisplayName()+" "+account.getEmail(),Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();


                            UserSharedPreferenceData.setLoggedInUserID(getApplication(),account.getEmail());
                            UserSharedPreferenceData.setLoggedInUserName(getApplicationContext(),account.getDisplayName());
                            UserSharedPreferenceData.setPrefLoggedinUserProf(getApplicationContext(),account.getPhotoUrl().toString());
                            UserSharedPreferenceData.setUserLoggedInWith(getApplicationContext(),"Google");
                         //   account.getEmail();
                            UserSharedPreferenceData.setUserLoggedInStatus(getApplicationContext(),true);

                            postAndCheckSocialData(account.getEmail(),account.getGivenName(),account.getFamilyName(),account.getPhotoUrl().toString(),"");

//                            UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),false);
//
//                            Intent intent = new Intent(getApplicationContext(),LoginMobActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        }
                        // ...
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
      //  Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                       //     Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                         //   FirebaseUser user = mAuth.getCurrentUser();


                            Profile profile = Profile.getCurrentProfile();
                            //  Toast.makeText(LoginActivity.this,"Welcome "+profile.getName(),Toast.LENGTH_SHORT).show();


                        UserSharedPreferenceData.setLoggedInUserID(getApplicationContext(),profile.getId());
                        UserSharedPreferenceData.setLoggedInUserName(getApplicationContext(),profile.getName());
                        UserSharedPreferenceData.setPrefLoggedinUserProf(getApplicationContext(),profile.getProfilePictureUri(100,100).toString());
                        UserSharedPreferenceData.setUserLoggedInStatus(getApplicationContext(),true);
                        UserSharedPreferenceData.setUserLoggedInWith(getApplicationContext(),"Facebook");

                        postAndCheckSocialData(profile.getId(),profile.getFirstName(),profile.getLastName(),profile.getProfilePictureUri(100,100).toString(),profile.getLinkUri().toString());


//                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),false);
//
//                  //    Toast.makeText(getApplicationContext(),profile.getId(),Toast.LENGTH_LONG).show();
//
//                        Intent intent = new Intent(getApplicationContext(),LoginMobActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);



                           // Toast.makeText(LoginActivity.this,user.getDisplayName(),Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("signInWithCredential:failure "+ task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        //    updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleTwitterSession(TwitterSession session) {
     //   Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                        //    Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                        //    Toast.makeText(LoginActivity.this, user.getDisplayName(), Toast.LENGTH_SHORT).show();

                            UserSharedPreferenceData.setLoggedInUserID(getApplicationContext(),user.getUid());
                            UserSharedPreferenceData.setLoggedInUserName(getApplicationContext(),user.getDisplayName());
                            UserSharedPreferenceData.setPrefLoggedinUserProf(getApplicationContext(),user.getPhotoUrl().toString());
                            UserSharedPreferenceData.setUserLoggedInWith(getApplicationContext(),"Twitter");
                            UserSharedPreferenceData.setUserLoggedInStatus(getApplicationContext(),true);

                            postAndCheckSocialData(user.getUid(),user.getDisplayName(),"`",user.getPhotoUrl().toString(),"0");


//                            UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),false);
//
//                           // Toast.makeText(getApplicationContext(),user.getUid(),Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(getApplicationContext(),LoginMobActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                       //     Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                         //   updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void postAndCheckSocialData(String email,String fname,String lname,String picture,String link) {


        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientPostSocialLoginData = ApiClientBase.getApiClient().create(ApiClientPostSocialLoginData.class);

        String url = "AddUser";

        System.out.println("URL :" + url  + " " + fname + " " + lname + " " + email + " " + picture + " " + link);

        Call<String> call= apiClientPostSocialLoginData.postData(fname,lname,email,picture,link);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String output =response.body();

                //    Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(output!=null) {

               //     Toast.makeText(getApplicationContext(),output,Toast.LENGTH_LONG).show();

                    if(output.equals("0")) {

                        //all good
                      //  Toast.makeText(getApplicationContext(),"Something went wrong, try again",Toast.LENGTH_SHORT).show();

                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(), true);

                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                    else if(output.equals("1"))
                    {

                        //new user

                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(), true);

                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }

                    else if(output.equals("2"))
                    {

                        //mob screen

                        UserSharedPreferenceData.setUserPhnStatus(getApplicationContext(),false);

                        Intent intent = new Intent(getApplicationContext(),LoginMobActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                      //  videoView.stopPlayback();


                    }

                    else if(output.equals("3"))
                    {
                        Toast.makeText(getApplicationContext(),"Operation failed try agian",Toast.LENGTH_LONG).show();
                        UserSharedPreferenceData.clearPref(LoginActivity.this);
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"this : "+output,Toast.LENGTH_LONG).show();
                        UserSharedPreferenceData.clearPref(LoginActivity.this);
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
    protected void onPostResume() {
        super.onPostResume();
     //   videoView.resume();
    }
}
