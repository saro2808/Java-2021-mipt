package mygroupid.service.dao;

import java.awt.print.Book;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.AllArgsConstructor;
import mygroupid.domain.Booking;
import mygroupid.service.db.SimpleJdbcTemplate;

@AllArgsConstructor
public final class BookingDao {
    private final SimpleJdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getString("book_ref"),
                resultSet.getObject("book_date", ZonedDateTime.class),
                resultSet.getInt("total_amount")
        );
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException {
        this.source.preparedStatement("insert into booking(book_red, " +
                "book_date, total_amount) values (?, ?, ?)", (insertBooking) -> {
            for (Booking booking : bookings) {
                insertBooking.setString(1, booking.getBookRef());
                insertBooking.setObject(2, booking.getBookDate());
                insertBooking.setInt(3, booking.getTotalAmount());
                insertBooking.execute();
            }
        });
    }

    public Set<Booking> getBookings() throws SQLException {
        return source.statement((stmt) -> {
            Set<Booking> result = new HashSet();
            ResultSet resultSet = stmt.executeQuery("select * from booking");

            while(resultSet.next()) {
                result.add(this.createBooking(resultSet));
            }

            return result;
        });
    }
}

