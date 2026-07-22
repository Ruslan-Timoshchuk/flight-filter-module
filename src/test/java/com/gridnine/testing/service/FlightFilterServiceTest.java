package com.gridnine.testing.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.gridnine.testing.service.strategy.AcceptableGroundTimeStrategy;
import com.gridnine.testing.service.strategy.ExcludePastDepartureStrategy;
import com.gridnine.testing.service.strategy.ValidChronologyStrategy;

class FlightFilterServiceTest {

    private final FlightBuilder flightBuilder = new FlightBuilder();
    
    @Test
    void givenFlightsWithPastDepartures_whenFilterByPastDepartureStrategy_thenExcludeFlightsBeforeDefinedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        var flightFilterService = new FlightFilterService(new ExcludePastDepartureStrategy(now));
        var normalFlights = flightBuilder.createNormalFlightsBeforeAllFilters();
        var invalidFlights = flightBuilder.createInvalidFlightsBeforePastDepartureFilter();
        var allFlights = flightBuilder.createAllFlights(List.of(normalFlights, invalidFlights));
        assertEquals(normalFlights, flightFilterService.filter(allFlights));
    }

    @Test
    void givenFlightsWithInvalidChronology_whenFilterByValidChronologyStrategy_thenExcludeInvalidChronologyFlights() {
        var flightFilterService = new FlightFilterService(new ValidChronologyStrategy());
        var normalFlights = flightBuilder.createNormalFlightsBeforeAllFilters();
        var ivalidFlights = flightBuilder.createInvalidFlightsBeforeValidChronologyFilter();
        var allFlights = flightBuilder.createAllFlights(List.of(normalFlights, ivalidFlights));
        assertEquals(normalFlights, flightFilterService.filter(allFlights));
    }

    @Test
    void givenFlightsWithLongGroundTime_whenFilterByAcceptableGroundTimeStrategy_thenExcludeLongGroundTimeFlights() {
        long groundTimeLimit = 120L;
        var flightFilterService = new FlightFilterService(new AcceptableGroundTimeStrategy(groundTimeLimit));
        var normalFlights = flightBuilder.createNormalFlightsBeforeAllFilters();
        var ivalidFlights = flightBuilder.createInvalidFlightsBeforeGroundTimeFilter();
        var allFlights = flightBuilder.createAllFlights(List.of(normalFlights, ivalidFlights));
        assertEquals(normalFlights, flightFilterService.filter(allFlights));
    }

    @Test
    void givenMixedInvalidFlights_whenFilterByAllThreeStrategies_thenKeepOnlyValidFlights() {
        LocalDateTime now = LocalDateTime.now();
        long groundTimeLimit = 120L;
        var flightFilterService = new FlightFilterService(
                new ExcludePastDepartureStrategy(now),
                new ValidChronologyStrategy(), 
                new AcceptableGroundTimeStrategy(groundTimeLimit));
        var normalFlights = flightBuilder.createNormalFlightsBeforeAllFilters();
        var ivalidFlights = flightBuilder.createInvalidFlightsBeforeAllFilters();
        var allFlights = flightBuilder.createAllFlights(List.of(normalFlights, ivalidFlights));
        assertEquals(normalFlights, flightFilterService.filter(allFlights));
    }

}