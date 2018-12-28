package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFollowFollowing;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterMyDashboard;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetFollowFollowingList;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetPinnedPost;
import tutorialslink.com.tutorialslinkwebview.pojos.followfollwingpojo.FollowFollowingPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo.PinnedPostPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {


    public FollowingFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewFollowing;
    private ImageView imageViewNoFollowing;
    private Snackbar snackbar;
    private ApiClientGetFollowFollowingList apiClientGetFollowFollowingList;
    private FollowFollowingPojo followFollowingPojo;
    private CustomAdapterFollowFollowing customAdapterFollowFollowing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        imageViewNoFollowing=view.findViewById(R.id.imageViewNoFollowing);
        recyclerViewFollowing=view.findViewById(R.id.recyclerViewFollowing);

        recyclerViewFollowing.setHasFixedSize(true);
       // recyclerViewFollowing.setLayoutManager(new GridLayoutManager(getContext(), 2));
         recyclerViewFollowing.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        if(NetworkCheck.isNetworkAvailable(getContext()))
            getList();

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
                            .detach(FollowingFragment.this)
                            .attach(FollowingFragment.this)
                            .commit();


                }
            }).show();

        }



        return view;
    }

    private void getList() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        String url = "FalloFallowinglist?authid="+ UserSharedPreferenceData.getLoggedInUserID(getContext())+"&type=Following";
        apiClientGetFollowFollowingList = ApiClientBase.getApiClient().create(ApiClientGetFollowFollowingList.class);
        Call<FollowFollowingPojo> call= apiClientGetFollowFollowingList.getList(url);
        call.enqueue(new Callback<FollowFollowingPojo>() {
            @Override
            public void onResponse(Call<FollowFollowingPojo> call, Response<FollowFollowingPojo> response) {


                followFollowingPojo=response.body();

              //     Toast.makeText(getContext(),followFollowingPojo.toString(),Toast.LENGTH_SHORT).show();
                if(followFollowingPojo!=null&&followFollowingPojo.getTable().size()>0) {


                  //  Toast.makeText(getContext(),followFollowingPojo.toString(),Toast.LENGTH_SHORT).show();

                    imageViewNoFollowing.setVisibility(View.GONE);

                    customAdapterFollowFollowing=new CustomAdapterFollowFollowing(getContext(),followFollowingPojo,getActivity());
                    recyclerViewFollowing.setAdapter(customAdapterFollowFollowing);
                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                    recyclerViewFollowing.setLayoutAnimation(controller);
                    recyclerViewFollowing.getAdapter().notifyDataSetChanged();
                    recyclerViewFollowing.scheduleLayoutAnimation();


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());

                }

                else if(followFollowingPojo.getTable().size()==0)
                {
                    //   Toast.makeText(getContext(),"Nothing Added Yet",Toast.LENGTH_SHORT).show();
                    recyclerViewFollowing.setVisibility(View.GONE);
                    imageViewNoFollowing.setVisibility(View.VISIBLE);


                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<FollowFollowingPojo> call, Throwable t) {

                pDialog.dismiss();


                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        getFragmentManager().beginTransaction()
                                .detach(FollowingFragment.this)
                                .attach(FollowingFragment.this)
                                .commit();


                    }
                }).show();

                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
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
