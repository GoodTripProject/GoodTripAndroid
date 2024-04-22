package ru.hse.goodtrip.data;

import android.util.Log;
import java.util.concurrent.CompletableFuture;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;

abstract class AbstractRepository {

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
