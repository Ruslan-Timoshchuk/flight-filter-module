package com.gridnine.testing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.strategy.FlightFilterStrategy;

public class FlightFilterService {

    private final List<FlightFilterStrategy> strategies;

    public FlightFilterService(FlightFilterStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    public List<Flight> filter(List<Flight> flights) {
        List<Flight> result = new ArrayList<>(flights);
        for (FlightFilterStrategy strategy : strategies) {
            result = strategy.apply(result);
        }
        return result;
    }

}