package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.DetailActivity;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.feedPojo.FeedPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * Created by akshaybmsa96 on 07/06/18.
 */

public class CustomAdapterFeed extends RecyclerView.Adapter<CustomAdapterFeed.ViewHolder>
{
    private Context context;
    private Activity activity;
    private FeedPojo feedPojo;
    private ApiClientGetArticleDetail apiClientGetArticleDetail;
    private DetailPojo detailPojo;


    public CustomAdapterFeed(Context context, FeedPojo feedPojo, Activity activity)
    {

        this.context = context;
        this.activity=activity;
        this.feedPojo=feedPojo;

    }


    @Override
    public CustomAdapterFeed.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_feed, parent, false);
        CustomAdapterFeed.ViewHolder holder = new CustomAdapterFeed.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterFeed.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(feedPojo.getTable().get(position).getTitle());

        String[] str_chop = feedPojo.getTable().get(position).getImage().split(",");

        if (!feedPojo.getTable().get(position).getImage().equals("0")&& !feedPojo.getTable().get(position).getImage().equals("0,")) {
            //

            // System.out.println(""+str_chop[0]);
            if (str_chop.length > 0)
                Picasso.with(context).load("https://tutorialslink.com/" + str_chop[0]).into(holder.imageView);
        }

//        else {
//            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.defaultfeed_icon));
//        }


        if(feedPojo.getTable().get(position).getFirst_name()!=null &&  !feedPojo.getTable().get(position).getFirst_name().equals(""))
        holder.textViewAuthor.setText(feedPojo.getTable().get(position).getCategory()+" By " + feedPojo.getTable().get(position).getFirst_name() + " " + feedPojo.getTable().get(position).getLast_name());

        else {
            holder.textViewAuthor.setText("");
        }

        holder.textViewTags.setText(feedPojo.getTable().get(position).getTags());

        holder.textViewDescription.setText(feedPojo.getTable().get(position).getShort_Description());

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle,textViewAuthor,textViewTags,textViewDescription;
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
            textViewAuthor=(TextView)view.findViewById(R.id.textViewAuthor);
            textViewTags=view.findViewById(R.id.textViewTags);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            textViewDescription=view.findViewById(R.id.textViewDescription);

           // textViewAuthor.setVisibility(View.VISIBLE);
           //  textViewDescription.setVisibility(View.GONE);
           //   textViewTags.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {
                String sr_no = feedPojo.getTable().get(getPosition()).getSr_No();
                getDetails(sr_no);
            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void getDetails(String sr_no) {

        final ProgressDialog pDialog = new ProgressDialog(context);


        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetArticleDetail = ApiClientBase.getApiClient().create(ApiClientGetArticleDetail.class);
        Call<DetailPojo> call= apiClientGetArticleDetail.getArticle("Article?id="+sr_no);
        call.enqueue(new Callback<DetailPojo>() {
            @Override
            public void onResponse(Call<DetailPojo> call, Response<DetailPojo> response) {


                detailPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(detailPojo!=null&&detailPojo.getTable().size()>0) {



//                  Toast.makeText(context,detailPojo.toString(),Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(context, DetailActivity.class);
                    intent.putExtra("data",new Gson().toJson(detailPojo));
                    context.startActivity(intent);

                         System.out.println(detailPojo.toString());

                }

                else if(detailPojo==null||detailPojo.getTable().size()==0)
                {
                    Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<DetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }


    @Override
    public int getItemCount() {

        return feedPojo.getTable().size();
    }


}