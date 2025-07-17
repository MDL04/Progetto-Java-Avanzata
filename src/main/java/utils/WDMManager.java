package utils;

import dao.DocumentDAO;
import dao.StopwordDAO;
import model.Document;
import model.Stopword;
import model.WordDocumentMatrix;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe responsabile della gestione della matrice dei documenti
 */

public class WDMManager {
    private static WordDocumentMatrix matrix;

    /**
     * Restituisce l'istanza della matrice e se non è stata creata, la carica da file CSV, altrimenti da database
     * @return
     */
    public static WordDocumentMatrix getInstance() {
        if (matrix == null) {
            File csv = new File("wdm.csv");
            File stopwords = new File("stopwords.txt");
            if (csv.exists() && stopwords.exists()) {
                try {
                    matrix = WordDocumentMatrix.importaCSV("wdm.csv", "stopwords.txt");
                } catch (IOException e) {
                    matrix = new WordDocumentMatrix();
                    caricaDaDatabase();
                    try {
                        matrix.esportaCSV("wdm.csv");
                        matrix.salvaStopwords("stopwords.txt");
                    } catch (IOException ignored) {}
                }
            } else {
                matrix = new WordDocumentMatrix();
                caricaDaDatabase();
                try {
                    matrix.esportaCSV("wdm.csv");
                    matrix.salvaStopwords("stopwords.txt");
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                    System.err.println("Errore durante l'esportazione del file CSV o salvataggio delle stopwords.");
                }
            }
        }
        return matrix;
    }

    /**
     * Elimina l'istanza della matrice in memoria
     */
    public static void delete() {
        matrix = null;
    }

    /**
     * Carica i documenti e le stopwords dal databaase e li carica nella matrice.
     */
    private static void caricaDaDatabase() {
        List<Document> documenti = DocumentDAO.selectAllDocuments();
        List<Stopword> stopwordsDB = StopwordDAO.selectAllStopwords();

        Set<String> tutteLeStopwords = stopwordsDB.stream()
                .flatMap(sw -> Arrays.stream(sw.getContent().split("\\s+")))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        matrix.setStopwords(tutteLeStopwords);

        for (Document doc : documenti) {
            matrix.aggiungiDocumento(doc.getTitle(), doc.getContent());
        }
    }

}
