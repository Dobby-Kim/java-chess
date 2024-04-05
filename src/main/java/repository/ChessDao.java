package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ChessDao {

    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "chess";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            throw new RuntimeException("DB 연결 오류: " + e.getMessage(), e);
        }
    }

    public boolean isInitialGame() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT fen_value FROM fen ORDER BY id LIMIT 1;");
            resultSet.next();
            return resultSet.getString("fen_value") == null;
        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패: " + e.getMessage(), e);
        }
    }

    public void updateFen(String fen) {
        final String query = "UPDATE fen SET fen_value = ? WHERE id = 1;";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
                query)) {
            preparedStatement.setString(1, fen);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException("FEN 정보 업데이트 실패: " + e.getMessage(), e);
        }
    }

    public String loadFenValue() {
        final String query = "SELECT fen_value FROM fen ORDER BY id DESC LIMIT 1;";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(
                query)) {
            resultSet.next();
            return resultSet.getString("fen_value");
        } catch (SQLException e) {
            throw new RuntimeException("FEN 정보 읽어오기 실패: " + e.getMessage(), e);
        }
    }

    public void initFen() {
        updateFen(null);
    }
}
