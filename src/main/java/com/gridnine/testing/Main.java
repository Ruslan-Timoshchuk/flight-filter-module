package com.gridnine.testing;

import java.time.LocalDateTime;
import com.gridnine.testing.service.FlightBuilder;
import com.gridnine.testing.service.FlightFilterService;
import com.gridnine.testing.service.strategy.AcceptableGroundTimeStrategy;
import com.gridnine.testing.service.strategy.ExcludePastDepartureStrategy;
import com.gridnine.testing.service.strategy.ValidChronologyStrategy;

public class Main {

    public static void main(String[] args) {
        var flights = FlightBuilder.createFlights();

        var now = LocalDateTime.now();
        System.out.println("Excluded flights with departure in the past");
        var futureDepartureFilter = new FlightFilterService(new ExcludePastDepartureStrategy(now));
        var flightsWithDepartureInFuture = futureDepartureFilter.filter(flights);
        flightsWithDepartureInFuture.forEach(System.out::println);

        System.out.println("Excluded all flights with invalid chronology");
        var validChronologyFilter = new FlightFilterService(new ValidChronologyStrategy());
        var flightsWithValidChronology = validChronologyFilter.filter(flights);
        flightsWithValidChronology.forEach(System.out::println);
        
        System.out.println("Excluded flights with a total ground time exceeding two hours");
        var limitGroundTime = 120L;
        var acceptableGroundTimeFilter = new FlightFilterService(new AcceptableGroundTimeStrategy(limitGroundTime));
        var flightsWithLimitedGroundTime = acceptableGroundTimeFilter.filter(flights);
        flightsWithLimitedGroundTime.forEach(System.out::println);
        
        System.out.println("Excluded flights by some rules");
        var comprehensiveFilter = new FlightFilterService(
                new ExcludePastDepartureStrategy(now),
                new ValidChronologyStrategy(),
                new AcceptableGroundTimeStrategy(limitGroundTime));
        var fullyFilteredFlights = comprehensiveFilter.filter(flights);
        fullyFilteredFlights.forEach(System.out::println);
    }

}