package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WordDocumentMatrix {
    private final Map<String, Map<String, Integer>> matrix = new HashMap<>();
    private final Set<String> stopwords = new HashSet<>();

    // --- STOPWORDS ---

    public void setStopwords(Collection<String> stopwordList) {
        stopwords.clear();
        stopwords.addAll(stopwordList.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    public void salvaStopwords(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String stopword : stopwords) {
                writer.write(stopword);
                writer.newLine();
            }
        }
    }

    public void caricaStopwords(String path) throws IOException {
        stopwords.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim().toLowerCase());
            }
        }
    }

    public Set<String> getStopwords() {
        return stopwords;
    }

    // --- DOCUMENTI ---

    public void aggiungiDocumento(String nome, String contenuto) {
        Map<String, Integer> frequenze = new HashMap<>();
        String[] parole = contenuto.toLowerCase()
                .replaceAll("[^\\p{L}\\p{Nd}]+", " ") // rimuove punteggiatura
                .split("\\s+");

        System.out.println("[DEBUG] Stopwords attive: " + stopwords);

        for (String parola : parole) {
            if (parola.isBlank() || stopwords.contains(parola)) continue;
            frequenze.put(parola, frequenze.getOrDefault(parola, 0) + 1);
            System.out.println("[DEBUG] >>> aggiungiDocumento: " + nome);
            System.out.println("[DEBUG] >>> Frequenze parole = " + frequenze);

        }

        matrix.put(nome, frequenze);
    }

    public int getFrequenza(String documento, String parola) {
        return matrix.getOrDefault(documento, Map.of())
                .getOrDefault(parola.toLowerCase(), 0);
    }

    public boolean contieneParola(String documento, String parola) {
        return getFrequenza(documento, parola) > 0;
    }

    public Set<String> getDocumenti() {
        return matrix.keySet();
    }

    public Set<String> getTutteLeParole() {
        return matrix.values().stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }

    public Map<String, Integer> getFrequenzePerDocumento(String documento) {
        return matrix.getOrDefault(documento, Map.of());
    }

    // --- EXPORT / IMPORT CSV ---

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

                matrix.matrix
                        .computeIfAbsent(doc, k -> new HashMap<>())
                        .put(word, freq);
            }
        }

        return matrix;
    }

    // --- MATRICE INVERTITA (parola -> doc -> frequenza) ---

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

    // --- UTILITY ---

    public String documentoConFrequenzaMassima(String parola) {
        parola = parola.toLowerCase();
        String bestDoc = null;
        int max = -1;

        for (String doc : matrix.keySet()) {
            int f = getFrequenza(doc, parola);
            if (f > max) {
                max = f;
                bestDoc = doc;
            }
        }
        return bestDoc;
    }

    public boolean parolaInTuttiIDocumenti(String parola) {
        final String parolaLowerCase = parola.toLowerCase();
        return matrix.keySet().stream()
                .allMatch(doc -> contieneParola(doc, parolaLowerCase));
    }

    public boolean parolaInNessunDocumento(String parola) {
        final String parolaLowerCase = parola.toLowerCase();
        return matrix.keySet().stream()
                .noneMatch(doc -> contieneParola(doc, parolaLowerCase));
    }

}
