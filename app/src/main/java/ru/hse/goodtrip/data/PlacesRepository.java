package ru.hse.goodtrip.data;

import androidx.annotation.Nullable;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import retrofit2.Call;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.places.PlacesService;
import ru.hse.goodtrip.network.places.model.PlaceRequest;
import ru.hse.goodtrip.network.places.model.PlaceRequest.PlacesTypes;
import ru.hse.goodtrip.network.places.model.PlaceResponse;

/**
 * Repository to work with Places.
 */
public class PlacesRepository extends AbstractRepository {

  private static volatile PlacesRepository instance;

  private final PlacesService placesService;

  private PlacesRepository() {
    super();
    placesService = NetworkManager.getInstance().getInstanceOfService(PlacesService.class);
  }


  /**
   * Get coordinate of place.
   *
   * @param placeName name of place.
   * @param token     Jwt token.
   * @return CompletableFuture - point of requested place.
   */
  @SuppressWarnings({"unchecked", "ConstantConditions"})
  public CompletableFuture<Result<Point>> getPlaceCoordinate(String placeName, String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = placesService.getCoordinates(
        placeName, getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder,
        "Note with this id not exists", (result) -> {
        }));
    return getCompletableFuture(resultHolder)
        .thenApplyAsync(result -> {
          if (result.isSuccess()) {
            HashMap<Object, Object> response = (HashMap<Object, Object>)
                ((Result.Success<Object>) result).getData();
            return new Result.Success<>(
                createNewPoint((Double) response.get("latitude"),
                    (Double) response.get("longitude")));
          }
          return new Result.Error<>(new Exception(result.toString()));
        });
  }

  @SuppressWarnings("unchecked")
  public CompletableFuture<Result<List<PlaceResponse>>> getNearPlaces(double lat,
      double lng,
  int radius, @Nullable String rankBy,
   @Nullable PlacesTypes type, String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = placesService.getNearPlaces(
        new PlaceRequest(lng, lat, radius, rankBy, type), getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Note with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder).thenApplyAsync(result -> {
      if (result.isSuccess()) {
        List<LinkedHashMap<Object,Object>> bareResponse = (List<LinkedHashMap<Object,Object>>)
            (((Result.Success<?>) result).getData());
        ObjectMapper mapper = new ObjectMapper();
        List<PlaceResponse> response = bareResponse
            .stream()
            .map(map -> mapper.convertValue(map,PlaceResponse.class))
            .collect(Collectors.toList());
        return new Result.Success<>(response);
      }
      return new Result.Error<>(new Exception(result.toString()));
    });
  }

  /**
   * Get instance of PlacesRepository.
   *
   * @return instance of PlacesRepository.
   */
  public static PlacesRepository getInstance() {
    if (instance == null) {
      instance = new PlacesRepository();
    }
    return instance;
  }
}
