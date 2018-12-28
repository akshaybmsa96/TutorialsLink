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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorialslink.com.tutorialslinkwebview.R;
import tutorialslink.com.tutorialslinkwebview.activities.AuthorProfileActivity;
import tutorialslink.com.tutorialslinkwebview.global.UserSharedPreferenceData;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientBase;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetArticleDetail;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetEventDetails;
import tutorialslink.com.tutorialslinkwebview.network.ApiClientGetLoginDetails;
import tutorialslink.com.tutorialslinkwebview.pojos.authorSearchPojo.AuthorSearchPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;
import tutorialslink.com.tutorialslinkwebview.pojos.searchpojo.SearchPojo;
import tutorialslink.com.tutorialslinkwebview.util.NetworkCheck;

public class CustomAdapterSearchAuthor extends RecyclerView.Adapter<CustomAdapterSearchAuthor.ViewHolder>
{
    private Context context;
    private Activity activity;
    private AuthorSearchPojo authorSearchPojo;
    private LoginDetailPojo loginDetailPojo;
    private ApiClientGetLoginDetails apiClientGetLoginDetails;


    public CustomAdapterSearchAuthor(Context context, AuthorSearchPojo authorSearchPojo, Activity activity)
    {
        this.context = context;
        this.activity=activity;
        this.authorSearchPojo=authorSearchPojo;

    }


    @Override
    public CustomAdapterSearchAuthor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout_author_search, parent, false);
        CustomAdapterSearchAuthor.ViewHolder holder = new CustomAdapterSearchAuthor.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(CustomAdapterSearchAuthor.ViewHolder holder, int position)
    {

        holder.textViewTitle.setText(authorSearchPojo.getTable().get(position).getFirst_name()+" "+authorSearchPojo.getTable().get(position).getLast_name());
//        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        String[] str_chop = authorSearchPojo.getTable().get(position).getPicture().split(",");

//        System.out.println(""+str_chop[0]);
        if(str_chop.length>0) {
            if(str_chop[0].contains("http")||str_chop[0].contains("https"))
            {
                Picasso.with(context).load(str_chop[0]).into(holder.imageView);
            }
            else {
                Picasso.with(context).load("https://tutorialslink.com/" + str_chop[0]).into(holder.imageView);
            }
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView textViewTitle;
        CircleImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textViewTitle=view.findViewById(R.id.textViewTitle);
            imageView=view.findViewById(R.id.imageView);

        }

        @Override
        public void onClick(View v) {

            if(NetworkCheck.isNetworkAvailable(context)) {
            getAuthorDetails(authorSearchPojo.getTable().get(getPosition()).getEmail());

            }

            else {

                Toast.makeText(context,"Network Unavailable",Toast.LENGTH_SHORT).show();
            }

        }
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

                //      Toast.makeText(getApplicationContext(),loginDetailPojo.getTable().toString()+"", Toast.LENGTH_SHORT).show();

                if(loginDetailPojo!=null) {
                    if(loginDetailPojo.getTable().size()>0) {

                        Intent intent = new Intent(context, AuthorProfileActivity.class);
                        intent.putExtra("data",new Gson().toJson(loginDetailPojo.getTable().get(0)));
                        context.startActivity(intent);
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


    @Override
    public int getItemCount() {

        return authorSearchPojo.getTable().size();
    }

}