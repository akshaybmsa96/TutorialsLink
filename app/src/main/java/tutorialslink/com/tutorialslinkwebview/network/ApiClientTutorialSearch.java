package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.searchpojo.SearchPojo;

public interface ApiClientTutorialSearch {
    @GET
    Call<SearchPojo> searchTut(@Url String url);
}
