package quiz;

import utils.WordDocumentMatrix;

import java.util.*;

public class QuestionFactory {
    private final WordDocumentMatrix matrix;
    private final Random random = new Random();

    public QuestionFactory(WordDocumentMatrix matrix) {
        this.matrix = matrix;
    }

    public Question generateAbsoluteFrequencyQuestion(int id) {
        List<String> documenti = new ArrayList<>(matrix.getDocumenti());
        String doc = documenti.get(random.nextInt(documenti.size()));

        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        String parola = parole.get(random.nextInt(parole.size()));

        int freq = matrix.getFrequenza(doc, parola);
        String domanda = String.format("Quante volte compare la parola '%s' nel documento '%s'?", parola, doc);

        List<String> opzioni = generaDistrattoriNumerici(freq);
        Collections.shuffle(opzioni);

        return new Question(id, QuestionType.ABSOLUTE_FREQUENCY, domanda, opzioni, String.valueOf(freq));
    }

    private List<String> generaDistrattoriNumerici(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);
        while (opzioni.size() < 4) {
            int offset = random.nextInt(5) + 1;
            int distrattore = corretta + (random.nextBoolean() ? offset : -offset);
            if (distrattore < 0) distrattore = 0;
            opzioni.add(distrattore);
        }
        return opzioni.stream().map(String::valueOf).toList();
    }
}
