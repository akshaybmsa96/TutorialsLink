package tutorialslink.com.tutorialslinkwebview.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.AuthorProfileActivity;
import tutorialslink.com.tutorialslinkwebview.activities.DetailActivity;
import tutorialslink.com.tutorialslinkwebview.activities.VideoActivity;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.notificationPojo.NotificationPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CustomAdapterNotifications extends RecyclerView.Adapter<CustomAdapterNotifications.ViewHolder> {
    private Context context;
    private Activity activity;
    private NotificationPojo notificationPojo;
    private ApiClientGetArticleDetail apiClientGetArticleDetail;
    private DetailPojo detailPojo;
    private LoginDetailPojo loginDetailPojo;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;


    public CustomAdapterNotifications(Context context, NotificationPojo notificationPojo, Activity activity) {

        this.context = context;
        this.activity = activity;
        this.notificationPojo = notificationPojo;

    }


    @Override
    public CustomAdapterNotifications.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_notifications, parent, false);
        CustomAdapterNotifications.ViewHolder holder = new CustomAdapterNotifications.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterNotifications.ViewHolder holder, final int position) {

//        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        String name="";


        if(notificationPojo.getTable().get(position).getCategory().equals("Follow"))
        {
            name = "<b>" + notificationPojo.getTable().get(position).getFirst_name() + " " + notificationPojo.getTable().get(position).getLast_name() + "</b>" + " started Following you";

        }

        else if(notificationPojo.getTable().get(position).getCategory().equals("Articles"))
        {
            name = "<b>" + notificationPojo.getTable().get(position).getFirst_name() + " " + notificationPojo.getTable().get(position).getLast_name() + "</b>" + " Uploaded a new Article :" + notificationPojo.getTable().get(position).getTitle();

        }

        else if(notificationPojo.getTable().get(position).getCategory().equals("News"))
        {
            name = "<b>" + notificationPojo.getTable().get(position).getFirst_name() + " " + notificationPojo.getTable().get(position).getLast_name() + "</b>" + " Uploaded a new News :" + notificationPojo.getTable().get(position).getTitle();

        }

        else if(notificationPojo.getTable().get(position).getCategory().equals("Videos"))
        {
            name = "<b>" + notificationPojo.getTable().get(position).getFirst_name() + " " + notificationPojo.getTable().get(position).getLast_name() + "</b>" + " Uploaded a new Video :" + notificationPojo.getTable().get(position).getTitle();

        }


        holder.textViewTitle.setText(Html.fromHtml(name));

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat1 = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(notificationPojo.getTable().get(position).getCreated_At());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat1.format(date);

        holder.textViewDate.setText(formattedDate);

        String[] str_chop = notificationPojo.getTable().get(position).getImage().split(",");

//        System.out.println(""+str_chop[0]);
        if(str_chop.length>0)

        {
            if (str_chop[0].contains("http"))
            {
                Picasso.with(context).load(str_chop[0]).into(holder.imageView);
            }

            else if (!str_chop[0].equals(""))
            {
                Picasso.with(context).load("https://tutorialslink.com/" + str_chop[0]).into(holder.imageView);
            }

        }

    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle, textViewDate;
        CircleImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewDate = view.findViewById(R.id.textViewDate);
            imageView = view.findViewById(R.id.imageView);

        }

        @Override
        public void onClick(View v) {

            if (NetworkCheck.isNetworkAvailable(context)) {

                if(notificationPojo.getTable().get(getPosition()).getCategory().equals("Follow"))
                {
                    getAuthorDetails(notificationPojo.getTable().get(getPosition()).getSr_No());
                }

                else {
                    getDetails(notificationPojo.getTable().get(getPosition()).getSr_No(),notificationPojo.getTable().get(getPosition()).getCategory());
                }


            } else {

                Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public int getItemCount() {
        return notificationPojo.getTable().size();
    }

    private void getDetails(String sr_no, final String type) {

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


                //               Toast.makeText(context,response.body().toString(),Toast.LENGTH_SHORT).show();
                if(detailPojo!=null&&detailPojo.getTable().size()>0) {

//                  Toast.makeText(context,detailPojo.toString(),Toast.LENGTH_SHORT).show();

                    if(type.equals("Videos"))
                    {
                        Intent intent=new Intent(context, VideoActivity.class);
                        intent.putExtra("data",new Gson().toJson(detailPojo));
                        context.startActivity(intent);
                    }
                    else {
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

    private void getAuthorDetails(String id) {

        final ProgressDialog pDialog = new ProgressDialog(context);


        pDialog.setMessage("Fetching Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        // show it
        apiClientGetLoginDetails = ApiClientBase.getApiClient().create(ApiClientGetLoginDetails.class);

        String url = "UserEmail?email="+id;

        System.out.println("url : " + url);

        Call<LoginDetailPojo> call= apiClientGetLoginDetails.getLoginDetails(url);
        call.enqueue(new Callback<LoginDetailPojo>() {
            @Override
            public void onResponse(Call<LoginDetailPojo> call, Response<LoginDetailPojo> response) {

                loginDetailPojo =response.body();

                  //    Toast.makeText(context,""+ loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if(loginDetailPojo!=null) {
                    if(loginDetailPojo.getTable().size()>0) {

                        Intent intent = new Intent(context, AuthorProfileActivity.class);
                        intent.putExtra("data",new Gson().toJson(loginDetailPojo.getTable().get(0)));
                        context.startActivity(intent);
                    }

                    else {
                        Toast.makeText(context,"Empty Data", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show();

                }

                pDialog.dismiss();


            }

            @Override
            public void onFailure(Call<LoginDetailPojo> call, Throwable t) {

                pDialog.dismiss();


                // if(skip==0)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                System.out.println("failure"+"+ : "+t.getMessage());
                System.out.println("failure"+"+ : "+t.getCause());
                System.out.println("failure"+"+ : "+t.toString());
            }
        });

    }

}