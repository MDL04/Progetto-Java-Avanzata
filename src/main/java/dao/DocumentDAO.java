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

    public static Optional<Document> selectDocumentByTitle(String tablename, String language, String title) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tablename + " WHERE title = ?")) {
            preparedStatement.setString(1, title);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                String content = rs.getString("content");
                return Optional.ofNullable(new Document(id, language, content, title));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }



    public static void insertITDocument(Document document) throws SQLException {
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO it_documents (title, content, language) VALUES (?, ?, 'it')")) {
            preparedStatement.setString(1, document.getTitle());
            preparedStatement.setString(2, document.getContent());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertENDocument(Document document) throws SQLException {
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO en_documents (title, content, language) VALUES (?, ?, 'en')")) {
            preparedStatement.setString(1, document.getLanguage());
            preparedStatement.setString(2, document.getContent());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteDocument(Document document) {
        String tableName;
        switch (document.getLanguage()) {
            case "it":
                tableName = "it_documents";
                break;
            case "en":
                tableName = "en_documents";
                break;
            default:
                System.err.println("Lingua non supportata: " + document.getLanguage());
                return;
        }
        try(Connection c = DBManager.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?")){
            preparedStatement.setLong(1, document.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun utente eliminato.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<Document> selectAllFromItalian() throws SQLException {
        return selectAllFrom("it_documents", "it");
    }

    public static List<Document> selectAllFromEnglish() throws SQLException {
        return selectAllFrom("en_documents", "en");
    }

    static List<Document> selectAllFrom(String tableName, String language) {
        List<Document> documents = new ArrayList<>();
        try(Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
        ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Document doc = new Document(rs.getLong("id"), language, rs.getString("content"), rs.getString("title"));
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }
}
