package ru.hse.goodtrip.data.model.tripRequests.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AddCountryRequest {

    String country;

    List<City> cities;
}
