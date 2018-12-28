package tutorialslink.com.tutorialslinkwebview.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.nio.BufferUnderflowException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientCheckFollow;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientFollow;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailsTablePojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BottomSheetDialogFragment {

    private TextView textViewAuthorName,textViewLocation,textViewTechnologies,textViewAchievement,textViewWebsite,textViewAboutMe;
    private ImageView imageViewBackBanner;
    private CircleImageView imageViewProfilePicture;
    private String gData;
    private DetailsTablePojo detailsTablePojo;
    private LinearLayout layoutLocation,layoutTechnologies,layoutAchievements,layoutWebsite;
    private Button buttonFollowOp;
    private ApiClientFollow apiClientFollow;
    private ApiClientCheckFollow apiClientCheckFollow;
    private boolean isFollowing=false;


    public ProfileFragment() {
        // Required empty public constructor
    }

    static public ProfileFragment newInstance (String data)
    {

        ProfileFragment f = new ProfileFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("data", data);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        setHasOptionsMenu(true);
        gData = getArguments().getString("data");
        initialize();

    }

    private void initialize() {

        detailsTablePojo=new Gson().fromJson(gData,DetailsTablePojo.class);

    }

    private void updateUI() {

        textViewAuthorName.setText(detailsTablePojo.getFirst_name()+" " +detailsTablePojo.getLast_name());

        if(!detailsTablePojo.getCity().equals("")&&detailsTablePojo.getCountry()!=null) {
            textViewLocation.setText(detailsTablePojo.getCity() + " " + detailsTablePojo.getCountry());
        }
        else {

            layoutLocation.setVisibility(View.GONE);
        }

        if(!detailsTablePojo.getBlog_link().equals("")&&detailsTablePojo.getBlog_link()!=null) {
            textViewWebsite.setText(detailsTablePojo.getBlog_link());
        }

        else {
                layoutWebsite.setVisibility(View.GONE);
        }

        if(!detailsTablePojo.getTechnologies().equals("")&&detailsTablePojo.getTechnologies()!=null) {
            textViewTechnologies.setText(detailsTablePojo.getTechnologies());
        }
        else {

            layoutTechnologies.setVisibility(View.GONE);
        }
        if(!detailsTablePojo.getAwards().equals("")&&detailsTablePojo.getAwards()!=null) {
            textViewAchievement.setText(detailsTablePojo.getAwards());
        }

        else {
            layoutAchievements.setVisibility(View.GONE);
        }

        if(!detailsTablePojo.getAbout_us().equals("")) {
            textViewAboutMe.setText(detailsTablePojo.getAbout_us());
        }

        if(!detailsTablePojo.getPicture().equals("")&&detailsTablePojo.getPicture()!=null) {
            if (detailsTablePojo.getPicture().startsWith("/")) {
                Picasso.with(getActivity()).load("https://tutorialslink.com" + detailsTablePojo.getPicture()).into(imageViewProfilePicture);
            } else {
                Picasso.with(getActivity()).load(detailsTablePojo.getPicture()).into(imageViewProfilePicture);
            }
        }

        if(!detailsTablePojo.getBack_img().equals("0")&&detailsTablePojo.getBack_img()!=null&&!detailsTablePojo.getBack_img().equals(""))
        {
            Picasso.with(getActivity()).load("https://tutorialslink.com" + detailsTablePojo.getBack_img()).into(imageViewBackBanner);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewAuthorName=view.findViewById(R.id.textViewAuthorName);
        textViewLocation=view.findViewById(R.id.textViewLocation);
        textViewTechnologies=view.findViewById(R.id.textViewTechnologies);
        textViewAchievement=view.findViewById(R.id.textViewAchievement);
        textViewWebsite=view.findViewById(R.id.textViewWebsite);
        textViewAboutMe=view.findViewById(R.id.textViewAboutMe);


        layoutWebsite =view.findViewById(R.id.layoutWebsite);
        layoutLocation =view.findViewById(R.id.layoutLocation);
        layoutTechnologies =view.findViewById(R.id.layoutTechnologies);
        layoutAchievements =view.findViewById(R.id.layoutAchievements);


        buttonFollowOp=view.findViewById(R.id.buttonFollowOp);


        imageViewBackBanner=view.findViewById(R.id.imageViewBackBanner);
        imageViewProfilePicture=view.findViewById(R.id.imageViewProfilePicture);

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://"+textViewWebsite.getText().toString()));
                startActivity(i);
            }
        });

        updateUI();

        buttonFollowOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFollowing)
                {
                    doUnfollow();
                }
                else {
                    doFollow();
                }

            }
        });

        if(detailsTablePojo.getCreated_by().equals(UserSharedPreferenceData.getLoggedInUserID(getContext())))
        {
            buttonFollowOp.setVisibility(View.GONE);
        }
        else {
            checkFollow();
        }


        return view;
    }

        private void doUnfollow() {

            String url = "Follow?authid=" + detailsTablePojo.getCreated_by() + "&fid=" + UserSharedPreferenceData.getLoggedInUserID(getContext()) + "&type=UnFollow";
            apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
            Call<String> call = apiClientFollow.followOp(url);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {


                    String res = response.body();

                    //     Toast.makeText(getContext(),"Unfollow "+res,Toast.LENGTH_SHORT).show();

                    if (res != null && res.equals("1")) {
                        buttonFollowOp.setText("Follow");
                        isFollowing = false;
                    } else {

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

        private void doFollow(){
            String url = "Follow?authid="+detailsTablePojo.getCreated_by()+"&fid="+UserSharedPreferenceData.getLoggedInUserID(getContext())+"&type=Follow";
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
        String url = "FollowCheck?authid="+detailsTablePojo.getCreated_by()+"&fid="+UserSharedPreferenceData.getLoggedInUserID(getContext());
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

}
