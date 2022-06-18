package mygroupid.domain;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class Flight {
    private final int flightId;
    private final String flightNo;
    private final ZonedDateTime scheduledDeparture;
    private final ZonedDateTime scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final ZonedDateTime actualDeparture;
    private final ZonedDateTime actualArrival;
    private final Set<Ticket> tickets;
}

