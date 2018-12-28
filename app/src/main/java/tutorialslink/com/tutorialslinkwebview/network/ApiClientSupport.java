package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiClientSupport {

    @POST("Support")
    @FormUrlEncoded
    Call<String> support(@Field("name") String name,@Field("Email") String Email,@Field("Mobile") String Mobile,
                         @Field("Country") String Country,@Field("State") String State,@Field("City") String City,
                         @Field("Subject") String Subject,@Field("Message") String Message);
}
