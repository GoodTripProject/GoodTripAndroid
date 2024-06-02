package ru.hse.goodtrip.network.social;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.hse.goodtrip.network.social.entities.User;

/**
 * Service to work with social API.
 */
public interface CommunicationService {
  @POST("/communication/follow")
  Call<String> follow(@Query("userId") int userId, @Query("author") String author,
      @Header("Authorization") String authorization);

  @POST("/communication/unfollow")
  Call<String> unfollow(@Query("userId") int userId, @Query("author") String author,
      @Header("Authorization") String authorization);

  @POST("/communication/like")
  Call<String> like(@Query("userId") int userId, @Query("tripId") int tripId,
      @Header("Authorization") String authorization);

  @POST("/communication/delete_like")
  Call<String> delete_like(@Query("userId") int userId, @Query("tripId") int tripId,
      @Header("Authorization") String authorization);

  @GET("/communication/followers")
  Call<List<User>> getFollowers(@Query("userId") int userId,
      @Header("Authorization") String authorization);

  @GET("/communication/subscriptions")
  Call<List<User>> getSubscriptions(@Query("userId") int userId,
      @Header("Authorization") String authorization);

}
