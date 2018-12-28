package tutorialslink.com.tutorialslinkwebview.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;

import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialLibrary;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class TutorialDetailActivity extends AppCompatActivity {

    private String intentData;
    private TutorialLibrary detailpojo;
    private Toolbar tb;
    private WebView webView;
    private TextView textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_detail);



        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        textViewTitle=(TextView)findViewById(R.id.textViewTitle);

        webView=(WebView)findViewById(R.id.webView);



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

                Snackbar.make((CoordinatorLayout)findViewById(R.id.layout),"Something went Wrong",Snackbar.LENGTH_INDEFINITE).
                        setAction("Retry", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                                //    finish();
                                //    startActivity(getIntent());

                                webView.loadUrl(webView.getUrl());


                            }
                        }).show();

            }
        });



        intentData=getIntent().getStringExtra("data");


        initData();



    }

    private void initData() {

        detailpojo=new Gson().fromJson(intentData,TutorialLibrary.class);

        textViewTitle.setText(detailpojo.getTCat_name());

        getSupportActionBar().setTitle(detailpojo.getTCat_name());


        //  textViewDescription.setText( Html.fromHtml(detailpojo.getTable().get(0).getDescription(),0,new HtmlHttpImageGetter(textViewDescription),null));
        // webView.loadDataWithBaseURL("", detailpojo.getTable().get(0).getDescription(), "text/html", "UTF-8", "");

        if (NetworkCheck.isNetworkAvailable(this)) {
            webView.loadUrl(detailpojo.getUrl());

        }

        else
        {
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

    }
}
