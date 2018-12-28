package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorialslink.com.tutorialslinkwebview.pojos.FeedPojo;

/**
 * Created by akshaybmsa96 on 07/06/18.
 */

public interface ApiClientGetArticle {
    @GET("GetAllNewsbyCat?cat=3")
    Call<FeedPojo> getArticle();
}
