package ru.hse.goodtrip.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public  class Result<T> {

  // hide the private constructor to limit subclass types (Success, Error)
  private Result() {
  }

  public boolean isSuccess() {
    return (this instanceof Result.Success);
  }

  /**
   * Success result.
   */
  @ToString
  @Getter
  @AllArgsConstructor
  public static final class Success<T> extends Result<T> {

    private final T data;
  }

  /**
   * Error result.
   */
  @ToString
  @Getter
  @AllArgsConstructor
  public static final class Error<T> extends Result<T> {

    private final Exception error;
  }


}