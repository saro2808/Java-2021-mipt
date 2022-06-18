package mygroupid.domain;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Booking {
    private final String bookRef;
    private final ZonedDateTime bookDate;
    private final int totalAmount;
}

