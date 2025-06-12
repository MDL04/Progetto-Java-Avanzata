package quiz;

import model.WordDocumentMatrix;

import java.util.*;

public class QuestionFactory {
    private final WordDocumentMatrix matrix;
    private final Random random = new Random();

    public QuestionFactory(WordDocumentMatrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("WordDocumentMatrix non può essere null");
        }
        this.matrix = matrix;
    }

    public Question generateQuestion(int id, QuestionType type) {
        validateMatrix(); // Controlla che la matrice sia valida
        return switch (type) {
            case ABSOLUTE_FREQUENCY -> generateAbsoluteFrequencyQuestion(id);
            case COMPARISON -> generateComparisonQuestion(id);
            case DOCUMENT_SPECIFIC -> generateDocumentSpecificQuestion(id);
            case EXCLUSION -> generateExclusionQuestion(id);
        };
    }

    public Question generateAbsoluteFrequencyQuestion(int id) {
        validateMatrix();
        String doc = getRandomDocument();
        String parola = getRandomWord();

        List<String> templates = QuestionType.ABSOLUTE_FREQUENCY.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda = String.format(template, parola, doc);
        int risposta = matrix.getFrequenza(doc, parola);
        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));

        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.ABSOLUTE_FREQUENCY, domanda, opzioni, String.valueOf(risposta));
    }

    public Question generateComparisonQuestion(int id) {
        validateMatrix();
        String doc = getRandomDocument();
        List<String> parole = getRandomWords(10);

        List<String> templates = QuestionType.COMPARISON.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        int risposta;

        switch (template) {
            case "Quante volte compare la parola più frequente nel documento '%s'?" -> {
                domanda = String.format(template, doc);
                risposta = parole.stream()
                        .mapToInt(p -> matrix.getFrequenza(doc, p))
                        .max()
                        .orElse(0);
            }
            case "Qual è la frequenza massima di una parola nel documento '%s'?" -> {
                domanda = String.format(template, doc);
                risposta = matrix.getTutteLeParole().stream()
                        .mapToInt(p -> matrix.getFrequenza(doc, p))
                        .max()
                        .orElse(0);
            }
            case "Qual è la differenza di frequenza tra '%s' e '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                domanda = String.format(template, p1, p2, doc);
                int freq1 = matrix.getFrequenza(doc, p1);
                int freq2 = matrix.getFrequenza(doc, p2);
                risposta = Math.abs(freq1 - freq2);
            }
            case "Quante volte in più compare '%s' rispetto a '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                domanda = String.format(template, p1, p2, doc);
                int freq1 = matrix.getFrequenza(doc, p1);
                int freq2 = matrix.getFrequenza(doc, p2);
                risposta = Math.max(0, freq1 - freq2);
            }
            case "Qual è la somma delle frequenze di '%s' e '%s' nel documento '%s'?" -> {
                String p1 = getRandomWord();
                String p2 = getRandomWord();
                domanda = String.format(template, p1, p2, doc);
                int freq1 = matrix.getFrequenza(doc, p1);
                int freq2 = matrix.getFrequenza(doc, p2);
                risposta = freq1 + freq2;
            }
            default -> throw new IllegalStateException("Template non valido: " + template);
        }

        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));
        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.COMPARISON, domanda, opzioni, String.valueOf(risposta));
    }

    public Question generateDocumentSpecificQuestion(int id) {
        validateMatrix();
        String parola = getRandomWord();
        List<String> docs = new ArrayList<>(matrix.getDocumenti());

        List<String> templates = QuestionType.DOCUMENT_SPECIFIC.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        int risposta;

        switch (template) {
            case "In quanti documenti compare la parola '%s'?" -> {
                domanda = String.format(template, parola);
                risposta = (int) docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .map(freq -> freq > 0 ? 1 : 0)
                        .sum();
            }
            case "Quante volte compare la parola '%s' nel documento dove è più frequente?" -> {
                domanda = String.format(template, parola);
                risposta = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .max()
                        .orElse(0);
            }
            case "Qual è la frequenza totale della parola '%s' in tutti i documenti?" -> {
                domanda = String.format(template, parola);
                risposta = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .sum();
            }
            case "In quanti documenti la parola '%s' compare almeno 2 volte?" -> {
                domanda = String.format(template, parola);
                risposta = (int) docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .map(freq -> freq >= 2 ? 1 : 0)
                        .sum();
            }
            case "Qual è la frequenza media (arrotondata) della parola '%s' nei documenti dove compare?" -> {
                domanda = String.format(template, parola);
                List<Integer> frequenze = docs.stream()
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .filter(freq -> freq > 0)
                        .boxed()
                        .toList();
                risposta = frequenze.isEmpty() ? 0 :
                        (int) Math.round(frequenze.stream().mapToInt(Integer::intValue).average().orElse(0));
            }
            default -> throw new IllegalStateException("Template non valido: " + template);
        }

        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));
        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.DOCUMENT_SPECIFIC, domanda, opzioni, String.valueOf(risposta));
    }

    public Question generateExclusionQuestion(int id) {
        validateMatrix();
        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());

        List<String> templates = QuestionType.EXCLUSION.getQuestionList();
        String template = templates.get(random.nextInt(templates.size()));

        String domanda;
        int risposta;

        switch (template) {
            case "Quante volte compare la parola '%s' in tutti i documenti tranne '%s'?" -> {
                String parola = getRandomWord();
                String docEscluso = getRandomDocument();
                domanda = String.format(template, parola, docEscluso);
                risposta = docs.stream()
                        .filter(doc -> !doc.equals(docEscluso))
                        .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                        .sum();
            }
            case "Qual è la frequenza della parola più comune in tutti i documenti?" -> {
                domanda = String.format(template);
                risposta = parole.stream()
                        .mapToInt(parola -> docs.stream()
                                .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                                .sum())
                        .max()
                        .orElse(0);
            }
            case "Quante parole diverse compaiono almeno 3 volte nel documento '%s'?" -> {
                String doc = getRandomDocument();
                domanda = String.format(template, doc);
                risposta = (int) parole.stream()
                        .mapToInt(parola -> matrix.getFrequenza(doc, parola))
                        .filter(freq -> freq >= 3)
                        .count();
            }
            case "Qual è il numero di parole uniche nel documento '%s'?" -> {
                String doc = getRandomDocument();
                domanda = String.format(template, doc);
                risposta = (int) parole.stream()
                        .mapToInt(parola -> matrix.getFrequenza(doc, parola))
                        .filter(freq -> freq > 0)
                        .count();
            }
            case "Quante volte compare complessivamente la parola meno frequente in tutti i documenti?" -> {
                domanda = String.format(template);
                risposta = parole.stream()
                        .mapToInt(parola -> docs.stream()
                                .mapToInt(doc -> matrix.getFrequenza(doc, parola))
                                .sum())
                        .filter(freq -> freq > 0)
                        .min()
                        .orElse(0);
            }
            default -> throw new IllegalStateException("Template non valido: " + template);
        }

        List<String> opzioni = new ArrayList<>(generaDistrattoriNumerici(risposta));
        Collections.shuffle(opzioni);
        return new Question(id, QuestionType.EXCLUSION, domanda, opzioni, String.valueOf(risposta));
    }

    // ========== Metodi di supporto ==========

    private List<String> generaDistrattoriNumerici(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);

        while (opzioni.size() < 4) {
            int offset = random.nextInt(Math.max(5, corretta / 2 + 1)) + 1;
            int distrattore = corretta + (random.nextBoolean() ? offset : -offset);
            opzioni.add(Math.max(distrattore, 0));
        }

        return opzioni.stream()
                .map(String::valueOf)
                .sorted((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)))
                .toList();
    }

    private String getRandomDocument() {
        if (matrix == null || matrix.getDocumenti() == null || matrix.getDocumenti().isEmpty()) {
            throw new IllegalStateException("Nessun documento disponibile nella matrice");
        }
        List<String> docs = new ArrayList<>(matrix.getDocumenti());
        return docs.get(random.nextInt(docs.size()));
    }

    private String getRandomWord() {
        if (matrix == null || matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola disponibile nella matrice");
        }
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        return parole.get(random.nextInt(parole.size()));
    }

    private List<String> getRandomWords(int count) {
        if (matrix == null || matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola disponibile nella matrice");
        }
        List<String> parole = new ArrayList<>(matrix.getTutteLeParole());
        Collections.shuffle(parole);
        return parole.subList(0, Math.min(count, parole.size()));
    }

    private void validateMatrix() {
        if (matrix == null) {
            throw new IllegalStateException("WordDocumentMatrix non inizializzata");
        }
        if (matrix.getDocumenti() == null || matrix.getDocumenti().isEmpty()) {
            throw new IllegalStateException("Nessun documento caricato nella matrice");
        }
        if (matrix.getTutteLeParole() == null || matrix.getTutteLeParole().isEmpty()) {
            throw new IllegalStateException("Nessuna parola trovata nei documenti");
        }
    }
}