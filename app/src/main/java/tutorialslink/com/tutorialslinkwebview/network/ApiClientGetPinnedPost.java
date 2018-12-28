package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.pinnedPostPojo.PinnedPostPojo;

public interface ApiClientGetPinnedPost {

    @GET
    Call<PinnedPostPojo> getPinnedPost(@Url String url);
}
