package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFollowFollowing;
import tutorialslink.com.tutorialslinkwebview.fragments.FollowersFragment;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientCheckFollow;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientFollow;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetFollowFollowingList;
import tutorialslink.com.tutorialslinkwebview.pojos.followfollwingpojo.FollowFollowingPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.TablePojo;

public class AuthorProfileActivity extends AppCompatActivity {


    private TextView textViewAuthorName,textViewLocation,textViewTechnologies,textViewAchievement,textViewWebsite,textViewAboutMe;
    private ImageView imageViewBackBanner;
    private CircleImageView imageViewProfilePicture;
    private String gData;
    private TablePojo loginDetailPojo;
    private LinearLayout layoutLocation,layoutTechnologies,layoutAchievements,layoutWebsite;
    private Button buttonFollowOp;
    private ApiClientFollow apiClientFollow;
    private ApiClientCheckFollow apiClientCheckFollow;
    private boolean isFollowing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_profile);

        gData=getIntent().getStringExtra("data");

        loginDetailPojo =new Gson().fromJson(gData,TablePojo.class);


        textViewAuthorName=findViewById(R.id.textViewAuthorName);
        textViewLocation=findViewById(R.id.textViewLocation);
        textViewTechnologies=findViewById(R.id.textViewTechnologies);
        textViewAchievement=findViewById(R.id.textViewAchievement);
        textViewWebsite=findViewById(R.id.textViewWebsite);
        textViewAboutMe=findViewById(R.id.textViewAboutMe);

        buttonFollowOp=findViewById(R.id.buttonFollowOp);


        layoutWebsite =findViewById(R.id.layoutWebsite);
        layoutLocation =findViewById(R.id.layoutLocation);
        layoutTechnologies =findViewById(R.id.layoutTechnologies);
        layoutAchievements =findViewById(R.id.layoutAchievements);



        imageViewBackBanner=findViewById(R.id.imageViewBackBanner);
        imageViewProfilePicture=findViewById(R.id.imageViewProfilePicture);

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://"+textViewWebsite.getText().toString()));
                startActivity(i);
            }
        });


        buttonFollowOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          //      Toast.makeText(AuthorProfileActivity.this,""+isFollowing,Toast.LENGTH_LONG).show();

                if(isFollowing)
                {
                    doUnfollow();
                }
                else {
                    doFollow();
                }

            }
        });

        if(loginDetailPojo.getEmail().equals(UserSharedPreferenceData.getLoggedInUserID(this)))
        {
            buttonFollowOp.setVisibility(View.GONE);
        }
        else {
            checkFollow();
        }

        updateUI();

    }

    private void doUnfollow() {

        String url = "Follow?authid="+loginDetailPojo.getEmail()+"&fid="+UserSharedPreferenceData.getLoggedInUserID(this)+"&type=UnFollow";
        apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
        Call<String> call= apiClientFollow.followOp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

              //           Toast.makeText(AuthorProfileActivity.this,"Unfollow "+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {
                    buttonFollowOp.setText("Follow");
                    isFollowing=false;
                }
                else
                {

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

/*
                Snackbar.make(findViewById(R.id.layout),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE).
                        setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        recreate();


                    }
                }).show();

                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());

                */

            }
        });


    }

    private void doFollow() {

        String url = "Follow?authid="+loginDetailPojo.getEmail()+"&fid="+UserSharedPreferenceData.getLoggedInUserID(this)+"&type=Follow";
        apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
        Call<String> call= apiClientFollow.followOp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

            //         Toast.makeText(AuthorProfileActivity.this,""+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {
                    buttonFollowOp.setText("Unfollow");
                    buttonFollowOp.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    isFollowing=true;

                }
                else
                {

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

/*
                Snackbar.make(findViewById(R.id.layout),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE).
                        setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        recreate();


                    }
                }).show();

                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());

                */

            }
        });

    }

    private void checkFollow() {


        // show it
        String url = "FollowCheck?authid="+loginDetailPojo.getEmail()+"&fid="+UserSharedPreferenceData.getLoggedInUserID(this);
        apiClientCheckFollow = ApiClientBase.getApiClient().create(ApiClientCheckFollow.class);
        Call<String> call= apiClientCheckFollow.check(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

                //     Toast.makeText(getContext(),followFollowingPojo.toString(),Toast.LENGTH_SHORT).show();

                if(res.equals("0"))
                {
                    isFollowing=false;
                }
                else
                {
                    buttonFollowOp.setText("Unfollow");
                    isFollowing=true;
                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

/*
                Snackbar.make(findViewById(R.id.layout),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE).
                        setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        recreate();


                    }
                }).show();

                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());

                */

            }
        });



    }


    private void updateUI() {

        textViewAuthorName.setText(loginDetailPojo.getFirst_name()+" " + loginDetailPojo.getLast_name());

        if(loginDetailPojo.getCountry()!=null&&!loginDetailPojo.getCity().equals("")) {
            textViewLocation.setText(loginDetailPojo.getCity() + " " + loginDetailPojo.getCountry());
        }
        else {

            layoutLocation.setVisibility(View.GONE);
        }

        if(loginDetailPojo.getBlog_link()!=null&&!loginDetailPojo.getBlog_link().equals("")) {
            textViewWebsite.setText(loginDetailPojo.getBlog_link());
        }

        else {
            layoutWebsite.setVisibility(View.GONE);
        }

        if(loginDetailPojo.getTechnologies()!=null&&!loginDetailPojo.getTechnologies().equals("")) {
            textViewTechnologies.setText(loginDetailPojo.getTechnologies());
        }
        else {

            layoutTechnologies.setVisibility(View.GONE);
        }
        if(loginDetailPojo.getAwards()!=null&&!loginDetailPojo.getAwards().equals("")) {
            textViewAchievement.setText(loginDetailPojo.getAwards());
        }

        else {
            layoutAchievements.setVisibility(View.GONE);
        }

        if(loginDetailPojo.getAbout_us()!=null&&!loginDetailPojo.getAbout_us().equals("")) {
            textViewAboutMe.setText(loginDetailPojo.getAbout_us());
        }

        if(loginDetailPojo.getPicture()!=null&&!loginDetailPojo.getPicture().equals("")) {
            if (loginDetailPojo.getPicture().startsWith("/")) {
                Picasso.with(this).load("https://tutorialslink.com" + loginDetailPojo.getPicture()).into(imageViewProfilePicture);
            } else {
                Picasso.with(this).load(loginDetailPojo.getPicture()).into(imageViewProfilePicture);
            }
        }

        if(loginDetailPojo.getBack_img()!=null&&!loginDetailPojo.getBack_img().equals("0")&&!loginDetailPojo.getBack_img().equals(""))
        {
            Picasso.with(this).load("https://tutorialslink.com" + loginDetailPojo.getBack_img()).into(imageViewBackBanner);
        }

    }
}
