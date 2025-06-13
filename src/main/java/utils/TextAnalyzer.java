package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe che fornisce metodi per analizzare il contenuto di un testo e calcola
 * la frequenza delle parole
 */

public class TextAnalyzer {
    private final StopwordsManager stopwordsManager;

    /**
     * Crea un'istanza di quest'oggetto con il gestore delle stopwords
     *
     * @param stopwordsManager
     */
    public TextAnalyzer(final StopwordsManager stopwordsManager) {
        this.stopwordsManager = stopwordsManager;
    }


    /**
     * Analizza il contenuto di un testo e restituisce una mappa con le parole e le loro frequenze.
     * @param content
     * @return
     */
    public Map<String, Integer> analyze(String content) {
        Map<String, Integer> frequencies = new HashMap<>();

        String[] words = content.toLowerCase()
                .replaceAll("[^a-zàèéìòù]", " ")
                .split("\\s+");

        for (String word : words) {
            if (!word.isBlank() && !stopwordsManager.isStopword(word)) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }

        return frequencies;
    }

    /**
     * Restituisce una lista di parole più frequenti da una mappa di frequenze
     *
     * @param freqMap
     * @param topN
     * @return
     */
    public List<String> getMostFrequentWords(Map<String, Integer> freqMap, int topN) {
        return freqMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }
}

