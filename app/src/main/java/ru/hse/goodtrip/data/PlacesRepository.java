package ru.hse.goodtrip.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import retrofit2.Call;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.places.PlacesService;
import ru.hse.goodtrip.network.places.model.PlaceRequest;
import ru.hse.goodtrip.network.places.model.PlaceResponse;

public class PlacesRepository extends AbstractRepository {

  private final PlacesService placesService;
  private static final GeometryFactory factory = new GeometryFactory();

  private PlacesRepository() {
    super();
    placesService = NetworkManager.getInstance().getInstanceOfService(PlacesService.class);
  }

  private static Point createNewPoint(double x, double y) {
    return factory.createPoint(new CoordinateXY(x, y));
  }

  @SuppressWarnings("unchecked")
  public CompletableFuture<Result<Point>> getPlaceCoordinate(String placeName, String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = placesService.getNearPlaces(
        new PlaceRequest(placeName, 10, null, null), getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Note with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder).thenApplyAsync(result -> {
      if (result instanceof List) {
        PlaceResponse response = ((List<PlaceResponse>) result).get(0);
        return new Result.Success<>(createNewPoint(response.getLat(), response.getLng()));
      }
      return new Result.Error<>(new Exception("Invalid Request"));
    });
  }
}
