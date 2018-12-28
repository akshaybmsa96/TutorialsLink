package tutorialslink.com.tutorialslinkwebview.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.AllTutorialsActivity;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFeaturedTutorial;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterHomeArticle;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterHomeNews;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterHomeVideo;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdpaterHomePager;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetHighlights;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetHome;
import tutorialslink.com.tutorialslinkwebview.pojos.HomePojo;
import tutorialslink.com.tutorialslinkwebview.pojos.highlightsPojo.HighlightsPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {



    private RecyclerView recyclerViewArticles,recyclerViewTutorials,recyclerViewNews,recyclerViewVideos;
    private ApiClientGetHome apiClientGetHome;
    private ArrayList<HomePojo> homePojo;
    private CustomAdapterFeaturedTutorial customAdapterFeaturedTutorial;
    private CustomAdapterHomeArticle customAdapterHomeArticle;
    private CustomAdapterHomeNews customAdapterHomeNews;
    private CustomAdapterHomeVideo customAdapterHomeVideo;
    private Snackbar snackbar;
    private LinearLayout layout;
    private TextView buttonAllArticles,buttonAllTutorials,buttonAllNews,buttonAllVideo;
    private FragmentTransaction fragmentTransaction;
    private ViewPager viewPager;
    private ApiClientGetHighlights apiClientGetHighlights;
    private HighlightsPojo highlightsPojo;

    private int[] mResources = {
            R.drawable.background1,
            R.drawable.background2,
            R.drawable.background3,
            R.drawable.background4,
    };

    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 500;
    private final long PERIOD_MS = 3000;
    private CircleIndicator indicator ;
    private RelativeLayout layoutPager;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        buttonAllArticles=(TextView) view.findViewById(R.id.buttonAllArticles);
        buttonAllTutorials=(TextView)view.findViewById(R.id.buttonAllTutorials);
        buttonAllNews=(TextView)view.findViewById(R.id.buttonAllNews);
        buttonAllVideo=(TextView)view.findViewById(R.id.buttonAllVideo);
        indicator = (CircleIndicator)view.findViewById(R.id.indicator);

        layout=(LinearLayout)view.findViewById(R.id.layout);
        layout.setVisibility(View.GONE);
        layoutPager=view.findViewById(R.id.layoutPager);

        recyclerViewArticles=(RecyclerView)view.findViewById(R.id.recyclerViewArticles);
        recyclerViewArticles.setHasFixedSize(true);
      //  recyclerViewArticles.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
     //   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        recyclerViewTutorials=(RecyclerView)view.findViewById(R.id.recyclerViewTutorials);
        recyclerViewTutorials.setHasFixedSize(true);
      //  recyclerViewTutorials.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        recyclerViewNews=(RecyclerView)view.findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setHasFixedSize(true);
      //  recyclerViewNews.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        recyclerViewVideos=(RecyclerView)view.findViewById(R.id.recyclerViewVideos);
        recyclerViewVideos.setHasFixedSize(true);
        //  recyclerViewNews.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        viewPager=view.findViewById(R.id.viewPager);


       // viewPager.setPageTransformer(true, new DepthPageTransformer());

      //  viewPager.setPageTransformer(true, new ZoomOutPageTransformer());


        buttonAllArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new ArticleFragment());
                    fragmentTransaction.commit();
                }

                else {

                    Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonAllTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                {
                    startActivity(new Intent(getActivity(), AllTutorialsActivity.class));
                }

                else {

                    Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
                }

            }
        });


        buttonAllNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getContext()))
                {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new NewsFragment());
                    fragmentTransaction.commit();

                }

                else {

                    Toast.makeText(getContext(),"Network Unavailable",Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonAllVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_layout_id, new VideosFragment());
                fragmentTransaction.commit();

            }
        });


        if(NetworkCheck.isNetworkAvailable(getContext())) {
            getHome();
            getHighlights();
        }

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
                                    .detach(HomeFragment.this)
                                    .attach(HomeFragment.this)
                                    .commit();


                        }
                    }).show();
        }
        return view;
    }

    private void getHighlights() {

//        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//
//
//        pDialog.setMessage("Loading...");
//        pDialog.setCancelable(false);
//        pDialog.show();

        // show it
        apiClientGetHighlights = ApiClientBase.getApiClient().create(ApiClientGetHighlights.class);
        Call<HighlightsPojo> call= apiClientGetHighlights.getHighlights();
        call.enqueue(new Callback<HighlightsPojo>() {
            @Override
            public void onResponse(Call<HighlightsPojo> call, Response<HighlightsPojo> response) {


                highlightsPojo=response.body();

//                    System.out.println("Articles"+ new Gson().toJson(homePojo.get(0).getArticlelist()));

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(highlightsPojo!=null&&highlightsPojo.getTable().size()>0) {

                    layoutPager.setVisibility(View.VISIBLE);

                    int j = 0;

                    for(int i = 0 ; i<highlightsPojo.getTable().size();i++)
                    {
                        highlightsPojo.getTable().get(i).setBackImage(mResources[j]);
                        if(j==3)
                            j=0;

                        else
                            j++;

                    }

                    CustomAdpaterHomePager customPagerAdapter=new CustomAdpaterHomePager(getActivity(),highlightsPojo,mResources);
                    viewPager.setAdapter(customPagerAdapter);
                    indicator.setViewPager(viewPager);

                    startPagerAutoScroll();


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(homePojo==null||homePojo.size()==0)
                {
                //    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                    viewPager.setVisibility(View.GONE);
                }

           //     pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<HighlightsPojo> call, Throwable t) {

           //     pDialog.dismiss();
                // if(skip==0)
                //   Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();

                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void getHome() {


            final ProgressDialog pDialog = new ProgressDialog(getActivity());


            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            // show it
        apiClientGetHome = ApiClientBase.getApiClient().create(ApiClientGetHome.class);
            Call<ArrayList<HomePojo>> call= apiClientGetHome.getHome();
            call.enqueue(new Callback<ArrayList<HomePojo>>() {
                @Override
                public void onResponse(Call<ArrayList<HomePojo>> call, Response<ArrayList<HomePojo>> response) {


                    homePojo=response.body();

//                    System.out.println("Articles"+ new Gson().toJson(homePojo.get(0).getArticlelist()));

                    //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                    if(homePojo!=null&&homePojo.size()>0) {

                        customAdapterFeaturedTutorial=new CustomAdapterFeaturedTutorial(getContext(),homePojo.get(0).getTutorialslist(),getActivity());
                        recyclerViewTutorials.setAdapter(customAdapterFeaturedTutorial);

                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_right);

                        recyclerViewTutorials.setLayoutAnimation(controller);
                        recyclerViewTutorials.getAdapter().notifyDataSetChanged();
                        recyclerViewTutorials.scheduleLayoutAnimation();


                //     recyclerViewTutorials.scrollToPosition(2);



                        customAdapterHomeArticle= new CustomAdapterHomeArticle(getContext(),homePojo.get(0).getArticlelist(),getActivity());
                        recyclerViewArticles.setAdapter(customAdapterHomeArticle);

                        recyclerViewArticles.setLayoutAnimation(controller);
                        recyclerViewArticles.getAdapter().notifyDataSetChanged();
                        recyclerViewArticles.scheduleLayoutAnimation();

               //         recyclerViewArticles.scrollToPosition(2);



                        customAdapterHomeNews= new CustomAdapterHomeNews(getContext(),homePojo.get(0).getNewslist(),getActivity());
                        recyclerViewNews.setAdapter(customAdapterHomeNews);

                        recyclerViewNews.setLayoutAnimation(controller);
                        recyclerViewNews.getAdapter().notifyDataSetChanged();
                        recyclerViewNews.scheduleLayoutAnimation();


                     //   System.out.println(new Gson().toJson(homePojo.get(0).getVideolist()));




                        customAdapterHomeVideo= new CustomAdapterHomeVideo(getContext(),homePojo.get(0).getVideolist(),getActivity());
                        recyclerViewVideos.setAdapter(customAdapterHomeVideo);

                        recyclerViewVideos.setLayoutAnimation(controller);
                        recyclerViewVideos.getAdapter().notifyDataSetChanged();
                        recyclerViewVideos.scheduleLayoutAnimation();




                        layout.setVisibility(View.VISIBLE);


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                   //     System.out.println(feedPojo.getTable().get(0).getImage());


                    }

                    else if(homePojo==null||homePojo.size()==0)
                    {
                        Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }

                    pDialog.dismiss();


                }

                @Override
                public void onFailure(Call<ArrayList<HomePojo>> call, Throwable t) {

                    pDialog.dismiss();


                    // if(skip==0)
                 //   Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();

                    snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something went wrong",Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Retry", new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                    //    finish();
                                    //    startActivity(getIntent());

                                    getFragmentManager().beginTransaction()
                                            .detach(HomeFragment.this)
                                            .attach(HomeFragment.this)
                                            .commit();


                                }
                            }).show();

                    System.out.println("failure"+"+ : "+t.getMessage());
                    System.out.println("failure"+"+ : "+t.getCause());
                    System.out.println("failure"+"+ : "+t.toString());
                }
            });


        }

        void startPagerAutoScroll()
        {
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == highlightsPojo.getTable().size()) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer .schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(snackbar!=null)
        snackbar.dismiss();
    }
}