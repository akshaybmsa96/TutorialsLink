package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorialslink.com.tutorialslinkwebview.pojos.eventspojo.TablesPojo;

/**
 * Created by akshaybmsa96 on 09/06/18.
 */

public interface ApiClientGetEvents {

    @GET("events?top=50")
    Call<TablesPojo> getEvents();

}
