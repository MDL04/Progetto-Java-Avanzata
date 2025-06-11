package utils;

import dao.DocumentDAO;
import dao.StopwordDAO;
import model.Document;
import model.Stopword;
import model.WordDocumentMatrix;

import java.util.*;
import java.util.stream.Collectors;

public class WDMManager {
    private static WordDocumentMatrix matrix;

    // Restituisce l'istanza singleton, caricandola dal DB se necessario
    public static WordDocumentMatrix getInstance() {
        if (matrix == null) {
            matrix = new WordDocumentMatrix();
            caricaDaDatabase();
        }
        return matrix;
    }

    // Elimina l'istanza in memoria (verrà ricostruita alla prossima chiamata a getInstance)
    public static void delete() {
        matrix = null;
    }

    // Metodo privato per caricare documenti e stopwords dal database
    private static void caricaDaDatabase() {
        List<Document> documenti = DocumentDAO.selectAllDocuments();
        List<Stopword> stopwordsDB = StopwordDAO.selectAllStopwords();

        // Unisci tutte le stopwords (da più righe) in un unico set
        Set<String> tutteLeStopwords = stopwordsDB.stream()
                .flatMap(sw -> Arrays.stream(sw.getContent().split("\\s+")))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        matrix.setStopwords(tutteLeStopwords);

        // Aggiungi i documenti alla matrice
        for (Document doc : documenti) {
            matrix.aggiungiDocumento(doc.getTitle(), doc.getContent());
        }
    }
}
