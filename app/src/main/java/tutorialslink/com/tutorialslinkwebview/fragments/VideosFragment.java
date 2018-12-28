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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterVideo;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetVideo;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;


public class VideosFragment extends Fragment

{

    private RecyclerView recyclerView;
    private ApiClientGetVideo apiClientGetVideo;
    private FeedPojo feedPojo;
    private CustomAdapterVideo customAdapterVideo;
    private Snackbar snackbar;


    public VideosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        if (NetworkCheck.isNetworkAvailable(getContext()))
            getVideos();


        else
        {
            snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Network Unavailable",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Retry", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                            //    finish();
                            //    startActivity(getIntent());

                            getFragmentManager().beginTransaction()
                                    .detach(VideosFragment.this)
                                    .attach(VideosFragment.this)
                                    .commit();


                        }
                    }).show();
        }

        return view;
    }

    private void getVideos() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading Videos...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetVideo = ApiClientBase.getApiClient().create(ApiClientGetVideo.class);
        Call<FeedPojo> call= apiClientGetVideo.getVideos();
        call.enqueue(new Callback<FeedPojo>() {
            @Override
            public void onResponse(Call<FeedPojo> call, Response<FeedPojo> response) {


                feedPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(feedPojo!=null&&feedPojo.getTable().size()>0) {

                    customAdapterVideo=new CustomAdapterVideo(getContext(),feedPojo.getTable(),getActivity());
                    recyclerView.setAdapter(customAdapterVideo);
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

                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Network Unavailable",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                //    finish();
                                //    startActivity(getIntent());

                                getFragmentManager().beginTransaction()
                                        .detach(VideosFragment.this)
                                        .attach(VideosFragment.this)
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
