package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.loginDetailspojo.LoginDetailPojo;

public interface ApiClientGetLoginDetails {

    @GET
    Call<LoginDetailPojo> getLoginDetails(@Url String url);
}
