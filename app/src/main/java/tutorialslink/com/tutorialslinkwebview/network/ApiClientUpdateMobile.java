package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiClientUpdateMobile {

    @GET
    Call<String> updateMobile(@Url String url);
}
