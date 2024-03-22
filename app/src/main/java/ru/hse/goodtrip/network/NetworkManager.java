package ru.hse.goodtrip.network;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Singleton class which saves basic settings about interaction with server API.
 */
public class NetworkManager {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("") // TODO: add getting url from properties
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  /**
   * Creates instance of service.
   *
   * @param tClass interface of retrofit service to create.
   * @param <T> type of interface.
   * @return instance of interface.
   */
  public <T> T getInstanceOfService(Class<T> tClass) {
    return retrofit.create(tClass);
  }

  /**
   * Holder of network manager.
   */
  private static class NetworkManagerHolder {

    public static final NetworkManager HOLDER_INSTANCE = new NetworkManager();
  }

  /**
   * @return instance of Network manager.
   */
  NetworkManager getInstance() {
    return NetworkManagerHolder.HOLDER_INSTANCE;
  }
}
