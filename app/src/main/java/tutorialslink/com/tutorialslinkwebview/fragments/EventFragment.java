package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterEvent;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetEvents;
import tutorialslink.com.tutorialslinkwebview.pojos.eventspojo.TablesPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private RecyclerView recyclerView;
    private ApiClientGetEvents apiClientGetEvents;
    private TablesPojo tablesPojo;
    private CustomAdapterEvent customAdapterEvent;
    private Snackbar snackbar;
    private LinearLayout frameLayout;


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //  recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        frameLayout = view.findViewById(R.id.relativeLayout);


        if (NetworkCheck.isNetworkAvailable(getContext()))
            getEvents();


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
                                    .detach(EventFragment.this)
                                    .attach(EventFragment.this)
                                    .commit();

                        }
                    }).show();



        }




        return view;
    }

    private void getEvents() {


        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading Events...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetEvents = ApiClientBase.getApiClient().create(ApiClientGetEvents.class);
        Call<TablesPojo> call= apiClientGetEvents.getEvents();
        call.enqueue(new Callback<TablesPojo>() {
            @Override
            public void onResponse(Call<TablesPojo> call, Response<TablesPojo> response) {


                tablesPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(tablesPojo!=null&&tablesPojo.getTable().size()>0) {

                    customAdapterEvent=new CustomAdapterEvent(getContext(),tablesPojo.getTable(),getActivity());
                    recyclerView.setAdapter(customAdapterEvent);
                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(tablesPojo==null||tablesPojo.getTable().size()==0)
                {
                 //   snackbar =  Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"No Upcoming Events, Check back soon",Snackbar.LENGTH_INDEFINITE);
                 //   snackbar.show();

                    ImageView imageViewEmpyCart=new ImageView(getContext());
                   // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                   // lp.gravity=(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                   // imageViewEmpyCart.setLayoutParams(lp);
                    imageViewEmpyCart.setImageResource(R.drawable.eventsbackimg);
                    imageViewEmpyCart.setVisibility(View.VISIBLE);
                    frameLayout.addView(imageViewEmpyCart);

                    recyclerView.setVisibility(View.GONE);

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<TablesPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
              //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();

                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something went wrong",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                //    finish();
                                //    startActivity(getIntent());

                                getFragmentManager().beginTransaction()
                                        .detach(EventFragment.this)
                                        .attach(EventFragment.this)
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
