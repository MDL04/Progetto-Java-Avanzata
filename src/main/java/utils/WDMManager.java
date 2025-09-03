package utils;

import model.Document;
import model.WordDocumentMatrix;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Classe responsabile della gestione della matrice dei documenti
 */

public class WDMManager {
    private static WordDocumentMatrix matrix;

    /**
     * Restituisce l'istanza della matrice e se non è stata creata, la carica da file CSV, altrimenti analizza i file in resources
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
                    matrix = creaDaFile();
                }
            } else {
                matrix = creaDaFile();
            }
        }
        return matrix;
    }

    /**
     * Elimina l'istanza della matrice in memoria
     */
    public static void delete() {
        matrix = null;
        File wdm = new File("wdm.csv");
        File stopwords = new File("stopwords.txt");
        if (wdm.exists()) wdm.delete();
        if (stopwords.exists()) stopwords.delete();
    }

    /**
     * Carica i documenti e le stopwords dalla cartella risorse e li carica nella matrice.
     */
    private static WordDocumentMatrix creaDaFile() {
        WordDocumentMatrix m = new WordDocumentMatrix();
        try {
            List<Document> docs = new ArrayList<>();
            docs.addAll(FileManager.caricaDocumenti("it"));
            docs.addAll(FileManager.caricaDocumenti("en"));
            List<String> stopwords = new ArrayList<>();
            stopwords.addAll(FileManager.caricaStopwords("it"));
            stopwords.addAll(FileManager.caricaStopwords("en"));
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
