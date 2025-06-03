package DAO;

import actors.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    user.setBest_score_easy(rs.getInt("best_easy"));
                    user.setBest_score_hard(rs.getInt("best_hard"));
                    user.setBest_score_normal(rs.getInt("best_medium"));
                    user.setScore_easy(rs.getInt("score_total_easy"));
                    user.setScore_hard(rs.getInt("score_total_hard"));
                    user.setScore_normal(rs.getInt("score_total_medium"));
                    user.setPartite_easy(rs.getInt("games_easy"));
                    user.setPartite_hard(rs.getInt("games_hard"));
                    user.setPartite_normal(rs.getInt("games_medium"));
                    user.setUrl_avatar(rs.getString("avatar_url"));
                }
                result = Optional.ofNullable(user);
            }catch (SQLException e){
                e.printStackTrace();
            }
            return result;
        }
}
