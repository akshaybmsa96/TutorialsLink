package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiClientPostSocialLoginData {

    @POST("AddUser")
    @FormUrlEncoded
    Call<String> postData(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("picture") String picture,@Field("link")  String link);

}
