package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.eventDetailPojo.EventDetailPojo;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public interface ApiClientGetEventDetails {
    @GET
    Call<EventDetailPojo> getEventDetails(@Url String url);
}
