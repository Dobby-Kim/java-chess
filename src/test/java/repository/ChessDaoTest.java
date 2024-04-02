package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class ChessDaoTest {

    private final ChessDao chessDao = new ChessDao();

    @Test
    void connection() {
        try (final var connection = chessDao.getConnection()) {
            assertThat(connection).isNotNull();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
