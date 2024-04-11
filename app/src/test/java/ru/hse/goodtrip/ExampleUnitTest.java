package ru.hse.goodtrip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  public static User fakeUser = new User(0, "aboba", "Jane Doe", null, null);

  private ArrayList<Trip> getFakeTrips() {
    Trip testTrip = new Trip("Weekend in Heaven", new ArrayList<>(), LocalDate.now(),
        LocalDate.now(), null,
        1000, new HashSet<>(), fakeUser);
    Coordinates fakeCords = new Coordinates(-34, 131);
    Country fakeCountry = new Country("Russia", fakeCords);
    City fakeCity1 = new City("Moscow", fakeCords, fakeCountry);
    City fakeCity2 = new City("Saint-Petersburg", fakeCords, fakeCountry);
    ArrayList<City> cities = new ArrayList<>();
    cities.add(fakeCity1);
    cities.add(fakeCity2);
    CountryVisit countryVisit = new CountryVisit(fakeCountry, cities);
    ArrayList<CountryVisit> countryVisits = new ArrayList<>();
    countryVisits.add(countryVisit);
    testTrip.setCountries(countryVisits);
    final String note1 =
        "Что вершит судьбу человечества в этом мире?\n"
            + " Некое незримое существо или закон, подобно Длани Господней парящей над миром\n?"
            + " По крайне мере истинно то, что человек не властен даже над своей волей.\n";

    final String note2 = "Я видел такое, что вам, людям, и не снилось.\n"
        + " Атакующие корабли, пылающие над Орионом; Лучи Си, разрезающие мрак у ворот Тангейзера. \n"
        + "Все эти мгновения затеряются во времени, как... слёзы в дожде... Пришло время умирать.\n";

    Note firstNote = new Note("First note", note1, null, fakeCity1);
    Note secondNote = new Note("Second note", note2, null, fakeCity2);
    List<Note> notes = new ArrayList<>();
    notes.add(firstNote);
    notes.add(secondNote);
    testTrip.setNotes(notes);
    ArrayList<Trip> fakeTrips = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      fakeTrips.add(testTrip);
    }
    return fakeTrips;
  }

  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);
  }
}