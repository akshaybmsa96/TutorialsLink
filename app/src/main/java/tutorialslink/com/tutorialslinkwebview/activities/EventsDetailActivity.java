package tutorialslink.com.tutorialslinkwebview.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo.EventDetailPojo;

public class EventsDetailActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private TextView textViewDescription,textViewTime,textViewLocation,textViewTitle,textViewDate;
    private String intentData;
    private EventDetailPojo eventsDetailpojo;
    private Toolbar tb;
    private AppBarLayout mAppBarLayout;
    private ImageView imageViewTop;
    private CollapsingToolbarLayout toolbar_layout;
    private Button buttonRegister;
    private WebView webView;
   // private MapView mapView;
    private GoogleMap gmap;
   private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        tb=(Toolbar)findViewById(R.id.toolbar);
        imageViewTop=(ImageView)findViewById(R.id.imageViewTop);
        //   textViewDescription=(TextView)findViewById(R.id.textViewDescription);
//        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

        toolbar_layout=(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

      //  toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));

        toolbar_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));

        webView=(WebView)findViewById(R.id.webView);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        buttonRegister=(Button)findViewById(R.id.buttonRegister);

        textViewTime=(TextView)findViewById(R.id.textViewTime);
        textViewLocation=(TextView)findViewById(R.id.textViewLocation);
        textViewTitle=(TextView)findViewById(R.id.textViewTitle);
        textViewDate=findViewById(R.id.textViewDate);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventsDetailpojo.getTable().get(0).getURl()));
                startActivity(browserIntent);
            }
        });



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
        tb.setTitleTextColor(Color.WHITE);

        tb.setNavigationIcon(R.mipmap.ic_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intentData=getIntent().getStringExtra("data");
        getSupportActionBar().setTitle("");

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
                    toolbar_layout.setTitle(eventsDetailpojo.getTable().get(0).getTitle());
                    isShow = true;
                } else if(isShow) {
                    toolbar_layout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });



    }

    private void initData() {

      //  mapView.getMapAsync(this);

        mapFragment.getMapAsync(this);

        eventsDetailpojo=new Gson().fromJson(intentData,EventDetailPojo.class);

      //  System.out.println("https://tutorialslink.com"+eventsDetailpojo.getTable().get(0).getImage());

        Picasso.with(this).load("https://tutorialslink.com"+eventsDetailpojo.getTable().get(0).getImage()).into(imageViewTop);

        textViewTime.setText(eventsDetailpojo.getTable().get(0).getStart_time() +" - " + eventsDetailpojo.getTable().get(0).getEnd_time());
        textViewLocation.setText(eventsDetailpojo.getTable().get(0).getVenue());

        textViewTitle.setText(eventsDetailpojo.getTable().get(0).getTitle());

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat1 = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(eventsDetailpojo.getTable().get(0).getEvent_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat1.format(date);

        textViewDate.setText(formattedDate);


        //  textViewDescription.setText( Html.fromHtml(detailpojo.getTable().get(0).getDescription(),0,new HtmlHttpImageGetter(textViewDescription),null));
        webView.loadDataWithBaseURL("", eventsDetailpojo.getTable().get(0).getDescription(), "text/html", "UTF-8", "");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        gmap = googleMap;
        gmap.setMinZoomPreference(10);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));


        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(textViewLocation.getText().toString(), 5);
            if (addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

              //  Log.d("lat-long", "" + lat + "......." + lon);
                final LatLng user = new LatLng(lat, lon);
                /*used marker for show the location */
                Marker hamburg = gmap.addMarker(new MarkerOptions()
                        .position(user)
                        .title(textViewLocation.getText().toString()));
                // Move the camera instantly to hamburg with a zoom of 15.
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15));

                // Zoom in, animating the camera.
                gmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

              //  gmap.getUiSettings().setZoomControlsEnabled(true);
                hamburg.showInfoWindow();
                googleMap.getUiSettings().setMapToolbarEnabled(true);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }
}
