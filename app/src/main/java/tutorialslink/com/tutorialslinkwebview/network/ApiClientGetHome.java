package tutorialslink.com.tutorialslinkwebview.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorialslink.com.tutorialslinkwebview.pojos.HomePojo;

/**
 * Created by akshaybmsa96 on 20/06/18.
 */

public interface ApiClientGetHome {

@GET("Homelist")
Call<ArrayList<HomePojo>> getHome();


}
