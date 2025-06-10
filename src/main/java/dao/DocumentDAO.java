package dao;

import java.util.ArrayList;
import java.util.List;

import model.Document;
import utils.DBManager;
import java.sql.*;


public class DocumentDAO {

    public static List<Document> selectDocumentByLanguage(String language) {
        List<Document> documents = new ArrayList<>();
        try(Connection c = DBManager.getConnection();
            PreparedStatement sta = c.prepareStatement("SELECT * FROM documents WHERE language = ?")) {
            sta.setString(1, language);
            ResultSet rs = sta.executeQuery();
            if (rs.next()) {
                Document doc = new Document(rs.getLong("id"), language, rs.getString("content"));
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static boolean insertDocument(Document document) throws SQLException {
        try(Connection c = DBManager.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO documents (language, content) VALUES (?, ?)")) {
            preparedStatement.setString(1, document.getLanguage());
            preparedStatement.setString(2, document.getContent());
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteDocument(Document document) {
        try(Connection c = DBManager.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM documents WHERE id = ?")){
            preparedStatement.setLong(1, document.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nessun utente eliminato.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
