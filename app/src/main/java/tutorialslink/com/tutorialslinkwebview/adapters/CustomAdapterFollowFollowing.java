package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.AuthorProfileActivity;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientFollow;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.authorSearchPojo.AuthorSearchPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.followfollwingpojo.FollowFollowingPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CustomAdapterFollowFollowing extends RecyclerView.Adapter<CustomAdapterFollowFollowing.ViewHolder>
{
    private Context context;
    private Activity activity;
    private FollowFollowingPojo followFollowingPojo;
    private LoginDetailPojo loginDetailPojo;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;
    private ApiClientFollow apiClientFollow;
    private boolean isFollowing=false;



    public CustomAdapterFollowFollowing(Context context, FollowFollowingPojo followFollowingPojo, Activity activity)
    {

        this.context = context;
        this.activity=activity;
        this.followFollowingPojo=followFollowingPojo;

    }


    @Override
    public CustomAdapterFollowFollowing.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_authors_profile, parent, false);
        CustomAdapterFollowFollowing.ViewHolder holder = new CustomAdapterFollowFollowing.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterFollowFollowing.ViewHolder holder, final int position)
    {

        holder.textViewTitle.setText(followFollowingPojo.getTable().get(position).getFirst_name()+" "+followFollowingPojo.getTable().get(position).getLast_name());
        if(followFollowingPojo.getTable().get(position).getAbout_us()!=null&&!followFollowingPojo.getTable().get(position).getAbout_us().equals("")) {
            holder.textViewAbout.setText(followFollowingPojo.getTable().get(position).getAbout_us());
        }
        else {
            holder.textViewAbout.setText("No Information");
        }
//        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        String[] str_chop = followFollowingPojo.getTable().get(position).getPicture().split(",");

//        System.out.println(""+str_chop[0]);
        if(str_chop.length>0) {
            if(str_chop[0].contains("http"))
            {
                Picasso.with(context).load(str_chop[0]).into(holder.imageView);
            }
            else if(!str_chop[0].equals("")){
                Picasso.with(context).load("https://tutorialslink.com/" + str_chop[0]).into(holder.imageView);
            }

            if(followFollowingPojo.getTable().get(position).getAuthFollowing()>0)
            {
                holder.textViewIsFollowing.setVisibility(View.VISIBLE);
            }
            else {
                holder.textViewIsFollowing.setVisibility(View.GONE);
            }
        }

        if(followFollowingPojo.getTable().get(position).getIsfollowing()>0)
        {
            holder.buttonFollowOp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_corner_following_button));
            holder.buttonFollowOp.setTextColor(context.getResources().getColor(R.color.white));
            holder.buttonFollowOp.setText("Following");
        }

        else {
            holder.buttonFollowOp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_corner_follow_button));
            holder.buttonFollowOp.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.buttonFollowOp.setText("Follow");
        }

        holder.buttonFollowOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(followFollowingPojo.getTable().get(position).getIsfollowing()>0)
                {
                    doUnfollow(followFollowingPojo.getTable().get(position).getEmail(),position);
                }
                else
                    {
                        doFollow(followFollowingPojo.getTable().get(position).getEmail(),position);
                }

            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle,textViewAbout,textViewIsFollowing;
        CircleImageView imageView;
        Button buttonFollowOp;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=view.findViewById(R.id.textViewTitle);
            textViewAbout=view.findViewById(R.id.textViewAbout);
            textViewIsFollowing=view.findViewById(R.id.textViewIsFollowing);
            imageView=view.findViewById(R.id.imageView);
            buttonFollowOp=view.findViewById(R.id.buttonFollowOp);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {

                getAuthorDetails(followFollowingPojo.getTable().get(getPosition()).getEmail());
            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void getAuthorDetails(String id) {

        final ProgressDialog pDialog = new ProgressDialog(context);


        pDialog.setMessage("Fetching Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email="+id;

        System.out.println("url : " + url);

        Call<LoginDetailPojo> call= apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo =response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if(loginDetailPojo!=null) {
                    if(loginDetailPojo.getTable().size()>0) {

                        Intent intent = new Intent(context, AuthorProfileActivity.class);
                        intent.putExtra("data",new Gson().toJson(loginDetailPojo.getTable().get(0)));
                        context.startActivity(intent);
                    }
                }

                else
                {
                    Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LoginDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }


    @Override
    public int getItemCount() {

        return followFollowingPojo.getTable().size();
    }

    private void doUnfollow(String email, final int position) {

        String url = "Follow?authid="+email+"&fid="+ UserSharedPreferenceData.getLoggedInUserID(context)+"&type=UnFollow";
        apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
        Call<String> call= apiClientFollow.followOp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res =response.body();

          //      Toast.makeText(context,"Unfollow "+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {
             //       buttonFollowOp.setText("Follow");
                    followFollowingPojo.getTable().get(position).setIsfollowing(0);
                    notifyDataSetChanged();
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

    private void doFollow(String email, final int position) {

        String url = "Follow?authid="+email+"&fid="+UserSharedPreferenceData.getLoggedInUserID(context)+"&type=Follow";
        apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
        Call<String> call= apiClientFollow.followOp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();

                //         Toast.makeText(AuthorProfileActivity.this,""+res,Toast.LENGTH_SHORT).show();

                if(res!=null&&res.equals("1"))
                {
               //     buttonFollowOp.setText("Unfollow");
               //     buttonFollowOp.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

                    followFollowingPojo.getTable().get(position).setIsfollowing(1);
                    notifyDataSetChanged();

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


}
