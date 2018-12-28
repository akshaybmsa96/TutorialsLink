package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tutorialslink.com.tutorialslinkwebview.pojos.authorSearchPojo.AuthorSearchPojo;

public interface ApiClientSearchAuthor {

    @GET
    Call<AuthorSearchPojo> searchAuthor(@Url String url);

}
