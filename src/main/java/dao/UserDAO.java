package dao;

import actors.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

        public Optional<User> selectById(long id) {
            Optional<User> result = Optional.empty();
            try(Connection connection = DBManager.getConnection();
            Statement sta = connection.createStatement();
                ResultSet rs = sta.executeQuery("SELECT * FROM users WHERE id = " + id)){
                User user = null;
                if (rs.next()) {
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setBestScoreEasy(rs.getInt("best_easy"));
                    user.setBestScoreHard(rs.getInt("best_hard"));
                    user.setBestScoreNormal(rs.getInt("best_medium"));
                    user.setScoreEasy(rs.getInt("score_total_easy"));
                    user.setScoreHard(rs.getInt("score_total_hard"));
                    user.setScoreNormal(rs.getInt("score_total_medium"));
                    user.setPartiteEasy(rs.getInt("games_easy"));
                    user.setPartiteHard(rs.getInt("games_hard"));
                    user.setPartiteNormal(rs.getInt("games_medium"));
                    user.setUrlAvatar(rs.getString("avatar_url"));
                }
                result = Optional.ofNullable(user);
            }catch (SQLException e){
                e.printStackTrace();
            }
            return result;
        }

        public Optional<User> selectByUsername(String username) {
            Optional<User> result = Optional.empty();
            try (Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = " + username);
                ResultSet rs = preparedStatement.executeQuery()){
                User user = null;
                if (rs.next()) {
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setBestScoreEasy(rs.getInt("best_easy"));
                    user.setBestScoreNormal(rs.getInt("best_medium"));
                    user.setBestScoreHard(rs.getInt("best_hard"));
                    user.setScoreEasy(rs.getInt("score_total_easy"));
                    user.setScoreNormal(rs.getInt("score_total_medium"));
                    user.setScoreHard(rs.getInt("score_total_hard"));
                    user.setPartiteEasy(rs.getInt("games_easy"));
                    user.setPartiteNormal(rs.getInt("games_medium"));
                    user.setPartiteHard(rs.getInt("games_hard"));
                    user.setUrlAvatar(rs.getString("avatar_url"));
                }
                return result;
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
                    User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("email"), rs.getString("password_hash"), rs.getBoolean("is_admin"), rs.getInt("best_easy"), rs.getInt("best_medium"), rs.getInt("best_hard"), rs.getInt("games_easy"), rs.getInt("games_medium"), rs.getInt("games_hard"), rs.getInt("score_total_easy"), rs.getInt("score_total_medium"), rs.getInt("score_total_hard"), rs.getString("url_avatar"));
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();
                return rs.next();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }

//        public boolean update(User user, String difficulty, int score) {
//            try (Connection connection = DBManager.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET games_" + difficulty + " = games_" + difficulty + " + 1, total_score_" + difficulty + " = total_score_" + difficulty + " + " + score + ", best_" + difficulty + " = GREATEST(best_" + difficulty + ", " + score + ") WHERE id = " + user.getId());
//            ){
//
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
}
