package ru.hse.goodtrip.network;

import lombok.Setter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Singleton class which saves basic settings about interaction with server API.
 */
public class NetworkManager {
  private static volatile  NetworkManager instance;
  private final Retrofit retrofit;
  @Setter
  private String baseUrl;

  private NetworkManager(){
    retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }
  /**
   * Creates instance of service.
   *
   * @param tClass interface of retrofit service to create.
   * @param <T>    type of interface.
   * @return instance of interface.
   */
  public <T> T getInstanceOfService(Class<T> tClass) {
    return retrofit.create(tClass);
  }

  /**
   * @return instance of Network manager.
   */
  NetworkManager getInstance() {
    if (instance == null){
      instance = new NetworkManager();
    }
    return instance;
  }
}
