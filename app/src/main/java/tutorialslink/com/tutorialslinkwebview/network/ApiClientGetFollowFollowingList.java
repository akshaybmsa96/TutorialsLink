package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.followfollwingpojo.FollowFollowingPojo;

public interface ApiClientGetFollowFollowingList {

    @GET
    Call<FollowFollowingPojo> getList(@Url String url);


}
