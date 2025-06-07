package utils;

import java.util.*;
import java.util.stream.Collectors;

public class DistractorProvider {
    private final List<String> dictionary = new ArrayList<>();
    private final Random rand = new Random();

    public void loadFromList(List<String> words) {
        dictionary.clear();
        words.forEach(word -> dictionary.add(word.toLowerCase()));
    }

    public List<String> getRandomDistractors(int n, Set<String> avoidWords) {
        List<String> filtered = dictionary.stream().filter(w -> !avoidWords.contains(w.toLowerCase())).collect(Collectors.toList());

        Collections.shuffle(filtered);
        return filtered.stream().limit(n).collect(Collectors.toList());
    }

}

