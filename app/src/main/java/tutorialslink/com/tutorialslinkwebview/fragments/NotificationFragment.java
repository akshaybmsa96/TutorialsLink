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
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterFeed;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterNotifications;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterRest;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticle;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetNotifications;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.notificationPojo.NotificationPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private ImageView imageViewNoNotification;
    private RecyclerView recyclerView;
    private Snackbar snackbar;
    private ApiClientGetNotifications apiClientGetNotifications;
    private NotificationPojo notificationPojo;
    private CustomAdapterNotifications customAdapterNotifications;
    private int pageIndex=1,pageSize=10;;
    private LinearLayoutManager mLayoutManager;
    private String flag="0";
    private boolean start = true;



    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mLayoutManager = new LinearLayoutManager(getContext());

        imageViewNoNotification = view.findViewById(R.id.imageViewNoNotification);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(mLayoutManager);
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        if (NetworkCheck.isNetworkAvailable(getContext()))
        {
            getNotifications();
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
                            .detach(NotificationFragment.this)
                            .attach(NotificationFragment.this)
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
                        if(notificationPojo.getTable().size()>=pageIndex*pageSize) {
                         //   Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                            flag = "1";
                            pageIndex++;
                            getNotifications();
                            return;
                        }

                    }
                }
                //   Toast.makeText(ItemsDisplay.this, flag, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void getNotifications() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());

        String url="AuthorNotification?authid="+ UserSharedPreferenceData.getLoggedInUserID(getContext())+"&pageindex="+pageIndex+"&pagesize="+pageSize;

        if(start) {
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // show it
        apiClientGetNotifications = ApiClientBase.getApiClient().create(ApiClientGetNotifications.class);
        Call<NotificationPojo> call= apiClientGetNotifications.getNotifications(url);
        call.enqueue(new Callback<NotificationPojo>() {
            @Override
            public void onResponse(Call<NotificationPojo> call, Response<NotificationPojo> response) {


                if(start)
                {
                    notificationPojo =response.body();
                }
                else {
                    notificationPojo.getTable().addAll(response.body().getTable());
                }

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(notificationPojo!=null&&notificationPojo.getTable().size()>0) {

                    if(start) {

                        customAdapterNotifications=new CustomAdapterNotifications(getContext(),notificationPojo,getActivity());
                        recyclerView.setAdapter(customAdapterNotifications);
                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

                        recyclerView.setLayoutAnimation(controller);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                        start=false;
                    }

                    else {
                        if (customAdapterNotifications!=null)
                            customAdapterNotifications.notifyDataSetChanged();
                    }


                    //     Toast.makeText(getContext(),feedPojo.toString(),Toast.LENGTH_SHORT).show();

                    //     System.out.println(feedPojo.getTable().get(0).getImage());


                }

                else if(notificationPojo==null&&notificationPojo.getTable().size()==0)
                {
                    Toast.makeText(getContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

                if (pageIndex==1)
                    pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<NotificationPojo> call, Throwable t) {

                if (pageIndex==1)
                    pDialog.dismiss();


                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_layout_id),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        getFragmentManager().beginTransaction()
                                .detach(NotificationFragment.this)
                                .attach(NotificationFragment.this)
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
