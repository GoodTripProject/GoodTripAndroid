package ru.hse.goodtrip.ui.trips;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.model.City;
import ru.hse.goodtrip.model.Coordinates;
import ru.hse.goodtrip.model.Country;
import ru.hse.goodtrip.model.CountryVisit;
import ru.hse.goodtrip.model.ShowPlace;
import ru.hse.goodtrip.model.Trip;

public class PlanTripViewModel extends ViewModel {

  private final MutableLiveData<PlanTripFormState> planTripFormState = new MutableLiveData<>();
  List<CountryVisit> countries = new ArrayList<>();

  PlanTripViewModel() {

  }

  private LocalDate parseDate(String dateString) throws DateTimeException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    return LocalDate.parse(dateString, formatter);
  }

  public void createTrip(String name, String startTripDate,
      String endTripDate, @Nullable byte[] mainPhoto, String moneyInUSD,
      Set<ShowPlace> interestingPlacesToVisit) {
    if (isNameNotValid(name)) {
      planTripFormState.setValue(
          new PlanTripFormState(R.string.not_valid_name, null, null, null, null, null));
    } else if (isDateNotValid(startTripDate)) {
      planTripFormState.setValue(
          new PlanTripFormState(null, R.string.not_valid_date, null, null, null, null));
    } else if (isDateNotValid(endTripDate)) {
      planTripFormState.setValue(
          new PlanTripFormState(null, null, R.string.not_valid_date, null, null, null));
    } else if (parseDate(startTripDate).isAfter(parseDate(endTripDate))) {
      planTripFormState.setValue(
          new PlanTripFormState(null, null, R.string.not_valid_date_order, null, null, null));
    } else if (isMoneyNotValid(moneyInUSD)) {
      planTripFormState.setValue(new PlanTripFormState(null, null, null, null, null, null));
    } else {
      planTripFormState.setValue(new PlanTripFormState(true));
      // TODO make interaction with trip repository
      new Trip(name, countries, parseDate(startTripDate), parseDate(endTripDate), mainPhoto,
          Integer.parseInt(moneyInUSD), interestingPlacesToVisit);
    }
  }

  public MutableLiveData<PlanTripFormState> getPlanTripFormState() {
    return planTripFormState;
  }

  private boolean isDateNotValid(String dateString) {
    // check date format is dd.mm.yyyy
    try {
      parseDate(dateString);
    } catch (DateTimeException e) {
      return true;
    }
    return false;
  }

  private boolean isMoneyNotValid(String moneyInUSD) {
    try {
      return Integer.parseInt(moneyInUSD) < 0;
    } catch (NumberFormatException nfe) {
      return true;
    }
  }

  // code from https://stackoverflow.com/questions/9760341/retrieve-a-list-of-countries-from-the-android-os]
  public List<String> getCountriesList() {
    Locale[] locales = Locale.getAvailableLocales();
    ArrayList<String> countries = new ArrayList<>();
    for (Locale locale : locales) {
      String country = locale.getDisplayCountry();
      if (country.trim().length() > 0 && !countries.contains(country)) {
        countries.add(country);
      }
    }
    Collections.sort(countries);
    return countries;
  }

  private boolean isNameNotValid(String name) {
    return !(name != null && name.trim().length() <= 32);
  }

  public void addCountry(String countryName, List<String> citiesName) {
    Country country = new Country(countryName, new Coordinates(BigDecimal.ZERO, BigDecimal.ZERO));
    List<City> cities = new ArrayList<>();
    for (String cityName : citiesName) {
      cities.add(new City(cityName, new Coordinates(BigDecimal.ZERO, BigDecimal.ZERO), country));
    }
    CountryVisit countryVisit = new CountryVisit(country, cities);
    countries.add(countryVisit);
  }
}