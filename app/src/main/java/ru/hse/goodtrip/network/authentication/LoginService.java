package ru.hse.goodtrip.network.authentication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.hse.goodtrip.network.authentication.model.AuthenticationResponse;
import ru.hse.goodtrip.network.authentication.model.AuthorizationRequest;
import ru.hse.goodtrip.network.authentication.model.RegisterRequest;
import ru.hse.goodtrip.network.authentication.model.UrlHandler;

public interface LoginService {

  @POST("/auth/login")
  Call<AuthenticationResponse> login(@Body AuthorizationRequest request);

  @POST("/auth/register")
  Call<AuthenticationResponse> register(@Body RegisterRequest request);

  @POST("/auth/update_photo")
  Call<String> updateUserPhoto(@Query("userId") int userId,@Body UrlHandler urlHandler,
      @Header("Authorization") String authorization);
}
