package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFeed;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetFeed;
import tutorialslink.com.tutorialslinkwebview.pojos.feedPojo.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private Snackbar snackbar;
    private ApiClientGetFeed apiClientGetFeed;
    private boolean start = true;
    private int pageIndex=1,pageSize=10;
    private FeedPojo feedPojo;
    private RecyclerView recyclerView;
    private String flag="0";
    private LinearLayoutManager mLayoutManager;
    private CustomAdapterFeed customAdapterFeed;



    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);


        mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        //  recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(mLayoutManager);

        if(NetworkCheck.isNetworkAvailable(getContext()))
        {
            getfeeds();
        }
        else {
            snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Network Unavailable",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                    //    finish();
                    //    startActivity(getIntent());

                    getFragmentManager().beginTransaction()
                            .detach(FeedFragment.this)
                            .attach(FeedFragment.this)
                            .commit();

                }
            }).show();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                flag="0";
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();


                if (flag.compareTo("0")==0) {

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE)
                    {
                     //   Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                        if(feedPojo.getTable().size()>=pageIndex*pageSize) {
                            Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                            flag = "1";
                            pageIndex++;
                            getfeeds();
                            return;
                        }

                    }
                }
                //   Toast.makeText(ItemsDisplay.this, flag, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getfeeds() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());

        if(start) {
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // show it
        apiClientGetFeed = ApiClientBase.getApiClient().create(ApiClientGetFeed.class);

        //  String url = "UserEmail?email="+ UserSharedPreferenceData.getLoggedInUserID(this);
        String url = "AuhtorFeeds?authid="+ UserSharedPreferenceData.getLoggedInUserID(getContext())+"&pageindex="+pageIndex+"&pagesize="+pageSize;

        System.out.println("url  : " + url);

        Call<FeedPojo> call= apiClientGetFeed.getFeeds(url);
        call.enqueue(new Callback<FeedPojo>() {
            @Override
            public void onResponse(Call<FeedPojo> call, Response<FeedPojo> response) {

                if(start)
                {
                    feedPojo =response.body();
                }
                else {
                    feedPojo.getTable().addAll(response.body().getTable());
                }

            //    System.out.println(" Pojo    "+feedPojo.getTable().toString());

                if(feedPojo!=null) {
                    if(feedPojo.getTable().size()>0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        // updateUI();
             //          Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                       if(start) {
                           customAdapterFeed = new CustomAdapterFeed(getContext(), feedPojo, getActivity());
                           recyclerView.setAdapter(customAdapterFeed);
                           final LayoutAnimationController controller =
                                   AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                           recyclerView.setLayoutAnimation(controller);
                           recyclerView.getAdapter().notifyDataSetChanged();
                           recyclerView.scheduleLayoutAnimation();
                           start=false;
                       }

                       else {
                           if (customAdapterFeed!=null)
                               customAdapterFeed.notifyDataSetChanged();
                       }

                    }

                    else if(feedPojo.getTable().size()==0) {

                        recyclerView.setVisibility(View.GONE);
                    }


                }

                else
                {
                    Toast.makeText(getContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                if (pageIndex==1)
                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<FeedPojo> call, Throwable t) {

                if(pageIndex==1)
                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }

        });


    }


}
