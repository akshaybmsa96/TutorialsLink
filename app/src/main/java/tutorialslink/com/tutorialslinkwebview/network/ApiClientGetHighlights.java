package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import tutorialslink.com.tutorialslinkwebview.pojos.highlightsPojo.HighlightsPojo;

public interface ApiClientGetHighlights {
    @GET("GetFocus")
    Call<HighlightsPojo> getHighlights();
}
