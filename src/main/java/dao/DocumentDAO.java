package dao;

import java.util.ArrayList;
import java.util.List;

import model.Document;
import utils.DBManager;
import java.sql.*;
import java.util.Optional;


public class DocumentDAO {

    public static List<Document> selectDocumentByLanguage(String language) {
        List<Document> documents = new ArrayList<>();
        try(Connection c = DBManager.getConnection();
            PreparedStatement sta = c.prepareStatement("SELECT * FROM documents WHERE language = ?")) {
            sta.setString(1, language);
            ResultSet rs = sta.executeQuery();
            if (rs.next()) {
                Document doc = new Document(rs.getLong("id"), language, rs.getString("content"), rs.getString("title"));
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static Optional<Document> selectDocumentByTitle(String title) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM documents  WHERE title = ?")) {
            preparedStatement.setString(1, title);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                String content = rs.getString("content");
                String language = rs.getString("language");
                return Optional.ofNullable(new Document(id, language, content, title));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }



    public static void insertDocument(Document document) throws SQLException {
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO documents (title, content, language) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, document.getTitle());
            preparedStatement.setString(2, document.getContent());
            preparedStatement.setString(3, document.getLanguage());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void deleteDocument(Document document) {
        try(Connection c = DBManager.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM documents WHERE id = ?")){
            preparedStatement.setLong(1, document.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun documento eliminato.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<Document> selectAll() {
        List<Document> documents = new ArrayList<>();
        try(Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM documents");
        ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Document doc = new Document(rs.getLong("id"), rs.getString("language"), rs.getString("content"), rs.getString("title"));
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }
}
