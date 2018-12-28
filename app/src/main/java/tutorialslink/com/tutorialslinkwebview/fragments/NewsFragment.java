package tutorialslink.com.tutorialslinkwebview.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterHomeNews;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterRest;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetNews;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedTablePojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;


public class NewsFragment extends Fragment {

    private RecyclerView recyclerView,recyclerViewBreakinNews;
    private ApiClientGetNews apiClientGetNews;
    private FeedPojo feedPojo;
    private CustomAdapterRest customAdapterRest;
    private Snackbar snackbar;
    private CustomAdapterHomeNews customAdapterHomeNews;
    private LinearLayout layout;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        layout=view.findViewById(R.id.layout);
        layout.setVisibility(View.GONE);


        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        recyclerViewBreakinNews=(RecyclerView)view.findViewById(R.id.recyclerViewBreakinNews);
        recyclerViewBreakinNews.setHasFixedSize(true);
        recyclerViewBreakinNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerViewBreakinNews.setLayoutManager(new LinearLayoutManager(getContext()));


if(NetworkCheck.isNetworkAvailable(getContext()))
        getNews();

        else {

      snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Network Unavailable",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                    //    finish();
                    //    startActivity(getIntent());

                    getFragmentManager().beginTransaction()
                            .detach(NewsFragment.this)
                            .attach(NewsFragment.this)
                            .commit();


                }
            }).show();
}

        return view;
    }

    private void getNews() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading News...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetNews = ApiClientBase.getApiClient().create(ApiClientGetNews.class);
        Call<FeedPojo> call= apiClientGetNews.getNews();
        call.enqueue(new Callback<FeedPojo>() {
            @Override
            public void onResponse(Call<FeedPojo> call, Response<FeedPojo> response) {


                feedPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(feedPojo!=null&&feedPojo.getTable().size()>0) {

                    layout.setVisibility(View.VISIBLE);

                    FeedPojo feedPojo1 = new FeedPojo();
                    ArrayList<FeedTablePojo> feedTablePojo = new ArrayList<>();

                    for(int i = 0;i<5;i++) {
                        feedTablePojo.add(i, feedPojo.getTable().get(i));
                        feedPojo.getTable().remove(i);
                    }

                    customAdapterHomeNews= new CustomAdapterHomeNews(getContext(),feedTablePojo,getActivity());
                    recyclerViewBreakinNews.setAdapter(customAdapterHomeNews);

                    final LayoutAnimationController controller2 =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_right);

                    recyclerViewBreakinNews.setLayoutAnimation(controller2);
                    recyclerViewBreakinNews.getAdapter().notifyDataSetChanged();
                    recyclerViewBreakinNews.scheduleLayoutAnimation();

                    customAdapterRest=new CustomAdapterRest(getContext(),feedPojo.getTable(),getActivity());
                    recyclerView.setAdapter(customAdapterRest);
                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(feedPojo.getTable().size()==0)
                {
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<FeedPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
              //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();

                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                //    finish();
                                //    startActivity(getIntent());

                                getFragmentManager().beginTransaction()
                                        .detach(NewsFragment.this)
                                        .attach(NewsFragment.this)
                                        .commit();


                            }
                        }).show();

                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(snackbar!=null)
        snackbar.dismiss();
    }
}
