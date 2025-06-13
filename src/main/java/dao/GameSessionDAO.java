package dao;

import model.GameSession;
import utils.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameSessionDAO {

    public Optional<GameSession> selectById(int id) {
        Optional<GameSession> result = Optional.empty();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game_sessions WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                GameSession gameSession = null;
                gameSession.setId(rs.getInt("id"));
                gameSession.setUserId(rs.getInt("user_id"));
                gameSession.setDate(rs.getTimestamp("date").toLocalDateTime());
                gameSession.setScore(rs.getInt("score"));
                gameSession.setDifficulty(rs.getString("difficulty"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<GameSession> selectByUserId(int userId){
        List<GameSession> sessions = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game_sessions WHERE user_id = ? ORDER BY date DESC")) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                GameSession gameSession = null;
                gameSession.setId(rs.getInt("id"));
                gameSession.setUserId(rs.getInt("user_id"));
                gameSession.setDate(rs.getTimestamp("date").toLocalDateTime());
                gameSession.setScore(rs.getInt("score"));
                gameSession.setDifficulty(rs.getString("difficulty"));
                sessions.add(gameSession);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public List<GameSession> selectAll() {
        List<GameSession> sessions = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game_sessions ORDER BY date DESC");
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                GameSession gameSession = null;
                gameSession.setId(rs.getInt("id"));
                gameSession.setUserId(rs.getInt("user_id"));
                gameSession.setDate(rs.getTimestamp("date").toLocalDateTime());
                gameSession.setScore(rs.getInt("score"));
                gameSession.setDifficulty(rs.getString("difficulty"));
                sessions.add(gameSession);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public void insert(int userId, int score, String difficulty, LocalDateTime date) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO game_sessions (user_id, score, difficulty, date) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, score);
            preparedStatement.setString(3, difficulty);
            preparedStatement.setString(4, date.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM game_sessions WHERE id = ?")) {
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
