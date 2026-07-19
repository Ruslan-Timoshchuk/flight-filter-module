package com.gridnine.testing.service;

import java.util.List;
import com.gridnine.testing.model.Flight;

public interface FlightFilterService {

    List<Flight> apply(List<Flight> flights);
    
}