package dao;

import model.Stopword;
import utils.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StopwordDAO {
    public static List<Stopword> selectSWByLanguage(String language) {
        List<Stopword> SWlist = new ArrayList<>();
        try(Connection c = DBManager.getConnection();
            PreparedStatement sta = c.prepareStatement("SELECT * FROM stopwords WHERE language = ?")) {
            sta.setString(1, language);
            ResultSet rs = sta.executeQuery();
            while (rs.next()) {
                Stopword stopword = new Stopword(language, rs.getString("words"),rs.getLong("id") ,rs.getString("title"));
                SWlist.add(stopword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SWlist;
    }

    public static Optional<Stopword> selectSWByTitle(String title) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM stopwords WHERE title = ?")) {
            preparedStatement.setString(1, title);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                String content = rs.getString("words");
                String language = rs.getString("language");
                return Optional.ofNullable(new Stopword(language, content, id ,title));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void insertStopword(Stopword stopword) {
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO stopwords (title, words, language) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1,stopword.getTitle());
            preparedStatement.setString(2,stopword.getContent());
            preparedStatement.setString(3,stopword.getLanguage());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStopword(Stopword stopword){
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM stopwords WHERE id = ?")){
            preparedStatement.setLong(1, stopword.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun file di stopwords eliminato.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Stopword> selectAllStopwords() {
        List<Stopword> SWList = new ArrayList<>();
        try(Connection connection = DBManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM stopwords");
            ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Stopword stopword = new Stopword(rs.getString("language"), rs.getString("words"), rs.getLong("id") ,rs.getString("title"));
                SWList.add(stopword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SWList;
    }


}
