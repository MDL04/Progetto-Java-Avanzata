package utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe utilizzata per gestire una lista di parole e generare distrattori casuali.
 */

public class DistractorProvider {
    private final List<String> dictionary = new ArrayList<>();
    private final Random rand = new Random();

    /**
     * Carica una lista di parole nel dizionario, formattandole in minuscolo
     * @param words
     */
    public void loadFromList(List<String> words) {
        dictionary.clear();
        words.forEach(word -> dictionary.add(word.toLowerCase()));
    }

    /**
     * Restituisce una lista di distrattori random, escludendone alcune
     *
     * @param n numero di distrattori da utilizzare
     * @param avoidWords Parole da evitare
     * @return
     */
    public List<String> getRandomDistractors(int n, Set<String> avoidWords) {
        List<String> filtered = dictionary.stream().filter(w -> !avoidWords.contains(w.toLowerCase())).collect(Collectors.toList());

        Collections.shuffle(filtered);
        return filtered.stream().limit(n).collect(Collectors.toList());
    }

}

