package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterAllTutorials;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetTutorials;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialLibrary;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialsPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;


public class AllTutorialsActivity extends AppCompatActivity {

    private FastScrollRecyclerView recyclerView;
    private Snackbar snackbar;
    private ApiClientGetTutorials apiClientGetTutorials;
    private ArrayList<TutorialsPojo> tutorialsPojo;
    private CustomAdapterAllTutorials customAdapterAllTutorials;
    private LinearLayout layout;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tutorials);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layout = (LinearLayout) findViewById(R.id.layout);


        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Tutorial Library");


        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));


        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)


        if (NetworkCheck.isNetworkAvailable(this))
            getTutorials();

        else {
            snackbar = Snackbar.make((layout), "Network Unavailable", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                    //    finish();
                    //    startActivity(getIntent());

                    recreate();


                }
            }).show();
        }


    }

    private void getTutorials() {


        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Loading Tutorials...");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetTutorials = ApiClientBase.getApiClient().create(ApiClientGetTutorials.class);
        Call<ArrayList<TutorialsPojo>> call = apiClientGetTutorials.getTutorials();
        call.enqueue(new Callback<ArrayList<TutorialsPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<TutorialsPojo>> call, Response<ArrayList<TutorialsPojo>> response) {


                tutorialsPojo = response.body();
                //   tutorialsPojo.get(0).getTutorials().addAll(tutorialsPojo.get(0).getTutorials());

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if (tutorialsPojo.size() != 0) {

                    layout.setVisibility(View.VISIBLE);


                    Collections.sort(tutorialsPojo.get(0).getTutorials(), new Comparator<TutorialLibrary>() {
                        @Override
                        public int compare(TutorialLibrary lhs, TutorialLibrary rhs) {
                            return lhs.getTCat_name().compareTo(rhs.getTCat_name());
                        }
                    });

                    customAdapterAllTutorials = new CustomAdapterAllTutorials(AllTutorialsActivity.this, tutorialsPojo.get(0).getTutorials(), AllTutorialsActivity.this);
                    recyclerView.setAdapter(customAdapterAllTutorials);


                    final LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(AllTutorialsActivity.this, R.anim.layout_animation_fall_down);

                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();

                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());

                } else if (tutorialsPojo.size() == 0) {
                    Toast.makeText(AllTutorialsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<ArrayList<TutorialsPojo>> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();

                snackbar = Snackbar.make(layout, "Something Went Wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        recreate();


                    }
                }).show();

                System.out.println("failure" + "+ : " + t.getMessage());
                System.out.println("failure" + "+ : " + t.getCause());
                System.out.println("failure" + "+ : " + t.toString());
            }


        });


    }
}
