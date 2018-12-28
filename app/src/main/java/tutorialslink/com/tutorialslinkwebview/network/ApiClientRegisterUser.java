package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClientRegisterUser {
    @POST("RegisterAddUser")
    @FormUrlEncoded
    Call<String> registerUser(@Field("fname") String fname, @Field("email") String email,@Field("Password") String Password,
                              @Field("City") String City,@Field("State") String State,@Field("Country") String Country,
                              @Field("Postal_Code") String Postal_Code);
}
