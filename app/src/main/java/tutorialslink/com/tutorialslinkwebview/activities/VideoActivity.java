package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.fragments.ProfileFragment;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientPinPost;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {


    private TextView textViewDescription,textViewAuthor,textViewTitle,textViewDate,textViewViews;
    private String intentData;
    private DetailPojo detailpojo;
    private Toolbar tb;
    private CollapsingToolbarLayout toolbar_layout;
    private AppBarLayout mAppBarLayout;
    private ImageView imageViewTop;
    private WebView webView,mWebViewComments,mWebviewPop;
    private YouTubePlayerView youtubePlayerView;
    private CircleImageView imageView;
  //  private YouTubePlayer.OnInitializedListener onInitializedListener;
    private FloatingActionButton floatingActionButtonPinToDashboard;
    private ApiClientPinPost apiClientPinPost;
    private static final int CONTENT_VIEW_ID = 10101010;
    private boolean isLoading;
    private FrameLayout mContainer;
    private static final int NUMBER_OF_COMMENTS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        tb=(Toolbar)findViewById(R.id.toolbar);
        toolbar_layout=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        // toolbar_layout.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
        imageViewTop=(ImageView)findViewById(R.id.imageViewTop);
        //   textViewDescription=(TextView)findViewById(R.id.textViewDescription);
//        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

        textViewAuthor=(TextView)findViewById(R.id.textViewAuthor);
        textViewTitle=(TextView)findViewById(R.id.textViewTitle);

        textViewDate=(TextView)findViewById(R.id.textViewDate);
        textViewViews=(TextView)findViewById(R.id.textViewViews);

        webView=(WebView)findViewById(R.id.webView);

        imageView = findViewById(R.id.imageView);

        mContainer = (FrameLayout) findViewById(R.id.webview_frame);



        webView = (WebView) findViewById(R.id.webView);

        mWebViewComments = (WebView) findViewById(R.id.commentsView);

      //  youtubePlayerView = findViewById(R.id.youtubePlayerView);

        floatingActionButtonPinToDashboard = findViewById(R.id.floatingActionButtonPinToDashboard);




        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //   mWebView.loadUrl("file:///android_asset/myerrorpage.html");

                webView.setVisibility(View.GONE);

                Snackbar.make((CoordinatorLayout)findViewById(R.id.layout),"Network Unavailable",Snackbar.LENGTH_INDEFINITE).
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
        });

        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        tb.setTitle("");

        toolbar_layout.setTitle(" ");

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intentData=getIntent().getStringExtra("data");
     //   getSupportActionBar().setTitle("");

        initData();


   //     YTParams params = new YTParams();

    //    youtubePlayerView.setAutoPlayerHeight(this);

       // final String[] str_chop = detailpojo.getTable().get(0).getUpload_File().split("/");

//        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//           //     youTubePlayer.cueVideo(str_chop[str_chop.length-1]);
//
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };



  //      youtubePlayerView.initialize("AIzaSyAvYxh2Vw2PZpYYz_1W90ZjzuVv3T6XGks",this);

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize("AIzaSyAvYxh2Vw2PZpYYz_1W90ZjzuVv3T6XGks", this);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open sheet

                Gson gson = new Gson();
                ProfileFragment newFragment = ProfileFragment.newInstance(gson.toJson(detailpojo.getTable().get(0)));
//                newFragment.show(fragmentTransaction, "dialog");
//
                //ProfileFragment profileFragment = new ProfileFragment();
                 newFragment.show(getSupportFragmentManager(), newFragment.getTag());

            }
        });


        textViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open sheet

                Gson gson = new Gson();
                ProfileFragment newFragment = ProfileFragment.newInstance(gson.toJson(detailpojo.getTable().get(0)));
