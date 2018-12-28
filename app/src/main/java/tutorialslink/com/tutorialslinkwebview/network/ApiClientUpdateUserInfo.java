package tutorialslink.com.tutorialslinkwebview.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiClientUpdateUserInfo {

    @POST("AuthorUpdate")
    @FormUrlEncoded
    Call<String> updateUser(@Field("fname") String fname,@Field("email") String email,
                            @Field("City") String City,@Field("State") String State, @Field("Country") String Country,
                            @Field("Postal_Code") String Postal_Code,@Field("Technologies") String Technologies,
                            @Field("Awards") String Awards,@Field("About_us") String About_us,
                            @Field("Blog_link") String Blog_link,@Field("Faebook_profile") String Faebook_profile,
                            @Field("Linkedin_profile") String Linkedin_profile,@Field("Twitter_profile") String Twitter_profile,
                            @Field("Github_profile") String Github_profile);

}
