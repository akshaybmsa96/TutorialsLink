package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.fragments.EditProfileAchivementsFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.EditProfilePersonalFragment;
import tutorialslink.com.tutorialslinkwebview.fragments.EditProfileSocialFragment;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientRegisterUser;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientUpdateUserInfo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.FtpUploader;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

import static java.security.AccessController.getContext;

public class EditProfileActivity extends AppCompatActivity implements EditProfilePersonalFragment.OnCompleteListener1,EditProfileAchivementsFragment.OnCompleteListener2,EditProfileSocialFragment.OnCompleteListener3 {


    private ViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private LoginDetailPojo loginDetailPojo;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;
    private CoordinatorLayout layout;
    private TabLayout tabLayout;
    private ApiClientUpdateUserInfo apiClientUpdateUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        layout = findViewById(R.id.main_content);
        layout.setVisibility(View.GONE);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);


        tabLayout = findViewById(R.id.tabs);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("Edit Profile");


        if(NetworkCheck.isNetworkAvailable(this))
        {
            getUserDetails();
        }

        else {

            Snackbar.make(layout, "Network Unavailable", Snackbar.LENGTH_INDEFINITE).
                    setAction("Retry", new View.OnClickListener() {

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

    private void getUserDetails() {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Fetching Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email="+ UserSharedPreferenceData.getLoggedInUserID(this);

     //   System.out.println("url               : " + url);

        Call<LoginDetailPojo> call= apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo =response.body();

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if(loginDetailPojo!=null) {
                    if(loginDetailPojo.getTable().size()>0) {
                        layout.setVisibility(View.VISIBLE);
                        updateUI();
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<LoginDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void updateUI() {

        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.addFragment(new EditProfilePersonalFragment(loginDetailPojo));
        mSectionsPagerAdapter.addFragment(new EditProfileAchivementsFragment(loginDetailPojo));
        mSectionsPagerAdapter.addFragment(new EditProfileSocialFragment(loginDetailPojo));

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
    }

    @Override
    public void onComplete1(String name, String country, String state, String city, String postalCode, String aboutMe,Boolean next) {


        loginDetailPojo.getTable().get(0).setFirst_name(name);
        loginDetailPojo.getTable().get(0).setLast_name("");
        loginDetailPojo.getTable().get(0).setCountry(country);
        loginDetailPojo.getTable().get(0).setCity(city);
        loginDetailPojo.getTable().get(0).setState(state);
        loginDetailPojo.getTable().get(0).setPostal_Code(postalCode);
        loginDetailPojo.getTable().get(0).setAbout_us(aboutMe);

        if(next) {
            mViewPager.setCurrentItem(1, true);
        }

    //    Toast.makeText(this,"Photo changed : "+isUserPhotoChanged+" "+"CoverChanged : "+isUserCoverChanged,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onComplete2(Boolean next,String technologies,String awards) {

        loginDetailPojo.getTable().get(0).setTechnologies(technologies);
        loginDetailPojo.getTable().get(0).setAwards(awards);

     //   Toast.makeText(this,technologies+" "+ awards,Toast.LENGTH_LONG).show();


        if(next) {
            mViewPager.setCurrentItem(2, true);
        }

    }

    @Override
    public void onComplete3(Boolean update,String blog,String linkedIn,String github,String twitter,String facebook) {


        loginDetailPojo.getTable().get(0).setBlog_link(blog);
        loginDetailPojo.getTable().get(0).setLinkedin_profile(linkedIn);
        loginDetailPojo.getTable().get(0).setGithub_profile(github);
        loginDetailPojo.getTable().get(0).setTwitter_profile(twitter);
        loginDetailPojo.getTable().get(0).setFaebook_profile(facebook);

        if(update)
        {
            //execute update process
            updateProfile();
        }



    }

    private void updateProfile() {


        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientUpdateUserInfo = ApiClientBase.getApiClient().create(ApiClientUpdateUserInfo.class);

        //   String url = "UserEmail?email=" + UserSharedPreferenceData.getLoggedInUserID(this);

        //    System.out.println("url               : " + url);

        Call<String> call = apiClientUpdateUserInfo.updateUser(loginDetailPojo.getTable().get(0).getFirst_name(),UserSharedPreferenceData.getLoggedInUserID(this),loginDetailPojo.getTable().get(0).getCity(),loginDetailPojo.getTable().get(0).getState(),
                loginDetailPojo.getTable().get(0).getCountry(),loginDetailPojo.getTable().get(0).getPostal_Code(),loginDetailPojo.getTable().get(0).getTechnologies(),loginDetailPojo.getTable().get(0).getAwards(),loginDetailPojo.getTable().get(0).getAbout_us(),
                loginDetailPojo.getTable().get(0).getBlog_link(),loginDetailPojo.getTable().get(0).getFaebook_profile(),loginDetailPojo.getTable().get(0).getLinkedin_profile(),loginDetailPojo.getTable().get(0).getTwitter_profile(),
                loginDetailPojo.getTable().get(0).getGithub_profile());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res  = response.body();

                 //     Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().get(0).toString()+"", Toast.LENGTH_SHORT).show();

                System.out.println("Sendind data :  " + loginDetailPojo.getTable().get(0).toString());

                if (res != null) {
                    if(res.equals("1")) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "cannot update, try again "+ res, Toast.LENGTH_SHORT).show();
                        System.out.println("Error : " + res );
                    }

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                System.out.println("failure" + "+ : " + t.getMessage());
                System.out.println("failure" + "+ : " + t.getCause());
                System.out.println("failure" + "+ : " + t.toString());
            }
        });


    }

    /*
    public static String getRealFilePath(final Context context, final Uri uri )
    {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if
                ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {

            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {

                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    */

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
