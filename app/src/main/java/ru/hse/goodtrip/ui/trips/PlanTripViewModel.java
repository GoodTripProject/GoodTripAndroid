package ru.hse.goodtrip.ui.trips;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import ru.hse.goodtrip.R;
import ru.hse.goodtrip.model.CountryVisit;
import ru.hse.goodtrip.model.Note;
import ru.hse.goodtrip.model.ShowPlace;
import ru.hse.goodtrip.model.Trip;

public class PlanTripViewModel extends ViewModel {
    private MutableLiveData<PlanTripFormState> planTripFormState = new MutableLiveData<>();
    PlanTripViewModel(){

    }
    private LocalDate parseDate(String dateString) throws DateTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    // TODO: Implement the ViewModel
    public void createTrip(String name, List<CountryVisit> countries, String startTripDate, String endTripDate, @Nullable byte[] mainPhoto, String moneyInUSD, Set<ShowPlace> interestingPlacesToVisit, List<Note> notes) {
        if (!isNameValid(name)) {
            planTripFormState.setValue(new PlanTripFormState(R.string.not_valid_name, null, null, null, null, null));
        } else if (!isDateValid(startTripDate)) {
            planTripFormState.setValue(new PlanTripFormState(null, R.string.not_valid_date, null, null, null, null));
        } else if (!isDateValid(endTripDate)) {
            planTripFormState.setValue(new PlanTripFormState(null, null, R.string.not_valid_date, null, null, null));
        } else if (!isCountryValid(countries)) {
            planTripFormState.setValue(new PlanTripFormState(null, null, null, R.string.country_error, null, null));
        } else if (!isMoneyValid(moneyInUSD)){
            planTripFormState.setValue(new PlanTripFormState(null, null, null, null, null, null));
        } else {
            planTripFormState.setValue(new PlanTripFormState(true));
            new Trip(name, countries, parseDate(startTripDate), parseDate(endTripDate), mainPhoto, Integer.parseInt(moneyInUSD), interestingPlacesToVisit);
        }
    }

    public MutableLiveData<PlanTripFormState> getPlanTripFormState() {
        return planTripFormState;
    }

    private boolean isDateValid(String dateString) {
        // check date format is dd.mm.yyyy
        try {
            parseDate(dateString);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }

    private boolean isCountryValid(List<CountryVisit> countries) {
        return true;
    }

    private boolean isMoneyValid(String moneyInUSD) {
        try {
            return Integer.parseInt(moneyInUSD) >= 0;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean isNameValid(String name) {
        return name != null && name.trim().length() <= 32;
    }
}