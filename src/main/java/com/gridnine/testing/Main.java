package com.gridnine.testing;

import com.gridnine.testing.service.AcceptableGroundTimeFilter;
import com.gridnine.testing.service.FlightBuilder;
import com.gridnine.testing.service.FlightFilterService;
import com.gridnine.testing.service.FutureDepartureFilter;
import com.gridnine.testing.service.ValidChronologyFilter;

public class Main {

    public static void main(String[] args) {
        var flights = FlightBuilder.createFlights();

        System.out.println("Excluded all flights before now");
        FlightFilterService futureDepartureFilter = new FutureDepartureFilter();
        var filteredFlights = futureDepartureFilter.apply(flights);
        filteredFlights.forEach(System.out::println);

        System.out.println("Excluded all flights with invalid chronology");
        FlightFilterService validChronologyFilter = new ValidChronologyFilter();
        var filteredFlights2 = validChronologyFilter.apply(flights);
        filteredFlights2.forEach(System.out::println);
        
        System.out.println("Excluded flights with a total ground time exceeding two hours");
        FlightFilterService acceptableGroundTimeFilter = new AcceptableGroundTimeFilter(120);
        var filteredFlights3 = acceptableGroundTimeFilter.apply(flights);
        filteredFlights3.forEach(System.out::println);
        
    }

}