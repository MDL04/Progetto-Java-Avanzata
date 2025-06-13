package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Gestisce l'elenco della stopwords
 */

public class StopwordsManager {
    private final List<String> stopwords = new ArrayList<String>();
    // Oggetto Random per la generazione casuale
    private final Random random = new Random();

    /**
     * Carica una lista di parole e le converte in maiuscolo
     *
     * @param words
     */
    public void loadFromList(List<String> words) {
        stopwords.clear();
        stopwords.addAll(words.stream().map(String::toLowerCase).toList());
    }

    /**
     * Verifica se una parola è presente nell'elenco delle stopwords
     *
     * @param word
     * @return
     */
    public boolean isStopword(String word) {
        return stopwords.contains(word.toLowerCase());
    }

    /**
     * Seleziona casualmente un numero di stopwords
     *
     * @param n Il numero di stopword da selezionare
     * @return
     */
    public List<String> getRandomStopwords(int n) {
        Collections.shuffle(stopwords);
        return stopwords.stream().limit(n).toList();
    }

    public void clearStopwords() {
        stopwords.clear();
    }
}
