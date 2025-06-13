package dao;

import model.Document;
import utils.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe DAO che gestisce le transazioni con il database wordageddon.db per quanto riguarda i documenti.
 * Fornisce metodi per inserire, selezionare e cancellare documenti.
 */
public class DocumentDAO {

    /**
     * Seleziona un documento in base alla lingua selezionata.
     * Cattura un SQLException in caso si di errore nella selezione.
     *
     * @param language l'ID del documento da selezionare
     * @return una lista di documenti della lingua selezionata
     */
    public static List<Document> selectDocumentByLanguage(String language) {
        List<Document> documents = new ArrayList<>();
        try(Connection c = DBManager.getConnection();
            PreparedStatement sta = c.prepareStatement("SELECT * FROM documents WHERE language = ?")) {
            sta.setString(1, language);
            ResultSet rs = sta.executeQuery();
            while (rs.next()) {
                Document doc = new Document(rs.getLong("id"), language, rs.getString("content"), rs.getString("title"));
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * Seleziona un documento in base al titolo.
     * Cattura un SQLException in caso si di errore nella selezione.
     *
     * @param title il titolo del documento da selezionare
     * @return un Optional del Document trovato, è null se non viene trovato
     */
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

    /**
     * Inserisce un nuovo documento nel database.
     *
     * @param document il documento da inserire
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
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

    /**
     * Elimina un documento dal database.
     * Cattura un SQLException in caso si di errore nell'eliminazione.
     *
     * @param document il documento da eliminare
     */
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

    /**
     * Seleziona tutti i documenti presenti nel database.
     * Cattura un SQLException in caso si di errore nella selezione.
     *
     * @return una lista di tutti i documenti
     */
    public static List<Document> selectAllDocuments() {
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
