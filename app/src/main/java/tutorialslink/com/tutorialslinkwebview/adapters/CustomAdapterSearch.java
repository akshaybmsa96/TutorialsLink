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
import tutorialslink.com.tutorialslinkwebview.activities.DetailActivity;
import tutorialslink.com.tutorialslinkwebview.activities.EventsDetailActivity;
import tutorialslink.com.tutorialslinkwebview.activities.TutorialDetailActivity;
import tutorialslink.com.tutorialslinkwebview.activities.VideoActivity;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetEventDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedTablePojo;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo.EventDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.searchpojo.SearchPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CustomAdapterSearch extends RecyclerView.Adapter<CustomAdapterSearch.ViewHolder>
{
    private Context context;
    private Activity activity;
    private SearchPojo searchPojo;
    private ApiClientGetArticleDetail apiClientGetArticleDetail;
    private DetailPojo detailPojo;
    private ApiClientGetEventDetails apiClientGetEventDetails;
    private EventDetailPojo eventdetailPojo;



    public CustomAdapterSearch(Context context, SearchPojo searchPojo, Activity activity)
    {

        this.context = context;
        this.activity=activity;
        this.searchPojo=searchPojo;

    }


    @Override
    public CustomAdapterSearch.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_search, parent, false);
        CustomAdapterSearch.ViewHolder holder = new CustomAdapterSearch.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterSearch.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(searchPojo.getTable().get(position).getTitle());
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        String[] str_chop = searchPojo.getTable().get(position).getImage().split(",");

//        System.out.println(""+str_chop[0]);
        if(str_chop.length>0)
        Picasso.with(context).load("https://tutorialslink.com/"+str_chop[0]).into(holder.imageView);


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
                if(searchPojo.getTable().get(getPosition()).getCategory().equals("2"))
                {
                    String sr_no = searchPojo.getTable().get(getPosition()).getSr_no();
                    String cat = searchPojo.getTable().get(getPosition()).getCategory();
                    getDetailsTutorails(sr_no, cat);
                }

                else if(searchPojo.getTable().get(getPosition()).getCategory().equals("6"))
                {
                    String sr_no = searchPojo.getTable().get(getPosition()).getSr_no();
                    String cat = searchPojo.getTable().get(getPosition()).getCategory();
                    getDetailsEvents(sr_no, cat);
                }

                else
                {
                    String sr_no = searchPojo.getTable().get(getPosition()).getSr_no();
                    String cat = searchPojo.getTable().get(getPosition()).getCategory();
                    getDetails(sr_no, cat);
                }

            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void getDetailsEvents(String sr_no, String cat) {

        final ProgressDialog pDialog = new ProgressDialog(context);


        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetEventDetails = ApiClientBase.getApiClient().create(ApiClientGetEventDetails.class);
        Call<EventDetailPojo> call= apiClientGetEventDetails.getEventDetails("eventdetail?ID="+sr_no);
        call.enqueue(new Callback<EventDetailPojo>() {
            @Override
            public void onResponse(Call<EventDetailPojo> call, Response<EventDetailPojo> response) {


                eventdetailPojo=response.body();

                //   Toast.makeText(getContext(),purchaseReportPojo.toString(),Toast.LENGTH_SHORT).show();
                if(eventdetailPojo!=null&&eventdetailPojo.getTable().size()>0) {



                    //  Toast.makeText(context,eventdetailPojo.toString(),Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(context, EventsDetailActivity.class);
                    intent.putExtra("data",new Gson().toJson(eventdetailPojo));
                    context.startActivity(intent);

                    System.out.println(eventdetailPojo.toString());

                }

                else if(eventdetailPojo==null||eventdetailPojo.getTable().size()==0)
                {
                    Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<EventDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

    private void getDetailsTutorails(String sr_no, String cat) {


    }

    private void getDetails(String sr_no, final String cat) {

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



                    if(cat.equals("4"))
                    {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("data", new Gson().toJson(detailPojo));
                        context.startActivity(intent);
                    }

                    else {
//                  Toast.makeText(context,detailPojo.toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("data", new Gson().toJson(detailPojo));
                        context.startActivity(intent);
                    }

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

        return searchPojo.getTable().size();
    }


}