package utils;

import model.Document;
import model.WordDocumentMatrix;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Classe responsabile della gestione della matrice dei documenti
 */

public class WDMManager {
    private static WordDocumentMatrix matrix;

    /**
     * Restituisce l'istanza della matrice e se non è stata creata, la carica da file CSV, altrimenti da database
     * @return
     */
    public static WordDocumentMatrix getInstance(String language) {
        if (matrix == null) {
            File csv = new File("wdm.csv");
            File stopwords = new File("stopwords.txt");
            if (csv.exists() && stopwords.exists()) {
                try {
                    matrix = WordDocumentMatrix.importaCSV("wdm.csv", "stopwords.txt");
                } catch (IOException e) {
                    matrix = creaDaFile(language);
                }
            } else {
                matrix = creaDaFile(language);
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
     * Carica i documenti e le stopwords dalla cartella risorse e li carica nella matrice.
     */
    private static WordDocumentMatrix creaDaFile(String language) {
        WordDocumentMatrix m = new WordDocumentMatrix();
        try {
            List<Document> docs = FileManager.caricaDocumenti(language);
            List<String> stopwords = FileManager.caricaStopwords(language);
            m.setStopwords(stopwords);
            for (Document doc : docs) {
                m.aggiungiDocumento(doc.getTitle(), doc.getContent());
            }
            m.esportaCSV("wdm.csv");
            m.salvaStopwords("stopwords.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }
}
