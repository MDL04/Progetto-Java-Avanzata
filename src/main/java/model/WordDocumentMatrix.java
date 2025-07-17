package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Classe che gestisce l'analisi delle parole nei documenti, l'importazione/esportazione di dati e
 * il calcolo delle frequenze.
 */
public class WordDocumentMatrix {
    private final Map<String, Map<String, Integer>> matrix = new HashMap<>();
    private final Set<String> stopwords = new HashSet<>();

    /**
     * Imposta la lista delle stopwords
     * @param stopwordList
     */
    public void setStopwords(Collection<String> stopwordList) {
        stopwords.clear();
        stopwords.addAll(stopwordList.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    /**
     * Salva le stopwords in un file di testo
     * @param path
     * @throws IOException
     */
    public void salvaStopwords(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String stopword : stopwords) {
                writer.write(stopword);
                writer.newLine();
            }
        }
    }

    private String normalizzaParola(String parola) {
        return parola
                .toLowerCase()
                .trim()
                .replaceAll("[^\\p{L}\\p{Nd}]", "");
    }

    /**
     * Carica le stopwords da un file di testo
     * @param path
     * @throws IOException
     */
    public void caricaStopwords(String path) throws IOException {
        stopwords.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalizzata = normalizzaParola(line);
                if (!normalizzata.isEmpty()) {
                    stopwords.add(normalizzata);
                }
            }
        }
    }

    /**
     * Restituisce il set di stopwords
     * @return
     */
    public Set<String> getStopwords() {
        return stopwords;
    }

    /**
     * Aggiunge un documento alla matrice con le frequenze delle parole
     * @param nome
     * @param contenuto
     */
    public void aggiungiDocumento(String nome, String contenuto) {
        Map<String, Integer> frequenze = new HashMap<>();
        String[] parole = contenuto.toLowerCase()
                .replaceAll("[^\\p{L}\\p{Nd}]+", " ")
                .split("\\s+");


        for (String parola : parole) {
            if (parola.isBlank() || stopwords.contains(parola)) continue;
            frequenze.put(parola, frequenze.getOrDefault(parola, 0) + 1);
        }

        matrix.put(nome, frequenze);
    }

    /**
     * Restituisce la frequenza di una parola
     * @param documento
     * @param parola
     * @return
     */
    public int getFrequenza(String documento, String parola) {
        return matrix.getOrDefault(documento, Map.of())
                .getOrDefault(parola.toLowerCase(), 0);
    }

    /**
     * Restituisce il set di documenti
     * @return
     */
    public Set<String> getDocumenti() {
        return matrix.keySet();
    }

    /**
     * Restituisce il set di parole
     * @return
     */
    public Set<String> getTutteLeParole() {
        return matrix.values().stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }


    /**
     * Esporta la matrice di documenti e parole in un file CSV.
     * @param path
     * @throws IOException
     */
    public void esportaCSV(String path) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("documento,parola,frequenza");
            for (String doc : matrix.keySet()) {
                for (Map.Entry<String, Integer> entry : matrix.get(doc).entrySet()) {
                    writer.printf("%s,%s,%d%n", doc, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * Importa una matrice di documenti e parole da un file CSV
     * @param wdmPath
     * @param stopwordPath
     * @return
     * @throws IOException
     */
    public static WordDocumentMatrix importaCSV(String wdmPath, String stopwordPath) throws IOException {
        WordDocumentMatrix matrix = new WordDocumentMatrix();
        matrix.caricaStopwords(stopwordPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(wdmPath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String doc = parts[0].trim();
                String word = parts[1].trim().toLowerCase();
                int freq = Integer.parseInt(parts[2].trim());

                if (matrix.stopwords.contains(word)) continue;

                matrix.matrix
                        .computeIfAbsent(doc, k -> new HashMap<>())
                        .put(word, freq);
            }
        }
        return matrix;
    }

    /**
     * Restituisce la matrice invertita, ossia una mappa che associa ogni parola ai documenti in cui appare e alla frequenza
     * @return
     */
    public Map<String, Map<String, Integer>> getMatriceInversa() {
        Map<String, Map<String, Integer>> inversa = new HashMap<>();
        for (String doc : matrix.keySet()) {
            for (Map.Entry<String, Integer> entry : matrix.get(doc).entrySet()) {
                String parola = entry.getKey();
                int freq = entry.getValue();

                inversa.computeIfAbsent(parola, k -> new HashMap<>())
                        .put(doc, freq);
            }
        }
        return inversa;
    }

    /**
     * Restituisce la mappa delle frequenze per un documento specifico
     *
     * @param titolo Il titolo del documento
     * @return Una mappa che associa le parole alle loro frequenze nel documento, o una mappa vuota se il documento non esiste
     */
    public Map<String, Integer> getFrequenzeDocumento(String titolo) {
        return matrix.getOrDefault(titolo, Map.of());
    }

    /**
     * Aggiunge direttamente una mappa di frequenze per un documento.
     *
     * @param titolo Il titolo del documento
     * @param frequenze Una mappa che associa le parole alle loro frequenze nel documento
     */
    public void aggiungiFrequenzeDocumento(String titolo, Map<String, Integer> frequenze) {
        matrix.put(titolo, new HashMap<>(frequenze));
    }


}
