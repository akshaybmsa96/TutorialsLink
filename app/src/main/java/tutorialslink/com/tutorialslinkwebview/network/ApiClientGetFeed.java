package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.feedPojo.FeedPojo;

public interface ApiClientGetFeed {

    @GET
    Call<FeedPojo> getFeeds(@Url String url);
}
