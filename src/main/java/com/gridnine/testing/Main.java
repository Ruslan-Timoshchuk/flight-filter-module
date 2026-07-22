package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import com.gridnine.testing.service.FlightBuilder;
import com.gridnine.testing.service.FlightFilterService;
import com.gridnine.testing.service.strategy.AcceptableGroundTimeStrategy;
import com.gridnine.testing.service.strategy.ExcludePastDepartureStrategy;
import com.gridnine.testing.service.strategy.ValidChronologyStrategy;

public class Main {

    public static void main(String[] args) {
        FlightBuilder flightBuilder = new FlightBuilder();
        var normalFlights = flightBuilder.createNormalFlightsBeforeAllFilters();
        var invalidFlights = flightBuilder.createInvalidFlightsBeforeAllFilters();
        var allFlights = flightBuilder.createAllFlights(List.of(normalFlights, invalidFlights));

        var now = LocalDateTime.now();
        System.out.println("1: List without flights with departing in the past:");
        var futureDepartureFilter = new FlightFilterService(new ExcludePastDepartureStrategy(now));
        var flightsWithDepartureInFuture = futureDepartureFilter.filter(allFlights);
        flightsWithDepartureInFuture.forEach(System.out::println);

        System.out.println("2: List without flights with invalid chronology:");
        var validChronologyFilter = new FlightFilterService(new ValidChronologyStrategy());
        var flightsWithValidChronology = validChronologyFilter.filter(allFlights);
        flightsWithValidChronology.forEach(System.out::println);
        
        System.out.println("3: List without flights with total ground time exceeding two hours:");
        var limitGroundTime = 120L;
        var acceptableGroundTimeFilter = new FlightFilterService(new AcceptableGroundTimeStrategy(limitGroundTime));
        var flightsWithLimitedGroundTime = acceptableGroundTimeFilter.filter(allFlights);
        flightsWithLimitedGroundTime.forEach(System.out::println);
        
        System.out.println("4: List filtered by all rules combined:");
        var comprehensiveFilter = new FlightFilterService(
                new ExcludePastDepartureStrategy(now),
                new ValidChronologyStrategy(),
                new AcceptableGroundTimeStrategy(limitGroundTime));
        var fullyFilteredFlights = comprehensiveFilter.filter(allFlights);
        fullyFilteredFlights.forEach(System.out::println);
    }

}