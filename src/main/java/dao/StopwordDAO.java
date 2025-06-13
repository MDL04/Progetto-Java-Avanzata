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

/**
 * Classe DAO che gestisce le transazioni con il database wordageddon.db per quanto riguarda le stopwords.
 * Fornisce metodi per inserire, selezionare e cancellare stopwords.
 */
public class StopwordDAO {

    /**
     * Seleziona le stopwords dal database in base alla lingua selezionata
     * Cattura un SQLException in caso di errore nella selezione.
     *
     * @param language la lingua delle stopwords da selezionare
     * @return una lista con le stopwords selezionate
     */
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


    /**
     * Seleziona una stopword in base al titolo del file.
     * Cattura un SQLException in caso di errore nella selezione.
     *
     * @param title il titolo del file dove che contiene le stopwords
     * @return un oggetto Optional di Stopword che conterrà le stopwords, è vuoto se non viene trovato nulla
     */
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


    /**
     * Inserisce un file di stopwords nel database
     * Cattura un SQLException in caso di errore nell'inserimento.
     *
     * @param stopword l'oggetto Stopword da inserire nel database
     */
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


    /**
     * Elimina una stopword dal database.
     * Cattura un SQLException in caso di errore nell'eliminazione.
     *
     * @param stopword l'oggetto Stopword da eliminare
     */
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

    /**
     * Seleziona tutte le stopwords presenti nel database.
     * Cattura un SQLException in caso di errore nella selezione.
     *
     * @return una lista di stopwords presenti nel database
     */
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
