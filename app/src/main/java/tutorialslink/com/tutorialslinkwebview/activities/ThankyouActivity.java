package tutorialslink.com.tutorialslinkwebview.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tutorialslink.com.tutorialslinkwebview.R;

public class ThankyouActivity extends AppCompatActivity {

    private String gdata;
    private ImageView imageView;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        gdata = getIntent().getStringExtra("data");
        layout=findViewById(R.id.layout);
        imageView=new ImageView(this);
        layout.addView(imageView);


        if(gdata.equals("1"))
        {
            support();
        }

        else {

            reportBug();
        }

    }

    private void reportBug() {

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.reportbug));
        startThread();

    }

    private void startThread() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(ThankyouActivity.this,Dashboard.class);
                    startActivity(intent);

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    private void support() {

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.support));
        startThread();
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent i=new Intent(this,Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
