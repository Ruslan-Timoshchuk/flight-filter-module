package com.gridnine.testing.service;

import java.util.List;
import com.gridnine.testing.model.Flight;

public class ValidChronologyFilter implements FlightFilterService {

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream().filter(flight -> flight.getSegments().stream()
                .allMatch(segment -> segment.getDepartureDate().isBefore(segment.getArrivalDate()))).toList();
    }

}