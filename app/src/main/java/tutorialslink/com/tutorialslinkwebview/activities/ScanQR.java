package tutorialslink.com.tutorialslinkwebview.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientFollow;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class ScanQR extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean b;
    private CodeScannerView scannerView;
    private ApiClientFollow apiClientFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        b = checkAndRequestPermissions();

        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        if (b) {

            startCam();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        //   Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the noresult arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    //      Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                    startCam();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                return;
            }
            // other case lines to check for other
        }
    }

    private boolean checkAndRequestPermissions() {

        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void startCam() {
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                   //     Toast.makeText(ScanQR.this, result.getText(), Toast.LENGTH_LONG).show();
                        if(NetworkCheck.isNetworkAvailable(ScanQR.this)) {

                            if(result.getText().equals(UserSharedPreferenceData.getLoggedInUserID(ScanQR.this)))
                            {
                                Toast.makeText(ScanQR.this,"You cannot follow yourself",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                doOperation(result.getText());
                            }
                        }

                        else {
                            Toast.makeText(ScanQR.this,"Network Unavailable",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        mCodeScanner.startPreview();
    }

    private void doOperation(String id) {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();


        String url = "Follow?authid="+id+"&fid="+UserSharedPreferenceData.getLoggedInUserID(this) + "&type=Follow";
        apiClientFollow = ApiClientBase.getApiClient().create(ApiClientFollow.class);
        Call<String> call = apiClientFollow.followOp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String res = response.body();

                //     Toast.makeText(getContext(),followFollowingPojo.toString(),Toast.LENGTH_SHORT).show();

                if (res != null && res.equals("1")) {
                    Toast.makeText(ScanQR.this,"Now you are following the Author",Toast.LENGTH_LONG).show();
                    finish();
                } else {

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

/*
                Snackbar.make(findViewById(R.id.layout),"Something Went Wrong",Snackbar.LENGTH_INDEFINITE).
                        setAction("Retry", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //    Toast.makeText(DashboardActivity.this,"Snackbar",Toast.LENGTH_SHORT).show();
                        //    finish();
                        //    startActivity(getIntent());

                        recreate();


                    }
                }).show();

                // if(skip==0)
                //  Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());

                */

                pDialog.dismiss();

            }
        });
    }
}

