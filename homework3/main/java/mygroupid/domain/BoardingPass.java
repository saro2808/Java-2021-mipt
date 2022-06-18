package mygroupid.domain;

import lombok.Data;

@Data
public class BoardingPass {
    private final String ticketNo;
    private final int flightId;
    private final int boardingNo;
    private final String seatNo;
}

