package tutorialslink.com.tutorialslinkwebview.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorialslink.com.tutorialslinkwebview.pojos.tutorialspojo.TutorialsPojo;

/**
 * Created by akshaybmsa96 on 14/06/18.
 */

public interface ApiClientGetTutorials {

    @GET("Tutorials?top=10")
    Call<ArrayList<TutorialsPojo>> getTutorials();

}
