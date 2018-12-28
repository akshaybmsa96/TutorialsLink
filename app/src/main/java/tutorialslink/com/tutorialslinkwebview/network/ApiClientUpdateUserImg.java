package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiClientUpdateUserImg {
    @GET
    Call<String> updateUserImg(@Url String url);
}
