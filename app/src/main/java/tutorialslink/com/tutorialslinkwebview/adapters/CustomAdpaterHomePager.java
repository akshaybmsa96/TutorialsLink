package tutorialslink.com.tutorialslinkwebview.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.pojos.highlightsPojo.HighlightsPojo;

public class CustomAdpaterHomePager extends android.support.v4.view.PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    HighlightsPojo highlightsPojos;
    int[] mResources;

    public CustomAdpaterHomePager(Context context, HighlightsPojo highlightsPojos,int[] mResources) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.highlightsPojos=highlightsPojos;
        this.mResources=mResources;
    }




    @Override
    public int getCount() {
        return highlightsPojos.getTable().size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_home, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        TextView textViewTitle = itemView.findViewById(R.id.textViewTitle);
        if(highlightsPojos.getTable().get(position).getImage()!=null && !highlightsPojos.getTable().get(position).getImage().equals(""))
        Picasso.with(mContext).load("https://tutorialslink.com"+highlightsPojos.getTable().get(position).getImage()).into(imageView);

        else {

            imageView.setImageResource(highlightsPojos.getTable().get(position).getBackImage());
            textViewTitle.setText(highlightsPojos.getTable().get(position).getTitle());
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(highlightsPojos.getTable().get(position).getURl()));
                mContext.startActivity(i);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}