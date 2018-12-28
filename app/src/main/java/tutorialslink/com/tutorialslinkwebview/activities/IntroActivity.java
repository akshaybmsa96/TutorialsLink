package tutorialslink.com.tutorialslinkwebview.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.adapters.CustomPagerAdapter;
import tutorialslink.com.tutorialslinkwebview.transition.DepthPageTransformer;
import tutorialslink.com.tutorialslinkwebview.transition.ZoomOutPageTransformer;

public class IntroActivity extends AppCompatActivity {

    private Button buttonContinue;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        final int[] mResources = {
                R.drawable.welcome1,
                R.drawable.welcome5,
                R.drawable.welcome2,
                R.drawable.welcome3,
                R.drawable.welcome4
        };


        buttonContinue=(Button)findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        setButtonDisabled();

        viewPager=(ViewPager)findViewById(R.id.viewPager);


        viewPager.setPageTransformer(true, new DepthPageTransformer());

        //   viewPager.setPageTransformer(true, new DepthPageTransformer());

        CustomPagerAdapter customPagerAdapter=new CustomPagerAdapter(this,mResources);
        viewPager.setAdapter(customPagerAdapter);
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //    Toast.makeText(getApplicationContext(),position+" on page scrolled",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageSelected(int position) {
             //   Toast.makeText(getApplicationContext(),position+" on page selected",Toast.LENGTH_SHORT).show();

                if(position==mResources.length-1)
                {
                   setButtonEnabled();
                }

                else {
                    setButtonDisabled();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

             //   Toast.makeText(getApplicationContext(),state+" on page scroll",Toast.LENGTH_SHORT).show();

            }
        });


    }

    void setButtonEnabled()
    {
        buttonContinue.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        buttonContinue.setEnabled(true);
    }

    void setButtonDisabled()
    {
        buttonContinue.setBackgroundColor(getResources().getColor(R.color.grey));
        buttonContinue.setEnabled(false);
    }
}
