package io.hackathon.www;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by linroid on 6/6/15.
 */
public interface WeiboService {
    @GET("/users/show.json")
    void userInfo(@Query("access_token") String accessToken,  @Query("uid") String uid, Callback<WeiboUser> callback);
}
