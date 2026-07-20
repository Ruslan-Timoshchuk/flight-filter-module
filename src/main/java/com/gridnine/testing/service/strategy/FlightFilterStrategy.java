package com.gridnine.testing.service.strategy;

import java.util.List;
import com.gridnine.testing.model.Flight;

public interface FlightFilterStrategy {
    
    List<Flight> apply(List<Flight> flights); 
    
}