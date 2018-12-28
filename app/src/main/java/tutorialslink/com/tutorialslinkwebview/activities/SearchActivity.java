package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterSearch;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomAdapterSearchAuthor;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientSearch;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientSearchAuthor;
import tutorialslink.com.tutorialslinkwebview.pojos.authorSearchPojo.AuthorSearchPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.searchpojo.SearchPojo;

public class SearchActivity extends AppCompatActivity {
    private Toolbar tb;
    private SearchView searchView;
    private ApiClientSearch apiClientSearch;
    private int cat = 3;
    private String query="";
    private SearchPojo searchPojo;
    private LinearLayout layout,linearLayout;
    private TextView textViewArticles,textViewVideos,textViewNews,textViewTutorials,textViewPeople,textViewEvents;
    private RecyclerView recyclerView;
    private ImageView imageView,imageViewBack;
    private CustomAdapterSearch customAdapterSearch;
    private ApiClientSearchAuthor apiClientSearchAuthor;
    private AuthorSearchPojo authorSearchPojo;
    private CustomAdapterSearchAuthor customAdapterSearchAuthor;
    private HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

          tb = findViewById(R.id.toolbar);
         searchView =findViewById(R.id.searchView);
        imageViewBack=findViewById(R.id.imageViewBack);
         layout=findViewById(R.id.layout);
        linearLayout=findViewById(R.id.linearLayout);

        linearLayout.setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //  recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        textViewArticles=findViewById(R.id.textViewArticles);
        textViewVideos=findViewById(R.id.textViewVideos);
        textViewNews=findViewById(R.id.textViewNews);
        textViewTutorials=findViewById(R.id.textViewTutorials);
        textViewPeople=findViewById(R.id.textViewPeople);
        textViewEvents=findViewById(R.id.textViewEvents);

        horizontalScrollView=findViewById(R.id.horizontalScrollView);

        imageView = new ImageView(SearchActivity.this);
        imageView.setImageResource(R.drawable.noresult);
        linearLayout.addView(imageView);

        textViewArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 3;
                resetTags();
                setTag(textViewArticles);
                search();
                hideKeyboard(SearchActivity.this);
            }
        });

        textViewVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 4;
                resetTags();
                setTag(textViewVideos);
                search();
                hideKeyboard(SearchActivity.this);


            }
        });

        textViewTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 2;
                resetTags();
                setTag(textViewTutorials);
                search();
                hideKeyboard(SearchActivity.this);


            }
        });

        textViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 7;
                resetTags();
                setTag(textViewNews);
                search();
                hideKeyboard(SearchActivity.this);


            }
        });

        textViewPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 10;
                resetTags();
                setTag(textViewPeople);
                searchAuthor();
                hideKeyboard(SearchActivity.this);


            }
        });

        textViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cat = 6;
                resetTags();
                setTag(textViewEvents);
                search();
                hideKeyboard(SearchActivity.this);


            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
            //    Toast.makeText(SearchActivity.this,s,Toast.LENGTH_SHORT).show();

                query = s;

                if(cat==10)
                {
                    searchAuthor();
                }
                else {
                    search();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
          //      Toast.makeText(SearchActivity.this,s,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void searchAuthor() {


        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Searching....");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientSearchAuthor = ApiClientBase.getApiClient().create(ApiClientSearchAuthor.class);

        //  String url = "UserEmail?email="+ UserSharedPreferenceData.getLoggedInUserID(this);

        String url = "SearchAuthor?query="+query;


        System.out.println("url  : " + url);

        Call<AuthorSearchPojo> call= apiClientSearchAuthor.searchAuthor(url);
        call.enqueue(new Callback<AuthorSearchPojo>() {
            @Override
            public void onResponse(Call<AuthorSearchPojo> call, Response<AuthorSearchPojo> response) {

                authorSearchPojo =response.body();

                System.out.println(" Pojo    "+authorSearchPojo.getTable().toString());

                if(authorSearchPojo!=null) {
                    if(authorSearchPojo.getTable().size()>0) {
                        layout.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        // updateUI();

//                        if(customAdapterSearch==null) {
                        
                        customAdapterSearchAuthor = new CustomAdapterSearchAuthor(SearchActivity.this, authorSearchPojo, SearchActivity.this);
                        recyclerView.setAdapter(customAdapterSearchAuthor);

//                       }

                        //                      else {
//                        customAdapterSearch.notifyDataSetChanged();
//                        }

//                        Toast.makeText(SearchActivity.this,searchPojo.toString(),Toast.LENGTH_SHORT).show();

                    }

                    else if(authorSearchPojo.getTable().size()==0) {
                        linearLayout.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<AuthorSearchPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void search()
    {

      //  String  eg = "SearchCatSite?query=angular&cat=3";

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Searching....");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientSearch = ApiClientBase.getApiClient().create(ApiClientSearch.class);

      //  String url = "UserEmail?email="+ UserSharedPreferenceData.getLoggedInUserID(this);

        String url = "SearchCatSite?query="+query+"&cat="+cat;


        System.out.println("url  : " + url);

        Call<SearchPojo> call= apiClientSearch.search(url);
        call.enqueue(new Callback<SearchPojo>() {
            @Override
            public void onResponse(Call<SearchPojo> call, Response<SearchPojo> response) {

                searchPojo =response.body();

                     System.out.println(" Pojo    "+searchPojo.getTable().toString());

                if(searchPojo!=null) {
                    if(searchPojo.getTable().size()>0) {
                        layout.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                       // updateUI();

//                        if(customAdapterSearch==null) {
                            customAdapterSearch = new CustomAdapterSearch(SearchActivity.this, searchPojo, SearchActivity.this);
                          //  recyclerView.setAdapter(null);
                            recyclerView.setAdapter(customAdapterSearch);
//                       }

 //                      else {
                            customAdapterSearch.notifyDataSetChanged();
//                        }

//                        Toast.makeText(SearchActivity.this,searchPojo.toString(),Toast.LENGTH_SHORT).show();

                    }

                    else if(searchPojo.getTable().size()==0) {
                        linearLayout.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SearchPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void resetTags()
    {
        textViewVideos.setBackground(getResources().getDrawable(R.drawable.round_corners));
        textViewArticles.setBackground(getResources().getDrawable(R.drawable.round_corners));
        textViewEvents.setBackground(getResources().getDrawable(R.drawable.round_corners));
        textViewPeople.setBackground(getResources().getDrawable(R.drawable.round_corners));
        textViewNews.setBackground(getResources().getDrawable(R.drawable.round_corners));
        textViewTutorials.setBackground(getResources().getDrawable(R.drawable.round_corners));

        textViewVideos.setPadding(5,5,5,5);
        textViewArticles.setPadding(5,5,5,5);
        textViewEvents.setPadding(5,5,5,5);
        textViewPeople.setPadding(5,5,5,5);
        textViewNews.setPadding(5,5,5,5);
        textViewTutorials.setPadding(5,5,5,5);



        textViewVideos.setTextColor(getResources().getColor(R.color.black));
        textViewArticles.setTextColor(getResources().getColor(R.color.black));
        textViewEvents.setTextColor(getResources().getColor(R.color.black));
        textViewPeople.setTextColor(getResources().getColor(R.color.black));
        textViewNews.setTextColor(getResources().getColor(R.color.black));
        textViewTutorials.setTextColor(getResources().getColor(R.color.black));

    }

    private void setTag(TextView tv)
    {

        tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setPadding(5,5,5,5);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}