//                newFragment.show(fragmentTransaction, "dialog");
//
  //              ProfileFragment profileFragment = new ProfileFragment();
                newFragment.show(getSupportFragmentManager(), newFragment.getTag());

            }
        });


        floatingActionButtonPinToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkCheck.isNetworkAvailable(getApplicationContext()))
                {
                    pinPost();
                }

                else {
                    Snackbar.make(findViewById(R.id.layout),"Network Unavailable",Snackbar.LENGTH_LONG).show();
                }

            }
        });




        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar_layout.setTitle(detailpojo.getTable().get(0).getTitle());
                    isShow = true;
                } else if(isShow) {
                    toolbar_layout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

    private void initData() {

        detailpojo=new Gson().fromJson(intentData,DetailPojo.class);
        String[] str_chop = detailpojo.getTable().get(0).getImage().split(",");
        Picasso.with(this).load("https://tutorialslink.com/"+str_chop[0]).into(imageViewTop);
        textViewTitle.setText(detailpojo.getTable().get(0).getTitle());

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");

        Date date = null;
        try {
            date = originalFormat.parse(detailpojo.getTable().get(0).getCreated_At());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = targetFormat.format(date);

        textViewAuthor.setText("By "+detailpojo.getTable().get(0).getFirst_name()+ "" + detailpojo.getTable().get(0).getLast_name());

        textViewDate.setText("  "+formattedDate);
        textViewViews.setText("  "+detailpojo.getTable().get(0).getViews());



        //  textViewDescription.setText( Html.fromHtml(detailpojo.getTable().get(0).getDescription(),0,new HtmlHttpImageGetter(textViewDescription),null));
        if(!detailpojo.getTable().get(0).getDescription().equals("0"))
        webView.loadDataWithBaseURL("", detailpojo.getTable().get(0).getDescription(), "text/html", "UTF-8", "");

        if(!detailpojo.getTable().get(0).getPicture().equals("")&&detailpojo.getTable().get(0).getPicture()!=null)
        {
            if(detailpojo.getTable().get(0).getPicture().startsWith("/"))
            {
                Picasso.with(this).load("https://tutorialslink.com"+detailpojo.getTable().get(0).getPicture()).into(imageView);
            }
            else {
                Picasso.with(this).load(detailpojo.getTable().get(0).getPicture()).into(imageView);
            }
        }

        loadComments();


    }


    private void pinPost() {

        final ProgressDialog pDialog = new ProgressDialog(this);


        pDialog.setMessage("Pinning to Dashboard");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientPinPost = ApiClientBase.getApiClient().create(ApiClientPinPost.class);
        Call<String> call= apiClientPinPost.pinPost("Sp_User_PinPost_Add?author_id="+ UserSharedPreferenceData.getLoggedInUserID(this)+"&post_id="+detailpojo.getTable().get(0).getSr_no());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res =response.body();
                //   Toast.makeText(getApplicationContext(),response.body(),Toast.LENGTH_SHORT).show();

                if(res!=null)
                {

                    if(res.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Pinned to Dashboard", Toast.LENGTH_LONG).show();
                    }

                    else if(res.equals("0")) {
                        Toast.makeText(getApplicationContext(), "Already added tp Dashboard", Toast.LENGTH_LONG).show();
                    }

                    else {
                        System.out.println(res);
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }

                    //     System.out.println(detailPojo.toString());


                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });


    }

    private void loadComments() {
        mWebViewComments.setWebViewClient(new VideoActivity.UriWebViewClient());
        mWebViewComments.setWebChromeClient(new VideoActivity.UriChromeClient());
        mWebViewComments.getSettings().setJavaScriptEnabled(true);
        mWebViewComments.getSettings().setAppCacheEnabled(true);
        mWebViewComments.getSettings().setDomStorageEnabled(true);
        mWebViewComments.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebViewComments.getSettings().setSupportMultipleWindows(true);
        mWebViewComments.getSettings().setSupportZoom(false);
        mWebViewComments.getSettings().setBuiltInZoomControls(false);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebViewComments.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebViewComments, true);
        }

        // facebook comment widget including the article url
        String html = "<!doctype html> <html lang=\"en\"> <head> <script type=\"text/css\"> .fb_iframe_widget{overflow: hidden;} .fb_ltr{margin-bottom: -20px;} div.fb-comments.fb_iframe_widget span {margin-bottom: -35px;} </script></head> <body> " +
                "<div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; " +
                "if (d.getElementById(id)) return;" + " js = d.createElement(s); js.id = id; " +
                "js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.6\"; " +
                "fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script> " +
                "<div class=\"fb-comments\" data-href=\"" + detailpojo.getTable().get(0).getWebURL() + "\" " +
                "data-numposts=\"" + NUMBER_OF_COMMENTS + "\" data-order-by=\"reverse_time\">" +
                "</div> </body> </html>";

        mWebViewComments.loadDataWithBaseURL("http://www.tutorialslink.com", html, "text/html", "UTF-8", null);
        mWebViewComments.setMinimumHeight(200);
    }

//    private void setLoading(boolean isLoading) {
//        this.isLoading = isLoading;
//
//        if (isLoading)
//            progressBar.setVisibility(View.VISIBLE);
//        else
//            progressBar.setVisibility(View.GONE);
//
//        invalidateOptionsMenu();
//    }

    private class UriWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String host = Uri.parse(url).getHost();

            return !host.equals("m.facebook.com");

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String host = Uri.parse(url).getHost();
            //   setLoading(false);
            if (url.contains("/plugins/close_popup.php?reload")) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mContainer.removeView(mWebviewPop);
                        loadComments();
                    }
                }, 600);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            //   setLoading(false);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(getApplicationContext());
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new VideoActivity.UriWebViewClient());
            mWebviewPop.setWebChromeClient(this);
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setDomStorageEnabled(true);
            mWebviewPop.getSettings().setSupportZoom(false);
            mWebviewPop.getSettings().setBuiltInZoomControls(false);
            mWebviewPop.getSettings().setSupportMultipleWindows(true);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            //     Log.i(TAG, "onConsoleMessage: " + cm.message());
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        final String[] str_chop = detailpojo.getTable().get(0).getUpload_File().split("/");

        youTubePlayer.cueVideo(str_chop[str_chop.length-1]);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

}