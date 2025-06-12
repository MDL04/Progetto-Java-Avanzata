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

public class WDMManager {
    private static WordDocumentMatrix matrix;

    // Restituisce l'istanza singleton, caricandola dal DB se necessario
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

    // Elimina l'istanza in memoria (verrà ricostruita alla prossima chiamata a getInstance)
    public static void delete() {
        matrix = null;
    }

    // Metodo privato per caricare documenti e stopwords dal database
    private static void caricaDaDatabase() {
        System.out.println("=== [DEBUG] Avvio caricamento da database ===");

        // Recupero documenti e stopwords
        List<Document> documenti = DocumentDAO.selectAllDocuments();
        List<Stopword> stopwordsDB = StopwordDAO.selectAllStopwords();

        System.out.println("[DEBUG] Documenti trovati nel DB: " + documenti.size());
        for (Document doc : documenti) {
            System.out.println("[DEBUG] Documento: titolo='" + doc.getTitle() + "', lunghezza contenuto=" + doc.getContent().length());
        }

        System.out.println("[DEBUG] Stopwords trovate nel DB: " + stopwordsDB.size());

        // Unisci tutte le stopwords in un unico set
        Set<String> tutteLeStopwords = stopwordsDB.stream()
                .flatMap(sw -> Arrays.stream(sw.getContent().split("\\s+")))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        System.out.println("[DEBUG] Numero totale di stopwords: " + tutteLeStopwords.size());
        System.out.println("[DEBUG] Prime 10 stopwords: " + tutteLeStopwords.stream().limit(10).toList());

        matrix.setStopwords(tutteLeStopwords);

        // Aggiungi i documenti alla matrice
        for (Document doc : documenti) {
            System.out.println("[DEBUG] Aggiunta documento: " + doc.getTitle());
            matrix.aggiungiDocumento(doc.getTitle(), doc.getContent());
        }

        // Verifica risultato finale
        System.out.println("[DEBUG] Documenti effettivamente caricati nella matrice: " + matrix.getDocumenti().size());
        System.out.println("[DEBUG] Titoli documenti nella matrice: " + matrix.getDocumenti());
        System.out.println("[DEBUG] Numero parole totali nella matrice: " + matrix.getTutteLeParole().size());

        System.out.println("=== [DEBUG] Fine caricamento da database ===");
    }

}
