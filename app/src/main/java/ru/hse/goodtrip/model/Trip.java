package ru.hse.goodtrip.model;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Trip {
    private List<CountryVisit> countries;
    private Date startTripDate;
    private Date endTripDate;
    private File mainPhoto;
    private int moneyInUSD;
    private Set<ShowPlace> interestingPlacesToVisit;
    private List<Note> notes;

    public Trip(List<CountryVisit> countries, Date startTripDate, Date endTripDate, File mainPhoto, int moneyInUSD, Set<ShowPlace> interestingPlacesToVisit, List<Note> notes) {
        this.countries = countries;
        this.startTripDate = startTripDate;
        this.endTripDate = endTripDate;
        this.mainPhoto = mainPhoto;
        this.moneyInUSD = moneyInUSD;
        this.interestingPlacesToVisit = interestingPlacesToVisit;
        this.notes = notes;
    }
}
