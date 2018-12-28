package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.TutorialDetailActivity;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialLibrary;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * Created by akshaybmsa96 on 14/06/18.
 */

public class CustomAdapterTutorial extends RecyclerView.Adapter<CustomAdapterTutorial.ViewHolder>
{
    private Context context;
    private Activity activity;
    private ArrayList<TutorialLibrary> tablePojo;


    public CustomAdapterTutorial(Context context, ArrayList<TutorialLibrary> tablePojo, Activity activity)
    {

        this.context = context;
        this.activity=activity;
        this.tablePojo=tablePojo;

    }


    @Override
    public CustomAdapterTutorial.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_tutorial, parent, false);
        CustomAdapterTutorial.ViewHolder holder = new CustomAdapterTutorial.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterTutorial.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(tablePojo.get(position).getTCat_name());
      //  String[] str_chop = tablePojo.get(position).getImage().split(",");

      //  System.out.println(""+str_chop[0]);
        Picasso.with(context).load(tablePojo.get(position).getIco()).into(holder.imageView);


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle;
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
            imageView=(ImageView) view.findViewById(R.id.imageView);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {


                Intent intent=new Intent(context, TutorialDetailActivity.class);
                intent.putExtra("data",new Gson().toJson(tablePojo.get(getPosition())));
                context.startActivity(intent);

            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
    }




    @Override
    public int getItemCount() {

        return tablePojo.size();
    }


}