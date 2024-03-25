package ru.hse.goodtrip.network.authentication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.hse.goodtrip.network.authentication.model.AuthenticationResponse;
import ru.hse.goodtrip.network.authentication.model.AuthorizationRequest;
import ru.hse.goodtrip.network.authentication.model.RegisterRequest;

public interface LoginService {

  @POST("/auth/login")
  Call<AuthenticationResponse> login(@Body AuthorizationRequest request);

  @POST("/auth/register")
  Call<AuthenticationResponse> register(@Body RegisterRequest request);
}
