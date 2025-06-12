package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAnalyzer {
    private final StopwordsManager stopwordsManager;

    public TextAnalyzer(final StopwordsManager stopwordsManager) {
        this.stopwordsManager = stopwordsManager;
    }

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

    public List<String> getMostFrequentWords(Map<String, Integer> freqMap, int topN) {
        return freqMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }
}

