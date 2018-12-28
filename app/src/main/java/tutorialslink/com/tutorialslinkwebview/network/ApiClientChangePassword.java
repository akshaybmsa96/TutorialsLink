package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClientChangePassword {

    @POST("Change_Password")
    @FormUrlEncoded
    Call<String> changePassword(@Field("userid")String userid,@Field("old_password") String old_password,@Field("new_password") String new_password);

}
