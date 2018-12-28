package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.EventsDetailActivity;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetEventDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo.EventDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.eventspojo.EventsPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public class CustomAdapterEvent extends RecyclerView.Adapter<CustomAdapterEvent.ViewHolder>
{

    private Context context;
    private Activity activity;
    private ArrayList<EventsPojo> eventsPojos;
    private ApiClientGetEventDetails apiClientGetEventDetails;
    private EventDetailPojo eventdetailPojo;


    public CustomAdapterEvent(Context context, ArrayList<EventsPojo> eventsPojos, Activity activity) {

        this.context = context;
        this.activity=activity;
        this.eventsPojos=eventsPojos;


    }


    @Override
    public CustomAdapterEvent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_events, parent, false);
        CustomAdapterEvent.ViewHolder holder = new CustomAdapterEvent.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterEvent.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(eventsPojos.get(position).getTitle());
        holder.textViewVenue.setText(eventsPojos.get(position).getCity()+", "+eventsPojos.get(position).getCountry());



        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat1 = new SimpleDateFormat("dd");
        DateFormat targetFormat2 = new SimpleDateFormat("MMM");
        Date date = null;
        try {
            date = originalFormat.parse(eventsPojos.get(position).getEvent_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat2.format(date);

        holder.textViewDateMonth.setText(formattedDate);

        formattedDate = targetFormat1.format(date);

        holder.textViewDateDay.setText(formattedDate);




//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        try {
//            holder.textViewDate.setText(sdf.parse(eventsPojos.get(position).getEvent_date()).toLocaleString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle,textViewDateMonth,textViewDateDay,textViewVenue;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
            textViewDateMonth=(TextView)view.findViewById(R.id.textViewDateMonth);
            textViewDateDay=(TextView)view.findViewById(R.id.textViewDateDay);
            textViewVenue=(TextView)view.findViewById(R.id.textViewVenue);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {
                String sr_no = eventsPojos.get(getPosition()).getEvent_id();
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



    @Override
    public int getItemCount() {

        return eventsPojos.size();
    }


}