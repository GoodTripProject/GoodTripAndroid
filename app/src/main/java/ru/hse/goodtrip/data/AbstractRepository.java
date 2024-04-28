package ru.hse.goodtrip.data;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;

abstract class AbstractRepository {
  /**
   * Make a callback.
   *
   * @param resultHolder result Holder.
   * @return callback.
   */
  protected  <T> Callback<T> getCallback(ResultHolder<T> resultHolder, String errorMessage,
      Consumer<T> handler) {
    return new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        Log.println(Log.DEBUG, "Response", "Response " + response.body());
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

  protected AbstractRepository() {
  }
}
