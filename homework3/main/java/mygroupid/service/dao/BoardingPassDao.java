package mygroupid.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.AllArgsConstructor;
import mygroupid.domain.BoardingPass;
import mygroupid.service.db.SimpleJdbcTemplate;

@AllArgsConstructor
public final class BoardingPassDao {
    private final SimpleJdbcTemplate source;

    private BoardingPass createBoardingPass(ResultSet resultSet) throws SQLException {
        return new BoardingPass(
                resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getInt("boarding_no"),
                resultSet.getString("seat_no")
        );
    }

    public void saveBoardingPasses(Collection<BoardingPass> boardingPasses) throws SQLException {
        this.source.preparedStatement("insert into boarding_pass(ticket_no, " +
                "flight_id, boarding_no, seat_no) values (?, ?, ?, ?)", insertBoardingPass -> {
            for (BoardingPass boardingPass : boardingPasses) {
                insertBoardingPass.setString(1, boardingPass.getTicketNo());
                insertBoardingPass.setInt(2, boardingPass.getFlightId());
                insertBoardingPass.setInt(3, boardingPass.getBoardingNo());
                insertBoardingPass.setString(4, boardingPass.getSeatNo());
                insertBoardingPass.execute();
            }
        });
    }

    public Set<BoardingPass> getBoardingPasses() throws SQLException {
        return source.statement((stmt) -> {
            Set<BoardingPass> result = new HashSet();
            ResultSet resultSet = stmt.executeQuery("select * from boarding_pass");
            while(resultSet.next()) {
                result.add(this.createBoardingPass(resultSet));
            }
            return result;
        });
    }
}

