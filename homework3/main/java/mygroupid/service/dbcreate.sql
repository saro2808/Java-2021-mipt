--drop all objects;
create table aircraft (
                          aircraft_code char(3) not null primary key,
                          model varchar(255) not null, --jsonb
                          range integer not null
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/aircrafts_data.csv',
    'aircraft_code, model, range');

create table airport (
                         airport_code char(3) not null primary key,
                         airport_name varchar(255) not null,  --jsonb
                         city varchar(255) not null,          --jsonb
                         coordinates varchar(255) not null,   --point
                         timezone text not null
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/airports_data.csv',
    'airport_code, airport_name, city, coordinates, timezone');

create table flight (
                        flight_id integer not null, -- auto_increment
                        flight_no char(6) not null,
                        scheduled_departure timestamp not null,
                        scheduled_arrival timestamp not null,
                        departure_airport char(3) not null,
                        arrival_airport char(3) not null,
                        status varchar(20) not null,
                        aircraft_code char(3) not null,
                        actual_departure timestamp,
                        actual_arrival timestamp,
                        primary key (flight_no, scheduled_departure)
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/flights.csv',
    'flight_id,flight_no,scheduled_departure, scheduled_arrival, departure_airport,
--                    arrival_airport, status, aircraft_code, actual_departure, actual_arrival');

create table ticket (
                        ticket_no char(13) not null primary key,
                        book_ref char(6) not null,
                        passenger_id varchar(20) not null,
                        passenger_name text not null,
                        contact_data varchar(255) --jsonb
)as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/tickets.csv',
    'ticket_no, book_ref, passenger_id, passenger_name, contact_data');

create table boarding_pass (
                               ticket_no char(13) not null,
                               flight_id integer not null,
                               boarding_no integer not null,
                               seat_no varchar(4) not null,
                               primary key (ticket_no, flight_id)
    ,foreign key (ticket_no) references ticket(ticket_no)
    ,foreign key (flight_id) references flight(flight_id)
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/boarding_passes.csv',
    'ticket_no, flight_id, boarding_no, seat_no');

create table booking (
                         book_ref char(6) not null primary key,
                         book_date timestamp not null,
                         total_amount numeric(10, 2) not null
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/bookings.csv',
    'book_ref, book_date, total_amount');

create table seat (
                      aircraft_code char(3) not null,
                      seat_no varchar(4) not null,
                      fare_conditions varchar(10) not null,
                      primary key (aircraft_code, seat_no)
    ,foreign key (aircraft_code) references aircraft(aircraft_code)
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/seats.csv',
    'aircraft_code, seat_no, fare_conditions');

create table ticket_flight (
                               ticket_no char(13) not null,
                               flight_id integer not null,
                               fare_conditions varchar(10) not null,
                               amount numeric(10, 2) not null,
                               primary key (ticket_no, flight_id),
                               foreign key (ticket_no) references ticket(ticket_no),
                               foreign key (flight_id) references flight(flight_id)
) as select * from csvread('/home/saro/homework3/src/main/resources/mygroupid/data/ticket_flights.csv',
    'ticket_no, flight_id, fare_conditions, amount');

-- select model, aircraft.aircraft_code, flight_id, status
-- from flight inner join aircraft on flight.aircraft_code = aircraft.aircraft_code
-- where aircraft.aircraft_code = '319';
--
-- update flight set status = 'Scheduled' where aircraft_code = '319';
--
-- select distinct city, count(*) cancelled_flights from airport inner join flight
--                                                                          on airport_code = departure_airport where status = 'Cancelled'
-- group by city having cancelled_flights >= 5 order by cancelled_flights desc;
--
-- delete from ticket where ticket_no in (
--     select ticket_no from ticket_flight inner join flight on flight.flight_id = ticket_flight.flight_id
--     where flight.flight_id in (
--         select flight_id from flight inner join aircraft on flight.aircraft_code = aircraft.aircraft_code
--         where aircraft.aircraft_code = '319'
--     )
-- );

