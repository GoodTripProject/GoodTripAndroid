package ru.hse.goodtrip.data.model;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResultHolder<T> {

  private final CountDownLatch latch = new CountDownLatch(1);

  @Getter
  private Result<T> result;

  public void setResult(Result<T> result) {
    this.result = result;
    latch.countDown();
  }

  public void awaitForResult() {
    try {
      latch.await();
    } catch (InterruptedException ignored) {
    }
  }

  public long latchCount() {
    return latch.getCount();
  }
}
