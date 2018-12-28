package tutorialslink.com.tutorialslinkwebview.activities;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.fragments.ArticleFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.EventFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.FeedFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.FollowersFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.FollowingFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.HomeFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.MyDahboardFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.NewsFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.NotificationFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.TutorialFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.VideosFragment;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticle;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetNotificationCount;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;



public class Dashboard extends AppCompatActivity {

    private Toolbar tb;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MenuItem prevItem;
    private ActionBarDrawerToggle toggle;
    private FragmentTransaction fragmentTransaction;
    private TextView textViewUserName;
    private CircleImageView imageView;
    private ImageView imageViewBackBanner;
    private boolean doubleBackToExitPressedOnce = false;
    private BottomNavigationView navigationBottom;
    private boolean checkBottom = true;
    private TextView textViewNotificationCount;
    private BottomNavigationItemView itemView;
    private View badge;
    private ApiClientGetNotificationCount apiClientGetNotificationCount;
    private BottomNavigationMenuView bottomNavigationMenuView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           // Toast.makeText(Dashboard.this,"yes",Toast.LENGTH_LONG).show();
            checkBottom = true;
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:

                    if(checkBottom) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new MyDahboardFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("My Dashboard");
                    }
               //     navigationBottom.setSelectedItemId(R.id.navigation_dashboard);

                    break;

                case R.id.navigation_events:

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new EventFragment());
                    fragmentTransaction.commit();

                    getSupportActionBar().setTitle("Upcoming Events");

                    drawer.closeDrawers();
                    break;

                case R.id.navigation_home:


                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new HomeFragment());
                    fragmentTransaction.commit();

                    getSupportActionBar().setTitle("Home");

                    drawer.closeDrawers();
                    break;


                case R.id.navigation_notifications:


                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new NotificationFragment());
                    fragmentTransaction.commit();

                    getSupportActionBar().setTitle("Notifications");

                    drawer.closeDrawers();

                    itemView.removeView(badge);

                    break;


                case R.id.navigation_feed:

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new FeedFragment());
                    fragmentTransaction.commit();

                    getSupportActionBar().setTitle("Feeds");

                    drawer.closeDrawers();
                    break;

            }
            return true;
        }
    };


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dashboard);

        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setTitle("Home");


        //drawer code


        navigationView = (NavigationView) findViewById(R.id.navigator);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View header=navigationView.getHeaderView(0);
        textViewUserName=(TextView)header.findViewById(R.id.textViewUserName);
        imageView = header.findViewById(R.id.imageView);
        imageViewBackBanner=header.findViewById(R.id.imageViewBackBanner);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(Dashboard.this))
                startActivity(new Intent(Dashboard.this,ProfileActivity.class));

                else {
                        Toast.makeText(Dashboard.this,"Network Unavailable",Toast.LENGTH_SHORT).show();
                }
            }
        });


        setProfile();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
//        params.width = metrics.widthPixels*5/8;
//        navigationView.setLayoutParams(params);

        navigationBottom = (BottomNavigationView) findViewById(R.id.navigationBottom);
        navigationBottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         navigationBottom.getMenu().getItem(2).setChecked(true);

//        final MenuItem menuItem = navigationBottom.getMenu().getItem(4);
//
//        View actionView = MenuItemCompat.getActionView(menuItem);
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//
//        textCartItemCount.setText("9");
//            textCartItemCount.setVisibility(View.VISIBLE);

        bottomNavigationMenuView =
                (BottomNavigationMenuView) navigationBottom.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(4);
        itemView = (BottomNavigationItemView) v;



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

//                if (prevItem != null)
//                    prevItem.setChecked(false);
//
//                item.setCheckable(true);
//                item.setChecked(true);
//                checkBottom = false;
//
//                prevItem = item;

                switch (item.getItemId()) {

                    case R.id.home:

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_layout_id, new HomeFragment());
                    fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Home");
                    //

                    drawer.closeDrawers();
                    break;

