package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterMyDashboard;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetPinnedPost;
import tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo.PinnedPostPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDahboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ApiClientGetPinnedPost apiClientGetPinnedPost;
    private CustomAdapterMyDashboard customAdapterMyDashboard;
    private PinnedPostPojo pinnedPostPojo;
    private Snackbar snackbar;
    private FrameLayout frameLayout;


    public MyDahboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_dahboard, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
       // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        frameLayout = view.findViewById(R.id.layout);



        if(NetworkCheck.isNetworkAvailable(getContext()))
            getPinnedItems();

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
                            .detach(MyDahboardFragment.this)
                            .attach(MyDahboardFragment.this)
                            .commit();


                }
            }).show();

        }

        return view;
    }

    private void getPinnedItems() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        String url = "Sp_User_PinPost_Get?author_id="+ UserSharedPreferenceData.getLoggedInUserID(getContext());
        apiClientGetPinnedPost = ApiClientBase.getApiClient().create(ApiClientGetPinnedPost.class);
        Call<PinnedPostPojo> call= apiClientGetPinnedPost.getPinnedPost(url);
        call.enqueue(new Callback<PinnedPostPojo>() {
            @Override
            public void onResponse(Call<PinnedPostPojo> call, Response<PinnedPostPojo> response) {

                pinnedPostPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(pinnedPostPojo!=null&&pinnedPostPojo.getTable().size()>0) {

                    customAdapterMyDashboard=new CustomAdapterMyDashboard(getContext(),pinnedPostPojo,getActivity(),frameLayout);
                    recyclerView.setAdapter(customAdapterMyDashboard);
                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(pinnedPostPojo.getTable().size()==0)
                {
                 //   Toast.makeText(getContext(),"Nothing Added Yet",Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);

                    ImageView imageViewEmpyCart=new ImageView(getContext());
                    imageViewEmpyCart.setImageResource(R.drawable.sticker);
                    imageViewEmpyCart.setVisibility(View.VISIBLE);
                    frameLayout.addView(imageViewEmpyCart);


                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<PinnedPostPojo> call, Throwable t) {

                pDialog.dismiss();


                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        getFragmentManager().beginTransaction()
                                .detach(MyDahboardFragment.this)
                                .attach(MyDahboardFragment.this)
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
