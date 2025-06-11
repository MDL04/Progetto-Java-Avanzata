package utils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class WDMManager {
    private static final String WDM_FILE = "resources/wordDocumentMatrix.ser";

    public static void rigenera(List<File> documenti, List<String> stopwords) {
        WordDocumentMatrix matrix = new WordDocumentMatrix();
        matrix.setStopwords(stopwords);

        for (File doc : documenti) {
            try {
                String contenuto = Files.readString(doc.toPath());
                matrix.aggiungiDocumento(doc.getName(), contenuto);
            } catch (Exception e) {
                System.err.println("Errore nel file: " + doc.getName());
                e.printStackTrace();
            }
        }

        try {
            matrix.salva(WDM_FILE);
            System.out.println("WordDocumentMatrix salvata correttamente.");
        } catch (Exception e) {
            System.err.println("Errore nel salvataggio della WDM.");
            e.printStackTrace();
        }
    }

    public static void delete() {
        File f = new File(WDM_FILE);
        if (f.exists()) {
            f.delete();
            System.out.println("WordDocumentMatrix eliminata.");
        }
    }
}
