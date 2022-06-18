package mygroupid.domain;

import lombok.Data;

@Data
public class Seat {
    private final String aircraftCode;
    private final String seatNo;
    private final String fareConditions;
}

