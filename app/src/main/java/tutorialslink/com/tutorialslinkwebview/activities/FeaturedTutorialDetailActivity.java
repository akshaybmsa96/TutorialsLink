package tutorialslink.com.tutorialslinkwebview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientPinPost;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.FeaturedTutorials;

public class FeaturedTutorialDetailActivity extends AppCompatActivity  {
        private TextView textViewTitle;
        private String intentData;
        private FeaturedTutorials detailpojo;
        private Toolbar tb;
        private CollapsingToolbarLayout toolbar_layout;
        private AppBarLayout mAppBarLayout;
        private ImageView imageViewTop;
        private WebView webView;
        private ApiClientPinPost apiClientPinPost;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_featured_tutorial_detail);



            mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            tb = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(tb);

            tb.setNavigationIcon(R.mipmap.ic_back);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            getSupportActionBar().setTitle("");


            toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

            toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
            // toolbar_layout.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
            imageViewTop = (ImageView) findViewById(R.id.imageViewTop);
            //   textViewDescription=(TextView)findViewById(R.id.textViewDescription);
//        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

            textViewTitle = (TextView) findViewById(R.id.textViewTitle);

            webView = (WebView) findViewById(R.id.webView);


            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebViewClient(new WebViewClient());

            webView.setWebViewClient(new WebViewClient() {
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

                    Snackbar.make((CoordinatorLayout) findViewById(R.id.layout), "Network Unavailable", Snackbar.LENGTH_INDEFINITE).
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


            intentData = getIntent().getStringExtra("data");


            initData();



            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        toolbar_layout.setTitle(detailpojo.getTCat_name());
                        isShow = true;
                    } else if (isShow) {
                        toolbar_layout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            });

        }


        private void initData() {

            detailpojo = new Gson().fromJson(intentData, FeaturedTutorials.class);
            String[] str_chop = detailpojo.getImage().split(",");
            Picasso.with(this).load(str_chop[0]).into(imageViewTop);
            textViewTitle.setText(detailpojo.getTCat_name());


            //  textViewDescription.setText( Html.fromHtml(detailpojo.getTable().get(0).getDescription(),0,new HtmlHttpImageGetter(textViewDescription),null));
            // webView.loadDataWithBaseURL("", detailpojo.getTable().get(0).getDescription(), "text/html", "UTF-8", "");

            webView.loadUrl(detailpojo.getUrl());


        }

        @Override
        protected void onPause() {
            super.onPause();

        }

}