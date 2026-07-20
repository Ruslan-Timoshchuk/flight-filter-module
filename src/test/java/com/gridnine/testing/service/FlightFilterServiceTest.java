package com.gridnine.testing.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.strategy.AcceptableGroundTimeStrategy;
import com.gridnine.testing.service.strategy.ExcludePastDepartureStrategy;
import com.gridnine.testing.service.strategy.ValidChronologyStrategy;

class FlightFilterServiceTest {

    @Test
    void givenFlightsWithPastDepartures_whenFilterByPastDepartureStrategy_thenExcludeFlightsBeforeDefinedDateTime() {
        var dateTime = LocalDateTime.of(2026, 7, 20, 12, 0);
        var flightFilterService = new FlightFilterService(new ExcludePastDepartureStrategy(dateTime));
        var validFlight = new Flight(List.of(new Segment(dateTime.plusHours(1), dateTime.plusHours(3)),
                new Segment(dateTime.plusHours(5), dateTime.plusHours(7))));
        var pastDepartureFlight1 = new Flight(List.of(new Segment(dateTime.minusHours(2), dateTime.plusHours(1)),
                new Segment(dateTime.plusHours(3), dateTime.plusHours(5))));
        var pastDepartureFlight2 = new Flight(List.of(new Segment(dateTime.minusHours(7), dateTime.minusHours(5))));
        assertEquals(List.of(validFlight),
                flightFilterService.filter(List.of(validFlight, pastDepartureFlight1, pastDepartureFlight2)));
    }

    @Test
    void givenFlightsWithInvalidChronology_whenFilterByValidChronologyStrategy_thenExcludeInvalidChronologyFlights() {
        var dateTime = LocalDateTime.of(2026, 7, 20, 12, 0);
        var flightFilterService = new FlightFilterService(new ValidChronologyStrategy());
        var validFlight = new Flight(List.of(new Segment(dateTime.plusHours(1), dateTime.plusHours(3))));
        var invalidChronologyFlight = new Flight(List.of(new Segment(dateTime.plusHours(2), dateTime.minusHours(1))));
        assertEquals(List.of(validFlight), flightFilterService.filter(List.of(validFlight, invalidChronologyFlight)));
    }

    @Test
    void givenFlightsWithLongGroundTime_whenFilterByAcceptableGroundTimeStrategy_thenExcludeLongGroundTimeFlights() {
        var dateTime = LocalDateTime.of(2026, 7, 20, 12, 0);
        var flightFilterService = new FlightFilterService(new AcceptableGroundTimeStrategy(120L));
        var validFlight = new Flight(List.of(new Segment(dateTime.plusHours(1), dateTime.plusHours(3)),
                new Segment(dateTime.plusHours(4), dateTime.plusHours(6))));
        var longGroundTimeFlight = new Flight(List.of(new Segment(dateTime.plusHours(2), dateTime.plusHours(4)),
                new Segment(dateTime.plusHours(8), dateTime.plusHours(10))));
        assertEquals(List.of(validFlight), flightFilterService.filter(List.of(validFlight, longGroundTimeFlight)));
    }

    @Test
    void givenMixedInvalidFlights_whenFilterByAllThreeStrategiesCombined_thenKeepOnlyValidFlights() {
        var dateTime = LocalDateTime.of(2026, 7, 20, 12, 0);
        var flightFilterService = new FlightFilterService(new ExcludePastDepartureStrategy(dateTime),
                new ValidChronologyStrategy(), new AcceptableGroundTimeStrategy(120L));
        var validFlight = new Flight(List.of(new Segment(dateTime.plusHours(1), dateTime.plusHours(3)),
                new Segment(dateTime.plusHours(4), dateTime.plusHours(6))));
        var pastDepartureFlight = new Flight(List.of(new Segment(dateTime.minusHours(2), dateTime.plusHours(1))));
        var invalidChronologyFlight = new Flight(List.of(new Segment(dateTime.plusHours(2), dateTime.minusHours(1))));
        var longGroundTimeFlight = new Flight(List.of(new Segment(dateTime.plusHours(2), dateTime.plusHours(4)),
                new Segment(dateTime.plusHours(8), dateTime.plusHours(10))));
        var allFlights = List.of(validFlight, pastDepartureFlight, invalidChronologyFlight, longGroundTimeFlight);
        assertEquals(List.of(validFlight), flightFilterService.filter(allFlights));
    }

}