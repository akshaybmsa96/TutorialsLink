package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.MobAuthPOjo.VerifyOtpPojo;

public interface ApiClientOtpAuth {
    @GET
    Call<VerifyOtpPojo> sendMob(@Url String url);
}
