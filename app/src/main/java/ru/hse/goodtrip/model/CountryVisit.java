package ru.hse.goodtrip.model;

import java.util.List;

public class CountryVisit {
    Country country;
    List<City> visitedCities;
    public CountryVisit(Country country, List<City> visitedCities){
        this.country = country;
        this.visitedCities = visitedCities;
    }
}
