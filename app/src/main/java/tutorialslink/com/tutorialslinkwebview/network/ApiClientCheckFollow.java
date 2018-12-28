package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiClientCheckFollow {
    @GET
    Call<String> check(@Url String url);

}
