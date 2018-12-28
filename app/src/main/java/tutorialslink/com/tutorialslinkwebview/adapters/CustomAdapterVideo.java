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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.VideoActivity;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedTablePojo;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * Created by akshaybmsa96 on 13/06/18.
 */

public class CustomAdapterVideo extends RecyclerView.Adapter<CustomAdapterVideo.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<FeedTablePojo> tablePojo;
    private ApiClientGetArticleDetail apiClientGetArticleDetail;
    private DetailPojo detailPojo;


    public CustomAdapterVideo(Context context, ArrayList<FeedTablePojo> tablePojo, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.tablePojo=tablePojo;


    }


    @Override
    public CustomAdapterVideo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_rest, parent, false);
        CustomAdapterVideo.ViewHolder holder = new CustomAdapterVideo.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterVideo.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(tablePojo.get(position).getTitle());
        String[] str_chop = tablePojo.get(position).getImage().split(",");
        Picasso.with(context).load("https://tutorialslink.com/"+str_chop[0]).into(holder.imageView);

        if(tablePojo.get(position).getFirst_Name()!=null &&  !tablePojo.get(position).getFirst_Name().equals(""))
            holder.textViewAuthor.setText("By " + tablePojo.get(position).getFirst_Name() + " " + tablePojo.get(position).getLast_Name());

        else {
            holder.textViewAuthor.setText("");
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle,textViewAuthor;
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
            textViewAuthor=(TextView)view.findViewById(R.id.textViewAuthor);
            imageView=(ImageView) view.findViewById(R.id.imageView);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {
                String sr_no = tablePojo.get(getPosition()).getSr_No();
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

                    Intent intent=new Intent(context, VideoActivity.class);
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

        return tablePojo.size();
    }


}