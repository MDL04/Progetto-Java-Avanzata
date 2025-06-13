package dao;

import model.User;
import utils.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public Optional<User> selectById(long id) {
        Optional<User> result = Optional.empty();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password_hash"));
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setBestScoreEasy(rs.getInt("best_easy"));
                user.setBestScoreHard(rs.getInt("best_hard"));
                user.setBestScoreMedium(rs.getInt("best_medium"));
                user.setScoreEasy(rs.getInt("score_total_easy"));
                user.setScoreHard(rs.getInt("score_total_hard"));
                user.setScoreMedium(rs.getInt("score_total_medium"));
                user.setPartiteEasy(rs.getInt("games_easy"));
                user.setPartiteHard(rs.getInt("games_hard"));
                user.setPartiteMedium(rs.getInt("games_medium"));
                user.setUrlAvatar(rs.getString("avatar_url"));
                result = Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Optional<User> selectByUsername(String username) {
        Optional<User> result = Optional.empty();
        try (Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")){
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setBestScoreEasy(rs.getInt("best_easy"));
                    user.setBestScoreMedium(rs.getInt("best_medium"));
                    user.setBestScoreHard(rs.getInt("best_hard"));
                    user.setScoreEasy(rs.getInt("score_total_easy"));
                    user.setScoreMedium(rs.getInt("score_total_medium"));
                    user.setScoreHard(rs.getInt("score_total_hard"));
                    user.setPartiteEasy(rs.getInt("games_easy"));
                    user.setPartiteMedium(rs.getInt("games_medium"));
                    user.setPartiteHard(rs.getInt("games_hard"));
                    user.setUrlAvatar(rs.getString("avatar_url"));
                    return Optional.ofNullable(user);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Optional<User> selectByEmail(String email) {
        Optional<User> result = Optional.empty();
        try (Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setBestScoreEasy(rs.getInt("best_easy"));
                    user.setBestScoreMedium(rs.getInt("best_medium"));
                    user.setBestScoreHard(rs.getInt("best_hard"));
                    user.setScoreEasy(rs.getInt("score_total_easy"));
                    user.setScoreMedium(rs.getInt("score_total_medium"));
                    user.setScoreHard(rs.getInt("score_total_hard"));
                    user.setPartiteEasy(rs.getInt("games_easy"));
                    user.setPartiteMedium(rs.getInt("games_medium"));
                    user.setPartiteHard(rs.getInt("games_hard"));
                    user.setUrlAvatar(rs.getString("avatar_url"));
                    return Optional.ofNullable(user);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS WHERE is_admin = 0");
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password_hash"), rs.getBoolean("is_admin"), rs.getInt("best_easy"), rs.getInt("best_medium"), rs.getInt("best_hard"), rs.getInt("games_easy"), rs.getInt("games_medium"), rs.getInt("games_hard"), rs.getInt("score_total_easy"), rs.getInt("score_total_medium"), rs.getInt("score_total_hard"), rs.getString("avatar_url"));
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public boolean insert(User user) {
        try (Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, email, password_hash, is_admin, avatar_url) values (?,?,?,?,?)")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.isAdmin());
            preparedStatement.setString(5, user.getUrlAvatar());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkLogin(String username, String password) {
        try (Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password_hash = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void update(User user, String difficulty, int score) {
        try (Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users " + "SET " + "games_" + difficulty + " = games_" + difficulty + " + 1, " + "score_total_" + difficulty + " = score_total_" + difficulty + " + ?, " + "best_" + difficulty + " = CASE WHEN best_" + difficulty + " > ? THEN best_" + difficulty + " ELSE ? END " +
                "WHERE id = ?")){
            preparedStatement.setInt(1, score);
            preparedStatement.setInt(2, score);
            preparedStatement.setInt(3, score);
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean updateUserProfile(User user) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET username = ?, email = ?, avatar_url = ? WHERE id = ?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUrlAvatar());
            preparedStatement.setLong(4, user.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(long userId, String newPasswordHash) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET password_hash = ? WHERE id = ?")) {
            preparedStatement.setString(1, newPasswordHash);
            preparedStatement.setLong(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public void delete(User user){
            try(Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
                preparedStatement.setString(1, user.getUsername());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Nessun utente eliminato.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
