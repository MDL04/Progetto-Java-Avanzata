package dao;

import model.GameSession;
import utils.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe DAO che gestisce le transazioni con il database wordageddon.db per quanto riguarda le sessioni di gioco.
 * Fornisce metodi per inserire, selezionare e cancellare sessioni di gioco.
 */
public class GameSessionDAO implements DAO<GameSession>{

    /**
     * Seleziona una sessione di gioco in base all'id.
     * Cattura un SQLException in caso di errore nella selezione.
     *
     * @param id l'id della sessione di gioco da selezionare
     * @return un Optional contenente la sessione di gioco trovata, o vuoto se non trovata
     */
    public Optional<GameSession> selectById(long id) {
        Optional<GameSession> result = Optional.empty();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game_sessions WHERE id = ?")) {
            preparedStatement.setLong(1, id);
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

    /**
     * Seleziona le sessioni di gioco in base all'utente, quindi in base al suo id.
     * Cattura un SQLException in caso si di errore nella selezione.
     *
     * @param userId l'id dell'utente associato a quella sessione di gioco
     * @return una lista di sessioni di gioco associate all'utente
     */
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

    /**
     * Seleziona tutte le sessioni di gioco.
     * Cattura un SQLException in caso si di errore nella selezione.
     *
     * @return una lista di tutte le sessioni di gioco
     */
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

    /**
     * Inserisce una nuova sessione di gioco nel database.
     * Cattura un SQLException in caso si di errore durante l'inserimento.
     *
     * @param userId l'id dell'utente associato alla sessione di gioco
     * @param score il punteggio ottenuto nella sessione di gioco
     * @param difficulty la difficoltà della sessione di gioco
     * @param date la data in cui è finita la sessione di gioco
     */
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

    /**
     * Elimina una sessione di gioco in base all'id.
     * Cattura un SQLException in caso si di errore durante l'eliminazione.
     *
     * @param id l'id della sessione di gioco da eliminare
     */
    public void deleteById(int id) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM game_sessions WHERE id = ?")) {
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
