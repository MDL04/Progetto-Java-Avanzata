package utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WordDocumentMatrix implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Map<String, Integer>> matrix = new HashMap<>();
    private final Set<String> stopwords = new HashSet<>();

    public void setStopwords(Collection<String> stopwordList) {
        stopwords.clear();
        stopwords.addAll(stopwordList.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet()));
    }

    public void aggiungiDocumento(String nome, String contenuto) {
        Map<String, Integer> frequenze = new HashMap<>();
        String[] parole = contenuto.toLowerCase()
                .replaceAll("[^\\p{L}\\p{Nd}]+", " ") // pulizia base
                .split("\\s+");

        for (String parola : parole) {
            if (parola.isBlank() || stopwords.contains(parola)) continue;
            frequenze.put(parola, frequenze.getOrDefault(parola, 0) + 1);
        }

        matrix.put(nome, frequenze);
    }

    public int getFrequenza(String documento, String parola) {
        return matrix.getOrDefault(documento, Map.of())
                .getOrDefault(parola.toLowerCase(), 0);
    }

    public Set<String> getDocumenti() {
        return matrix.keySet();
    }

    public Set<String> getTutteLeParole() {
        return matrix.values().stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());
    }

    public boolean contieneParola(String documento, String parola) {
        return getFrequenza(documento, parola) > 0;
    }

    public void salva(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this);
        }
    }

    public static WordDocumentMatrix carica(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (WordDocumentMatrix) ois.readObject();
        }
    }
}
