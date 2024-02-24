package ru.hse.goodtrip.ui.trips;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
class PlanTripFormState {
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
    private boolean isDataValid;

    PlanTripFormState(@Nullable Integer tripNameError, @Nullable Integer departureDateError, @Nullable Integer arrivalDateError, @Nullable Integer countryError, @Nullable Integer cityError, @Nullable Integer moneyError) {
        this.tripNameError = tripNameError;
        this.departureDateError = departureDateError;
        this.arrivalDateError = arrivalDateError;
        this.countryError = countryError;
        this.cityError = cityError;
        this.moneyError = moneyError;
        this.isDataValid = false;
    }

    PlanTripFormState(boolean isDataValid){
        this.isDataValid = isDataValid;
    }
    @Nullable
    Integer getTripNameError() {
        return tripNameError;
    }

    @Nullable
    Integer getDepartureDateError() {
        return departureDateError;
    }
    @Nullable
    Integer getArrivalDateError() {
        return arrivalDateError;
    }

    @Nullable
    Integer getCountryError() {
        return countryError;
    }

    @Nullable
    Integer getCityError() {
        return cityError;
    }

    @Nullable
    Integer getMoneyError() {
        return moneyError;
    }
    @Nullable
    Integer getError(){
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

    boolean isDataValid() {
        return isDataValid;
    }
}
