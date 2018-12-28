package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.detailspojo.DetailPojo;

/**
 * Created by akshaybmsa96 on 08/06/18.
 */

public interface ApiClientGetArticleDetail {
    @GET
    Call<DetailPojo> getArticle(@Url String url);
}
