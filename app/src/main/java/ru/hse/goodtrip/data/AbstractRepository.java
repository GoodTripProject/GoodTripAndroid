package ru.hse.goodtrip.data;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;

abstract class AbstractRepository {

  protected static final int SRID = 4326;
  protected static final GeometryFactory factory = new GeometryFactory();


  protected AbstractRepository() {
  }

  /**
   * Constructs Point from x and y coordinate.
   *
   * @param x x coordinate.
   * @param y y coordinate.
   * @return Point.
   */
  protected static Point createNewPoint(double x, double y) {
    Point point = factory.createPoint(new CoordinateXY(x, y));
    point.setSRID(SRID);
    return point;
  }

  /**
   * Make a callback.
   *
   * @param resultHolder result Holder.
   * @return callback.
   */
  protected <T> Callback<T> getCallback(ResultHolder<T> resultHolder, String errorMessage,
      Consumer<T> handler) {
    return new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        Log.d("Response", "Response " + response.body());
        Log.d("Response", "Response for request" + call.request());
        T responseBody = response.body();
        if (responseBody == null) {
          resultHolder.setResult(
              new Result.Error<>(new InterruptedException(errorMessage))
          );
        } else {
          handler.accept(responseBody);
          resultHolder.setResult(new Result.Success<>(responseBody));
        }
      }

      @Override
      public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
        Log.println(Log.DEBUG, "Response", "Response failed" + throwable);
        Log.d("Response", "Response for request" + call.request());
        resultHolder.setResult(new Result.Error<>(new InterruptedException(errorMessage)));
      }
    };
  }

  protected <T> CompletableFuture<Result<T>> getCompletableFuture(ResultHolder<T> resultHolder) {
    return CompletableFuture.supplyAsync(() -> {
          Log.d(this.getClass().getName(),
              "Result holder started to wait latch, latch count now:" + resultHolder.latchCount());
          resultHolder.awaitForResult();
          Log.d(this.getClass().getName(),
              "Result holder is latched, result:" + resultHolder.getResult());
          return resultHolder.getResult();
        }
    );
  }

  /**
   * Wraps token.
   *
   * @param token Bare jwt token.
   * @return Wrapped token.
   */
  protected String getWrappedToken(String token) {
    return "Bearer " + token;
  }
}
