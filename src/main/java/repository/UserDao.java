package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class UserDao {

    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "chess";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void initializeDatabase() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS chess;");
            statement.execute("USE chess;");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS fen (id INT AUTO_INCREMENT PRIMARY KEY, fen_value VARCHAR(255) NOT NULL);");

            try (ResultSet resultSet = statement.executeQuery("SELECT 1 FROM fen LIMIT 1;")) {
                if (!resultSet.next()) {
                    statement.executeUpdate("INSERT INTO fen (fen_value) VALUES ('initial_fen_value');");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing the database: " + e.getMessage(), e);
        }
    }

    public boolean isTableExists() {
        final String CHECK_TABLE_EXIST_QUERY =
                "SELECT * FROM information_schema.tables WHERE table_schema = '" + DATABASE
                        + "' AND table_name = 'fen' LIMIT 1;";
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(
                CHECK_TABLE_EXIST_QUERY)) {
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
            return false;
        }
    }

    public void updateFen(final String fen) {
        final var query = "UPDATE fen SET fen_value = ? WHERE id = ?;";
        final var connection = getConnection();
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fen);
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadFenValues() {
        final String LOAD_FEN_QUERY = "SELECT fen_value FROM fen ORDER BY id DESC LIMIT 1;";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(
                LOAD_FEN_QUERY)) {
            if (resultSet.next()) {
                return resultSet.getString("fen_value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
