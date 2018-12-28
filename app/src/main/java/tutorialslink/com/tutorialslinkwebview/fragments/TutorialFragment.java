package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.AllTutorialsActivity;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFeaturedTutorial;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterTutorial;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetTutorials;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialLibrary;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialsPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFragment extends Fragment {

    private RecyclerView recyclerView,recyclerViewTutorials;
    private ApiClientGetTutorials apiClientGetTutorials;
    private ArrayList<TutorialsPojo> tutorialsPojo;
    private CustomAdapterTutorial customAdapterTutorial;
    private CustomAdapterFeaturedTutorial customAdapterFeaturedTutorial;
    private StickyRecyclerHeadersDecoration headersDecor;
    private LinearLayout layout;
    private Snackbar snackbar;
    private TextView buttonAllTutorials;


    public TutorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewTutorials = (RecyclerView) view.findViewById(R.id.recyclerViewTutorials);

        buttonAllTutorials=(TextView)view.findViewById(R.id.buttonAllTutorials);


        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        recyclerViewTutorials.setHasFixedSize(true);
        // recyclerViewTutorials.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //  recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerViewTutorials.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        buttonAllTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AllTutorialsActivity.class);
                getActivity().startActivity(intent);
            }
        });


        if (NetworkCheck.isNetworkAvailable(getContext()))
            getTutorials();

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
                                    .detach(TutorialFragment.this)
                                    .attach(TutorialFragment.this)
                                    .commit();


                        }
                    }).show();
        }

        return view;
    }

    private void getTutorials() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading Tutorials...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetTutorials = ApiClientBase.getApiClient().create(ApiClientGetTutorials.class);
        Call<ArrayList<TutorialsPojo>> call= apiClientGetTutorials.getTutorials();
        call.enqueue(new Callback<ArrayList<TutorialsPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<TutorialsPojo>> call, Response<ArrayList<TutorialsPojo>> response) {


                tutorialsPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(tutorialsPojo.size()!=0) {

                    layout.setVisibility(View.VISIBLE);


                    Collections.sort(tutorialsPojo.get(0).getTutorials(), new Comparator<TutorialLibrary>() {
                        @Override
                        public int compare(TutorialLibrary lhs, TutorialLibrary rhs) {
                            return lhs.getCat_id().compareTo(rhs.getCat_id());
                        }
                    });

                    customAdapterTutorial=new CustomAdapterTutorial(getContext(),tutorialsPojo.get(0).getTutorials(),getActivity());
                  //  recyclerViewTutorials.addItemDecoration(headersDecor);
                    recyclerViewTutorials.setAdapter(customAdapterTutorial);


                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                    recyclerViewTutorials.setLayoutAnimation(controller);
                    recyclerViewTutorials.getAdapter().notifyDataSetChanged();
                    recyclerViewTutorials.scheduleLayoutAnimation();



                    customAdapterFeaturedTutorial=new CustomAdapterFeaturedTutorial(getContext(),tutorialsPojo.get(0).getFeatured(),getActivity());
                    recyclerView.setAdapter(customAdapterFeaturedTutorial);
                    final LayoutAnimationController controller2 =
                            AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_right);

                    recyclerView.setLayoutAnimation(controller2);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();





                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(tutorialsPojo.size()==0 )
                {
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<ArrayList<TutorialsPojo>> call, Throwable t) {

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
                                        .detach(TutorialFragment.this)
                                        .attach(TutorialFragment.this)
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
