package com.gridnine.testing.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

/**
 * Factory class to get sample list of flights.
 */
public class FlightBuilder {
    
    public List<Flight> createAllFlights(List<List<Flight>> flightGroups) {
        return flightGroups.stream().flatMap(List::stream).toList();
    }

    public List<Flight> createNormalFlightsBeforeAllFilters() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // A normal flight with two hours duration
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                // A normal multi segment flight
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(3),
                        threeDaysFromNow.plusHours(5)));
    }

    public List<Flight> createInvalidFlightsBeforeAllFilters() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // A flight departing in the past
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                // A flight that departs before it arrives
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                // A flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(5),
                        threeDaysFromNow.plusHours(6)),
                // Another flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(3),
                        threeDaysFromNow.plusHours(4), threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
    }

    public List<Flight> createInvalidFlightsBeforePastDepartureFilter() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // A flight departing in the past
                createFlight(threeDaysFromNow.minusDays(8), threeDaysFromNow.minusDays(2)),
                // Another flight flight departing in the past
                createFlight(threeDaysFromNow.minusDays(10), threeDaysFromNow.minusDays(10).plusHours(2),
                        threeDaysFromNow.minusDays(10).plusHours(3), threeDaysFromNow.minusDays(10).plusHours(5)));
    }

    public List<Flight> createInvalidFlightsBeforeValidChronologyFilter() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // A flight that departs before it arrives
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                // Another flight with segment that departs before it arrives
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(3),
                        threeDaysFromNow.plusHours(1)));
    }
    
    public List<Flight> createInvalidFlightsBeforeGroundTimeFilter() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                // A flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(5),
                        threeDaysFromNow.plusHours(7)),
                // Another flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3).plusMinutes(30), threeDaysFromNow.plusHours(5),
                        threeDaysFromNow.plusHours(6).plusMinutes(30), threeDaysFromNow.plusHours(8)));
    }
    
    public Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
    
}