//                    case R.id.dashboard:
//
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.home_layout_id, new MyDahboardFragment());
//                        fragmentTransaction.commit();
//
//                        getSupportActionBar().setTitle("My Dashboard");
//
//                        drawer.closeDrawers();
//                        break;

                    case R.id.tutorials:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new TutorialFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Tutorials");

                        drawer.closeDrawers();
                        break;

                    case R.id.articles:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new ArticleFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Articles");

                        drawer.closeDrawers();
                        break;

                    case R.id.news:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new NewsFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("News");

                        drawer.closeDrawers();
                        break;

                    case R.id.videos:

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new VideosFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Videos");

                        drawer.closeDrawers();
                        break;

                   case R.id.logout_id :

                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        builder.setTitle("Log Out");
                        builder.setMessage("Are You Sure?");
                        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                UserSharedPreferenceData.clearPref(getApplicationContext());
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                        break;


                    case R.id.followers_id :

                           fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new FollowersFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Followers");

                        drawer.closeDrawers();

                       break;



                    case R.id.following_id :

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.home_layout_id, new FollowingFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Following");

                        drawer.closeDrawers();

                        break;


                    case R.id.editprofile :

                        drawer.closeDrawers();

                   //     navigationView.getMenu().findItem(R.id.editprofile).setChecked(false);

                        startActivity(new Intent(Dashboard.this,EditProfileActivity.class));

                        break;

                    case R.id.changepassword :

                        drawer.closeDrawers();

                        //     navigationView.getMenu().findItem(R.id.editprofile).setChecked(false);

                        startActivity(new Intent(Dashboard.this,ChangePasswordActivity.class));

                        break;

                }
                navigationBottom.getMenu().getItem(2).setChecked(true);
                return false;
            }
        });

        navigationView.setItemIconTintList(null);



//        MenuItem item = navigationView.getMenu().findItem(R.id.home);
//        item.setCheckable(true);
//        item.setChecked(true);
//        prevItem=item;


        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.home_layout_id, new HomeFragment());
        fragmentTransaction.commit();


        toggle = new ActionBarDrawerToggle(this, drawer,tb,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.closeDrawers();


        if(!UserSharedPreferenceData.getUserLoggedInWith(this).equals("Email"))
        {
            MenuItem item = navigationView.getMenu().findItem(R.id.changepassword);
            item.setVisible(false);
        }
    }

    private void setProfile() {

        textViewUserName.setText(UserSharedPreferenceData.getPrefLoggedinUserName(this));
        //Picasso.with(getApplicationContext()).load(UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext())).into(imageView);
        if(!UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext()).equals("")) {

          //  System.out.println(UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext()));
          //  Toast.makeText(getApplicationContext(),UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext()),Toast.LENGTH_LONG).show();

           if(UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext()).startsWith("/"))
            {

                Picasso.with(this).load("https://tutorialslink.com"+UserSharedPreferenceData.getPrefLoggedinUserProf(this)).into(imageView);
            }

            else {
                Picasso.with(getApplicationContext()).load(UserSharedPreferenceData.getPrefLoggedinUserProf(getApplicationContext())).into(imageView);
            }

            if(UserSharedPreferenceData.getPrefLoggedinUserProfBack(getApplicationContext()).startsWith("/"))
            {

                Picasso.with(this).load("https://tutorialslink.com"+UserSharedPreferenceData.getPrefLoggedinUserProfBack(this)).into(imageViewBackBanner);
            }

            else if(!UserSharedPreferenceData.getPrefLoggedinUserProfBack(getApplicationContext()).equals("")) {
                Picasso.with(getApplicationContext()).load(UserSharedPreferenceData.getPrefLoggedinUserProfBack(getApplicationContext())).into(imageViewBackBanner);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.search) {

           Intent intent = new Intent(this,SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        }

        else if(id == R.id.support)
        {
            startActivity(new Intent(this,SupportActivity.class));

        }

        else if(id == R.id.scanQR)
        {
            startActivity(new Intent(this,ScanQR.class));
        }


        else if(id == R.id.rateUs)
        {

            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    void getNotificationCount()
    {

        String url = "NotificationCount?email="+UserSharedPreferenceData.getLoggedInUserID(this);

        apiClientGetNotificationCount = ApiClientBase.getApiClient().create(ApiClientGetNotificationCount.class);
        Call<String> call= apiClientGetNotificationCount.getCount(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String count =response.body();
           //     Toast.makeText(getApplicationContext(),count,Toast.LENGTH_SHORT).show();

                    if(Integer.parseInt(count)>0)
                    {
                        badge = LayoutInflater.from(Dashboard.this)
                                .inflate(R.layout.custom_notiification_layout, bottomNavigationMenuView, false);

                        textViewNotificationCount=badge.findViewById(R.id.textViewNotificationCount);
                        textViewNotificationCount.setText(count);

                        itemView.addView(badge);
                    }
                }


            @Override
            public void onFailure(Call<String> call, Throwable t) {

                // if(skip==0)
                  Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }


    @Override
    protected void onResume() {

        getNotificationCount();

        super.onResume();
    }
}