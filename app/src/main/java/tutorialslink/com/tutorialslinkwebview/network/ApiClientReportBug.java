package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClientReportBug {
    @POST("ReportBug")
    @FormUrlEncoded
    Call<String> reportBug(@Field("Email") String Email,@Field("Name") String Name,@Field("Message") String Message);
}
