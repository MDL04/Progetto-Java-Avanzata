package utils;

import java.util.*;


public class StopwordsManager {
    private final List<String> stopwords = new ArrayList<String>();
    private final Random random = new Random();

    public void loadFromList(List<String> words) {
        stopwords.clear();
        stopwords.addAll(words.stream().map(String::toLowerCase).toList());
    }

    public boolean isStopword(String word) {
        return stopwords.contains(word.toLowerCase());
    }

    public List<String> getRandomStopwords(int n) {
        Collections.shuffle(stopwords);
        return stopwords.stream().limit(n).toList();
    }

    public void clearStopwords() {
        stopwords.clear();
    }
}
