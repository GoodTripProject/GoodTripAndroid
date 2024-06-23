package ru.hse.goodtrip.ui.trips.plantrip;

import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 * PlanTripFormState check if entered data is valid.
 */
@Getter
class PlanTripFormState {

  private final boolean isDataValid;
  @Nullable
  private Integer tripNameError;
  @Nullable
  private Integer departureDateError;
  @Nullable
  private Integer arrivalDateError;
  @Nullable
  private Integer countryError;
  @Nullable
  private Integer cityError;
  @Nullable
  private Integer moneyError;

  PlanTripFormState(@Nullable Integer tripNameError, @Nullable Integer departureDateError,
      @Nullable Integer arrivalDateError, @Nullable Integer countryError,
      @Nullable Integer cityError, @Nullable Integer moneyError) {
    this.tripNameError = tripNameError;
    this.departureDateError = departureDateError;
    this.arrivalDateError = arrivalDateError;
    this.countryError = countryError;
    this.cityError = cityError;
    this.moneyError = moneyError;
    this.isDataValid = false;
  }

  PlanTripFormState(boolean isDataValid) {
    this.isDataValid = isDataValid;
  }

  @Nullable
  Integer getError() {
    List<Integer> objects = Arrays.asList(
        getTripNameError(),
        getDepartureDateError(),
        getArrivalDateError(),
        getCountryError(),
        getCityError(),
        getMoneyError()
    );
    for (int index = 0; index < objects.size(); index++) {
      if (objects.get(index) != null) {
        return objects.get(index);
      }
    }
    return null;
  }
}
