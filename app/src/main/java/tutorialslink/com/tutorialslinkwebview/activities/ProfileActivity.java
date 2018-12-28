package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import net.glxn.qrgen.android.QRCode;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewAuthorName,textViewLocation,textViewTechnologies,textViewAchievement,textViewWebsite,textViewAboutMe,textViewPhone;
    private ImageView imageViewBackBanner,imageViewQR;
    private CircleImageView imageViewProfilePicture;
    private LinearLayout layoutLocation,layoutTechnologies,layoutAchievements,layoutWebsite,layoutPhone;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;
    private LoginDetailPojo loginDetailPojo;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewAuthorName=findViewById(R.id.textViewAuthorName);
        textViewLocation=findViewById(R.id.textViewLocation);
        textViewTechnologies=findViewById(R.id.textViewTechnologies);
        textViewAchievement=findViewById(R.id.textViewAchievement);
        textViewWebsite=findViewById(R.id.textViewWebsite);
        textViewAboutMe=findViewById(R.id.textViewAboutMe);
        textViewPhone=findViewById(R.id.textViewPhone);


        layoutWebsite =findViewById(R.id.layoutWebsite);
        layoutLocation =findViewById(R.id.layoutLocation);
        layoutTechnologies =findViewById(R.id.layoutTechnologies);
        layoutAchievements =findViewById(R.id.layoutAchievements);
        layoutPhone=findViewById(R.id.layoutPhone);


        relativeLayout=findViewById(R.id.layout);
        relativeLayout.setVisibility(View.GONE);


        imageViewBackBanner=findViewById(R.id.imageViewBackBanner);
        imageViewProfilePicture=findViewById(R.id.imageViewProfilePicture);
        imageViewQR=findViewById(R.id.imageViewQR);

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://"+textViewWebsite.getText().toString()));
                startActivity(i);
            }
        });

        if(NetworkCheck.isNetworkAvailable(this))
        {
            getProfile();
        }

        else {

            Snackbar.make((CoordinatorLayout) findViewById(R.id.layout), "Network Unavailable", Snackbar.LENGTH_INDEFINITE).
                    setAction("Retry", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                            //    finish();
                            //    startActivity(getIntent());

                            recreate();


                        }
                    }).show();
        }
    }

    private void getProfile() {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Fetching Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email="+UserSharedPreferenceData.getLoggedInUserID(this);

        System.out.println("url               : " + url);

        Call<LoginDetailPojo> call= apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo =response.body();

              //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if(loginDetailPojo!=null) {
                    if(loginDetailPojo.getTable().size()>0) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        updateUI();
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


    private void updateUI() {

        textViewAuthorName.setText(loginDetailPojo.getTable().get(0).getFirst_name()+" " +loginDetailPojo.getTable().get(0).getLast_name());

        if(!loginDetailPojo.getTable().get(0).getCity().equals("")&&loginDetailPojo.getTable().get(0).getCountry()!=null) {
            textViewLocation.setText(loginDetailPojo.getTable().get(0).getCity() + " " + loginDetailPojo.getTable().get(0).getCountry());
        }
        else {

            layoutLocation.setVisibility(View.GONE);
        }

        if(!loginDetailPojo.getTable().get(0).getBlog_link().equals("")&&loginDetailPojo.getTable().get(0).getBlog_link()!=null) {
            textViewWebsite.setText(loginDetailPojo.getTable().get(0).getBlog_link());
        }

        else {
            layoutWebsite.setVisibility(View.GONE);
        }

        if(!loginDetailPojo.getTable().get(0).getTechnologies().equals("")&&loginDetailPojo.getTable().get(0).getTechnologies()!=null) {
            textViewTechnologies.setText(loginDetailPojo.getTable().get(0).getTechnologies());
        }
        else {

            layoutTechnologies.setVisibility(View.GONE);
        }
        if(!loginDetailPojo.getTable().get(0).getAwards().equals("")&&loginDetailPojo.getTable().get(0).getAwards()!=null) {
            textViewAchievement.setText(loginDetailPojo.getTable().get(0).getAwards());
        }

        else {
            layoutAchievements.setVisibility(View.GONE);
        }

        if(!loginDetailPojo.getTable().get(0).getMobile_number().equals("")&&loginDetailPojo.getTable().get(0).getMobile_number()!=null) {
            textViewPhone.setText(loginDetailPojo.getTable().get(0).getMobile_number());
        }

        else {
            layoutPhone.setVisibility(View.GONE);
        }

        if(!loginDetailPojo.getTable().get(0).getAbout_us().equals("")) {
            textViewAboutMe.setText(loginDetailPojo.getTable().get(0).getAbout_us());
        }

        if(!loginDetailPojo.getTable().get(0).getPicture().equals("")&&loginDetailPojo.getTable().get(0).getPicture()!=null) {
            if (loginDetailPojo.getTable().get(0).getPicture().startsWith("/")) {
                Picasso.with(this).load("https://tutorialslink.com" + loginDetailPojo.getTable().get(0).getPicture()).into(imageViewProfilePicture);
            } else {
                Picasso.with(this).load(loginDetailPojo.getTable().get(0).getPicture()).into(imageViewProfilePicture);
            }
        }

         if(loginDetailPojo.getTable().get(0).getBack_img()!=null&&!loginDetailPojo.getTable().get(0).getBack_img().equals("0")&&!loginDetailPojo.getTable().get(0).getBack_img().equals(""))
        {
            Picasso.with(this).load("https://tutorialslink.com" + loginDetailPojo.getTable().get(0).getBack_img()).into(imageViewBackBanner);
        }

        Bitmap myBitmap= QRCode.from(loginDetailPojo.getTable().get(0).getEmail()).bitmap();
        imageViewQR.setImageBitmap(myBitmap);

    }
}