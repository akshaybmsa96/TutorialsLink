package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.notificationPojo.NotificationPojo;

public interface ApiClientGetNotifications {

    @GET
    Call<NotificationPojo> getNotifications(@Url String url);

}
