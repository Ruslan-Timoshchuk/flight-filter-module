package com.gridnine.testing.service;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

public class AcceptableGroundTimeFilter implements FlightFilterService {

    private final long acceptableGroundTime;

    public AcceptableGroundTimeFilter(long acceptableGroundTime) {
        this.acceptableGroundTime = acceptableGroundTime;
    }

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream().filter(flight -> countDuration(flight.getSegments()) <= acceptableGroundTime).toList();
    }

    private long countDuration(List<Segment> segments) {
        return IntStream.range(0, segments.size() - 1)
                .mapToLong(i -> Duration
                        .between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate()).toMinutes())
                .sum();
    }

}