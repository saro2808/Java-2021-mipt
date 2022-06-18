package mygroupid;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import mygroupid.domain.Aircraft;
import mygroupid.service.dao.AircraftDao;
import mygroupid.service.dao.AirportDao;
import mygroupid.service.dao.BoardingPassDao;
import mygroupid.service.dao.BookingDao;
import mygroupid.service.dao.SeatDao;
import mygroupid.service.dao.TicketDao;
import mygroupid.service.db.DbInit;
import mygroupid.service.db.SimpleJdbcTemplate;
import org.h2.jdbcx.JdbcConnectionPool;

public final class Main {
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(JdbcConnectionPool.create(
            "jdbc:h2:file:/home/saro/homework3/src/main/resources/mygroupid/dbcreate.sql;DB_CLOSE_DElAY=-1",
            "saro", ""));
    private static final AircraftDao aircraftDao = new AircraftDao(source);
    private static final AirportDao airportDao = new AirportDao(source);
    private static final BoardingPassDao boardingPassDao = new BoardingPassDao(source);
    private static final BookingDao bookingDao = new BookingDao(source);
    private static final SeatDao seatDao = new SeatDao(source);
    private static final TicketDao ticketDao = new TicketDao(source);

    private Main() {
    }

    static void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
    }

    static void tearDownDB() throws SQLException, IOException {
        source.statement((stmt) -> {
            stmt.execute("drop all objects;");
        });
    }

    public static void taskBPointOne() throws SQLException {
        source.statement((stmt) -> {
            ResultSet Jsonbs = stmt.executeQuery("select distinct city from airport;");
            Map<String, String> JsonbCity = new HashMap<>();

            while (Jsonbs.next()) {
                String Jsonb = Jsonbs.getString(1);
                String city = Jsonb.split("\"")[3];
                JsonbCity.put(Jsonb, city);
            }

            ResultSet resultSet = stmt.executeQuery(
                    "select city, airport_code from airport where city in\n" +
                            "(select city from (select city, count(airport_code) airport_count" +
                            "\nfrom airport group by city) where airport_count > 1);");
            Map<String, Set<String>> cityAirports = new HashMap<>();

            while (resultSet.next()) {
                String currentKey = JsonbCity.get(resultSet.getString(1));
                Set<String> currentValue = cityAirports.get(currentKey);
                if (currentValue == null) {
                    currentValue = new HashSet();
                }
                currentValue.add(resultSet.getString(2));
                cityAirports.put(currentKey, currentValue);
            }

            System.out.println("city        Airports");
            for (String city : cityAirports.keySet()) {
                System.out.println(city + " " + cityAirports.get(city));
            }
        });
    }

    public static void taskBPointTwo() throws SQLException {
        source.statement((stmt) -> {
            ResultSet resultSet = stmt.executeQuery(
                    "select distinct city, count(*) cancelled_flights from airport inner join flight\n" +
                            "    on airport_code = departure_airport where status = 'Cancelled'\n" +
                            "group by city having cancelled_flights >= 5 order by cancelled_flights desc;");
            System.out.println("city cancelled_flights");

            while(resultSet.next()) {
                System.out.println(
                        resultSet.getString(1).split("\"")[3] +
                                " " + resultSet.getInt(2));
            }
        });
    }

    public static void taskBPointThree() throws SQLException {
        source.statement((stmt) -> {
            ResultSet resultSet = stmt.executeQuery("select * from flight");
        });
    }

    public static void taskBPointFour() throws SQLException {
        source.statement((stmt) -> {
            ResultSet resultSet = stmt.executeQuery(
                    "select extract(month from scheduled_departure) month, count(*) from flight\n" +
                    "where status = 'Cancelled' group by month;");
            System.out.println("month cancelled_flights");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "     " + resultSet.getInt(2));
            }
        });
    }

    public static void taskBPointFive() throws SQLException {
        source.statement((stmt) -> {
            ResultSet resultSet = stmt.executeQuery(
                    "select departures.weekday, departure_count + arrival_count flight_count from\n" +
                            "(select day_of_week(scheduled_departure) weekday, count(*) departure_count\n" +
                            "from flight inner join airport on departure_airport = airport_code\n" +
                            "where city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}' group by weekday) departures\n" +
                            "inner join\n(select day_of_week(scheduled_arrival) weekday, count(*) arrival_count\n" +
                            "from flight inner join airport on arrival_airport = airport_code\n" +
                            "where city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}' group by weekday) arrivals\n" +
                            "on departures.weekday = arrivals.weekday;");
            System.out.println("weekday flights_count");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "       " + resultSet.getInt(2));
            }
        });
    }

    public static void taskBPointSix(String model) throws SQLException {
        source.statement((stmt) -> {
            Set<Aircraft> aircrafts = aircraftDao.getAircrafts();
            Map<String, String> modelCode = new HashMap<>();
            for (Aircraft aircraft : aircrafts) {
                modelCode.put(aircraft.getModel(), aircraft.getAircraftCode());
            }

            String code = modelCode.get(model);
            stmt.execute("update flight set status = 'Cancelled' where aircraft_code = '" + code + "';");
            stmt.execute("delete from ticket where ticket_no in (\n" +
                    "    select ticket_no from ticket_flight" +
                    "            inner join flight on flight.flight_id = ticket_flight.flight_id\n" +
                    "           where flight.flight_id in (\n" +
                    "                 select flight_id from flight" +
                    "                  inner join aircraft" +
                    "                  on flight.aircraft_code = aircraft.aircraft_code\n" +
                    "                 where aircraft.aircraft_code = '" + code + "'\n" +
                    "            )\n    );");
        });
    }

    public static void taskBPointSeven(String startTime, String endTime) throws SQLException {
        source.statement((stmt) -> {
            stmt.execute("update flight set status = 'Cancelled' where flight_id in (\n" +
                    "    select flight_id from flight inner join airport on departure_airport = airport_code and\n" +
                    "                                   timestamp'" + startTime + "' <= scheduled_departure and\n" +
                    "                                   scheduled_departure <= timestamp'" + endTime + "' and\n" +
                    "                                   city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}'\n" +
                    "union\n" +
                    "select flight_id\n" +
                    "from flight inner join airport on arrival_airport = airport_code and\n" +
                    "                                   timestamp'" + startTime + "' <= scheduled_arrival and\n" +
                    "                                   scheduled_arrival <= timestamp'" + endTime + "' and\n" +
                    "                                   city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}'\n" +
                    "    );\n");
            ResultSet resultSet = stmt.executeQuery(
                    "select day, round(sum(amount), 2) loss\n" +
                            "from (\n" +
                            "         select flight_id, extract(day from scheduled_departure) day\n" +
                            "         from flight\n" +
                            "                  inner join airport on departure_airport = airport_code and\n" +
                            "                        timestamp'" + startTime + "' <= scheduled_departure and\n" +
                            "                        scheduled_departure <= timestamp'" + endTime + "' and" +
                            "                        city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}'\n" +
                            "         union\n" +
                            "         select flight_id, extract(day from scheduled_arrival) day\n" +
                            "         from flight\n" +
                            "                  inner join airport on arrival_airport = airport_code and\n" +
                            "                        timestamp'" + startTime + "' <= scheduled_arrival and\n" +
                            "                        scheduled_arrival <= timestamp'" + endTime + "' and" +
                            "                        city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}'\n" +
                            "     ) subquery inner join ticket_flight " +
                            "on subquery.flight_id = ticket_flight.flight_id\n" +
                            "group by day;");
            System.out.println("Day Loss");

            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "   " + resultSet.getFloat(2));
            }
        });
    }

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("started");
        setupDB();
        System.out.println("DB is set up");
        taskBPointSix("319");
    }
}

