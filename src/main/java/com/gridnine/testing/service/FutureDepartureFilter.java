package com.gridnine.testing.service;

import static java.time.LocalDateTime.now;
import java.util.List;
import com.gridnine.testing.model.Flight;

public class FutureDepartureFilter implements FlightFilterService {

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream().filter(flight -> flight.getSegments().stream()
                .allMatch(segment -> segment.getDepartureDate().isAfter(now()))).toList();
    }

}