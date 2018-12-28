package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.Dashboard;
import tutorialslink.com.tutorialslinkwebview.activities.DetailActivity;
import tutorialslink.com.tutorialslinkwebview.activities.LoginActivity;
import tutorialslink.com.tutorialslinkwebview.activities.VideoActivity;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUnpin;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedTablePojo;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo.PinnedPostPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CustomAdapterMyDashboard extends RecyclerView.Adapter<CustomAdapterMyDashboard.ViewHolder>
{

    private Context context;
    private Activity activity;
    private PinnedPostPojo pinnedPostPojo;
    private ApiClientGetArticleDetail apiClientGetArticleDetail;
    private DetailPojo detailPojo;
    private ApiClientUnpin apiClientUnpin;
    int re = 0;
    private Handler handler;
    private Runnable delayRunnable;
    private FrameLayout frameLayout;


    public CustomAdapterMyDashboard(Context context, PinnedPostPojo pinnedPostPojo, Activity activity, FrameLayout frameLayout) {

        this.context = context;
        this.activity=activity;
        this.pinnedPostPojo=pinnedPostPojo;
        this.frameLayout=frameLayout;

    }


    @Override
    public CustomAdapterMyDashboard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_my_dashboard, parent, false);
        CustomAdapterMyDashboard.ViewHolder holder = new CustomAdapterMyDashboard.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterMyDashboard.ViewHolder holder,int position)
    {

        holder.textViewTitle.setText(pinnedPostPojo.getTable().get(position).getTitle());


        String[] str_chop = pinnedPostPojo.getTable().get(position).getImage().split(",");

        Picasso.with(context).load("https://tutorialslink.com"+str_chop[0]).into(holder.imageView);


    }

    private void unpin(String sr_no, final int position, final View view,final View view1) {


        final ProgressDialog pDialog = new ProgressDialog(context);


        pDialog.setMessage("UnPinning...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = "Sp_User_PinPost_delete?author_id="+ UserSharedPreferenceData.getLoggedInUserID(context)+"&postid="+sr_no;
        // show it
        apiClientUnpin = ApiClientBase.getApiClient().create(ApiClientUnpin.class);
        Call<String> call= apiClientUnpin.unpin(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();


                //               Toast.makeText(context,response.body().toString(),Toast.LENGTH_SHORT).show();
                if(res!=null) {

//                  Toast.makeText(context,detailPojo.toString(),Toast.LENGTH_SHORT).show();

                    if(res.equals("1"))
                    {
                        handler = new Handler();
                        delayRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                            }
                        };
                        handler.postDelayed(delayRunnable, 100);

                        Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
                        view.startAnimation(scaleDown);


                       handler = new Handler();
                        delayRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub


                                Animation fallDown = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);
                                view1.startAnimation(fallDown);
                            }
                        };
                        handler.postDelayed(delayRunnable, 400);



                        handler = new Handler();
                        delayRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub


                                  pinnedPostPojo.getTable().remove(position);
                                  notifyItemRemoved(position);

                                  if(pinnedPostPojo.getTable().size()==0)
                                  {

                                      ImageView imageViewEmpyCart=new ImageView(context);
                                      imageViewEmpyCart.setImageResource(R.drawable.sticker);
                                      imageViewEmpyCart.setVisibility(View.VISIBLE);
                                      frameLayout.addView(imageViewEmpyCart);

                                  }
                              //    Toast.makeText(context,"Unpinned From Dashboard", Toast.LENGTH_SHORT).show();

                            }
                        };
                        handler.postDelayed(delayRunnable, 650);

                    }
                    else if(res.equals("0")) {
                        Toast.makeText(context,"Try again Later",Toast.LENGTH_SHORT).show();

                    }

                }

                else
                {
               //     Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle;
        ImageView imageView,imageViewUnpin;
        RelativeLayout layout;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            imageViewUnpin=view.findViewById(R.id.imageViewUnpin);
            layout = view.findViewById(R.id.layout);

            imageViewUnpin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Unpin From Dashboard");
                    builder.setMessage("Are You Sure?");
                    builder.setPositiveButton("Unpin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String sr_no = pinnedPostPojo.getTable().get(getPosition()).getPost_id();
                            unpin(sr_no,getPosition(),imageViewUnpin,layout);


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


        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {
                String sr_no = pinnedPostPojo.getTable().get(getPosition()).getPost_id();
                String cat = pinnedPostPojo.getTable().get(getPosition()).getCategory();

                    getDetails(sr_no,cat);
                //   Toast.makeText(context,sr_no,Toast.LENGTH_SHORT).show();

            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void getDetails(String sr_no, final String cat) {

        final ProgressDialog pDialog = new ProgressDialog(context);

        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetArticleDetail = ApiClientBase.getApiClient().create(ApiClientGetArticleDetail.class);
        Call<DetailPojo> call= apiClientGetArticleDetail.getArticle("Article?id="+sr_no);
        call.enqueue(new Callback<DetailPojo>() {
            @Override
            public void onResponse(Call<DetailPojo> call, Response<DetailPojo> response) {


                detailPojo=response.body();


                //               Toast.makeText(context,response.body().toString(),Toast.LENGTH_SHORT).show();
                if(detailPojo!=null&&detailPojo.getTable().size()>0) {

//                  Toast.makeText(context,detailPojo.toString(),Toast.LENGTH_SHORT).show();

                    if(cat.equals("4"))
                    {
                        Intent intent=new Intent(context, VideoActivity.class);
                        intent.putExtra("data",new Gson().toJson(detailPojo));
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("data", new Gson().toJson(detailPojo));
                        context.startActivity(intent);
                    }

                    System.out.println(detailPojo.toString());

                }

                else if(detailPojo==null||detailPojo.getTable().size()==0)
                {
                    Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<DetailPojo> call, Throwable t) {

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

        return pinnedPostPojo.getTable().size();
    }


}