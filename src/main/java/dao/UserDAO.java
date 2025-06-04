package dao;

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
}
