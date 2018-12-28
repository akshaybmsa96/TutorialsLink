package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.activities.SearchActivity;
import tutorialslink.com.tutorialslinkwebview.pojos.eventspojo.TablesPojo;

public interface ApiClientCheckEmail {

    @GET
    Call<String> check(@Url String url);
}

