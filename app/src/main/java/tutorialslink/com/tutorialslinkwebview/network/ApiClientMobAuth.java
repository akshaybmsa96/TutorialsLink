package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.MobAuthPOjo.SendMobPojo;

public interface ApiClientMobAuth {
    @GET
    Call<SendMobPojo> sendMob(@Url String url);
}
