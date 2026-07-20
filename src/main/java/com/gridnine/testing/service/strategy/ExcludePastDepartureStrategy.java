package com.gridnine.testing.service.strategy;

import java.time.LocalDateTime;
import java.util.List;
import com.gridnine.testing.model.Flight;

public class ExcludePastDepartureStrategy implements FlightFilterStrategy {

    private final LocalDateTime date;

    public ExcludePastDepartureStrategy(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .allMatch(segment -> segment.getDepartureDate().isAfter(date)))
                .toList();
    }

}