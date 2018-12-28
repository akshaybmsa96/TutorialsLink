package tutorialslink.com.tutorialslinkwebview.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.searchpojo.SearchPojo;

public interface ApiClientSearch {
    @GET
    Call<SearchPojo> search(@Url String url);
}
